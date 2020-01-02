package tech.lancelot.shoppingorder.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.lancelot.shoppingcommon.dto.*;
import tech.lancelot.shoppingcommon.enums.OrderStatus;
import tech.lancelot.shoppingcommon.enums.PayStatus;
import tech.lancelot.shoppingcommon.utils.KeyUtil;
import tech.lancelot.shoppingorder.client.ProductClient;
import tech.lancelot.shoppingorder.domain.OrderDetail;
import tech.lancelot.shoppingorder.domain.OrderMaster;
import tech.lancelot.shoppingorder.repository.OrderDetailRepository;
import tech.lancelot.shoppingorder.repository.OrderMasterRepository;
import tech.lancelot.shoppingorder.stream.OrderStream;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
@EnableBinding(OrderStream.class)
public class OrderService {

    private final OrderMasterRepository orderMasterRepository;
    private final OrderDetailRepository orderDetailRepository;
    private final ProductClient productClient;
    private final OrderStream orderStream;


    @Autowired
    public OrderService(OrderMasterRepository orderMasterRepository,
                        OrderDetailRepository orderDetailRepository,
                        ProductClient productClient,
                        OrderStream orderStream) {

        this.orderMasterRepository = orderMasterRepository;
        this.orderDetailRepository = orderDetailRepository;
        this.productClient = productClient;
        this.orderStream = orderStream;
    }

    /**
     * 创建订单
     */
    @Transactional
    public String Create(OrderInput orderInput) throws Exception {

        //构建订单主表
        OrderMaster orderMaster = new OrderMaster();
        BeanUtils.copyProperties(orderInput, orderMaster);
        //指定默认值
        orderMaster.setOrderId(KeyUtil.genUniqueKey("OM"));
        orderMaster.setOrderStatus(OrderStatus.NEW);
        orderMaster.setPayStatus(PayStatus.WAIT);

        //构建订单明细
        List<String> productIds = orderInput.getOrderItemInputs().stream().map(OrderItemInput::getProductId).collect(Collectors.toList());
        ResultVo<List<ProductInfoOutput>> result2 = productClient.findProductInfosByIds(String.join(",", productIds));
        if (result2.getCode() != 0)
            throw new Exception("调用订单查询接口出错：" + result2.getMsg());
        List<ProductInfoOutput> productInfoOutputs = result2.getData();

        //订单金额总计
        BigDecimal total = new BigDecimal(BigInteger.ZERO);
        for (OrderItemInput orderItemInput : orderInput.getOrderItemInputs()) {
            OrderDetail orderDetail = new OrderDetail();
            BeanUtils.copyProperties(orderItemInput, orderDetail);

            Optional<ProductInfoOutput> productInfoOutputOptional = productInfoOutputs.stream()
                    .filter(s -> s.getProductId().equals(orderItemInput.getProductId())).findFirst();

            if (!productInfoOutputOptional.isPresent())
                throw new Exception(String.format("商品【%s】不存在", orderItemInput.getProductId()));

            ProductInfoOutput productInfoOutput = productInfoOutputOptional.get();
            orderDetail.setDetailId(KeyUtil.genUniqueKey("OD"));
            orderDetail.setOrderId(orderMaster.getOrderId());
            orderDetail.setProductName(productInfoOutput.getProductName());
            orderDetail.setProductPrice(productInfoOutput.getProductPrice().multiply(new BigDecimal(orderDetail.getProductQuantity())));
            orderDetail.setProductIcon(productInfoOutput.getProductIcon());
            total = total.add(orderDetail.getProductPrice());

            orderDetailRepository.save(orderDetail);
        }

        orderMaster.setOrderAmount(total);
        orderMasterRepository.save(orderMaster);

        //扣库存
        StockApplyInput stockApplyInput = new StockApplyInput();
        stockApplyInput.setOrderId(orderMaster.getOrderId());
        stockApplyInput.setOrderItemInputs(orderInput.getOrderItemInputs());
        orderStream.stockApplyOutput().send(MessageBuilder.withPayload(stockApplyInput).build());

        return orderMaster.getOrderId();
    }

    @StreamListener(OrderStream.STOCK_RESULT_INPUT)
    public void processStockResult(StockResultOutput stockResultOutput) {

        log.info("库存消息返回" + stockResultOutput);

        Optional<OrderMaster> optionalOrderMaster = orderMasterRepository.findById(stockResultOutput.getOrderId());
        if (optionalOrderMaster.isPresent()) {
            OrderMaster orderMaster = optionalOrderMaster.get();
            if (stockResultOutput.getIsSuccess()) {
                orderMaster.setOrderStatus(OrderStatus.OCCUPY_SUCCESS);
            } else {
                orderMaster.setOrderStatus(OrderStatus.OCCUPY_FAILURE);
            }
            orderMasterRepository.save(orderMaster);
        }
    }
}
