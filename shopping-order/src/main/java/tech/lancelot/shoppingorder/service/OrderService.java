package tech.lancelot.shoppingorder.service;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.lancelot.shoppingcommon.dto.OrderInput;
import tech.lancelot.shoppingcommon.dto.OrderItemInput;
import tech.lancelot.shoppingcommon.dto.ProductInfoOutput;
import tech.lancelot.shoppingcommon.dto.ResultVo;
import tech.lancelot.shoppingcommon.enums.OrderStatus;
import tech.lancelot.shoppingcommon.enums.PayStatus;
import tech.lancelot.shoppingcommon.utils.KeyUtil;
import tech.lancelot.shoppingorder.client.ProductClient;
import tech.lancelot.shoppingorder.domain.OrderDetail;
import tech.lancelot.shoppingorder.domain.OrderMaster;
import tech.lancelot.shoppingorder.repository.OrderDetailRepository;
import tech.lancelot.shoppingorder.repository.OrderMasterRepository;

import javax.validation.constraints.NotEmpty;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class OrderService {

    private final OrderMasterRepository orderMasterRepository;
    private final OrderDetailRepository orderDetailRepository;
    private final ProductClient productClient;


    @Autowired
    public OrderService(OrderMasterRepository orderMasterRepository,
                        OrderDetailRepository orderDetailRepository,
                        ProductClient productClient) {

        this.orderMasterRepository = orderMasterRepository;
        this.orderDetailRepository = orderDetailRepository;
        this.productClient = productClient;
    }

    /**
     * 创建订单
     *
     */
    @Transactional
    public String Create(OrderInput orderInput) throws Exception {

        //扣库存
        ResultVo result1=productClient.decreaseStock(orderInput.getOrderItemInputs());
        if (result1.getCode() != 0)
            throw new Exception("调用订单扣减库存接口出错：" + result1.getMsg());

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
        return orderMaster.getOrderId();
    }
}
