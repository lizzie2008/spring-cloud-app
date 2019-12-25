package tech.lancelot.shoppingorder.service;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.lancelot.shoppingcommon.dto.OrderInput;
import tech.lancelot.shoppingcommon.dto.OrderItemInput;
import tech.lancelot.shoppingcommon.enums.OrderStatus;
import tech.lancelot.shoppingcommon.enums.PayStatus;
import tech.lancelot.shoppingcommon.utils.KeyUtil;
import tech.lancelot.shoppingorder.domain.OrderDetail;
import tech.lancelot.shoppingorder.domain.OrderMaster;
import tech.lancelot.shoppingorder.repository.OrderDetailRepository;
import tech.lancelot.shoppingorder.repository.OrderMasterRepository;

import java.math.BigDecimal;

@Service
public class OrderService {

    private final OrderMasterRepository orderMasterRepository;
    private final OrderDetailRepository orderDetailRepository;


    @Autowired
    public OrderService(OrderMasterRepository orderMasterRepository,
                        OrderDetailRepository orderDetailRepository) {

        this.orderMasterRepository = orderMasterRepository;
        this.orderDetailRepository = orderDetailRepository;
    }

    /**
     * 创建订单
     * @return
     */
    @Transactional
    public String Create(OrderInput orderInput) {

        //构建订单主表
        OrderMaster orderMaster=new OrderMaster();
        BeanUtils.copyProperties(orderInput,orderMaster);
        //指定默认值
        orderMaster.setOrderId(KeyUtil.genUniqueKey("OM"));
        orderMaster.setOrderStatus(OrderStatus.NEW);
        orderMaster.setPayStatus(PayStatus.WAIT);
        //TODO:
        orderMaster.setOrderAmount(new BigDecimal(5.5));

        orderMasterRepository.save(orderMaster);

        //构建订单明细
        for (OrderItemInput orderItemInput:orderInput.getOrderItemInputs()){
            OrderDetail orderDetail=new OrderDetail();
            BeanUtils.copyProperties(orderItemInput,orderDetail);
            orderDetail.setDetailId(KeyUtil.genUniqueKey("OD"));
            orderDetail.setOrderId(orderMaster.getOrderId());
            //TODO:
            orderDetail.setProductName("Tests");
            orderDetail.setProductPrice(new BigDecimal(5.5));
            orderDetail.setProductIcon("/test_icon");

            orderDetailRepository.save(orderDetail);
        }

        return orderMaster.getOrderId();
    }
}
