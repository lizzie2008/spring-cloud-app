package tech.lancelot.shoppingorder.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tech.lancelot.shoppingcommon.dto.OrderInput;
import tech.lancelot.shoppingorder.service.OrderService;

import javax.validation.Valid;
import java.util.HashMap;

/**
 * 订单控制器
 */
@RestController
@RequestMapping("api/v1/order")
@EnableTransactionManagement
@Slf4j
public class OrderController {

    private final OrderService orderService;

    @Autowired
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping("/orders")
    public HashMap<String, String> create(@Valid @RequestBody OrderInput orderInput, BindingResult bindingResult) throws Exception {

        if (bindingResult.hasErrors()) {
            throw new Exception(bindingResult.getFieldError().getDefaultMessage());
        }

        HashMap<String, String> map = new HashMap<>();
        map.put("orderId", orderService.Create(orderInput));
        return map;
    }
}
