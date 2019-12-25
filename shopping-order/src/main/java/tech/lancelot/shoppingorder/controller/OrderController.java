package tech.lancelot.shoppingorder.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import tech.lancelot.shoppingcommon.dto.OrderInput;
import tech.lancelot.shoppingcommon.dto.ResultOutput;
import tech.lancelot.shoppingorder.service.OrderService;


import javax.validation.Valid;
import java.util.HashMap;

/**
 * 订单控制器
 */
@RestController
@RequestMapping("/api/orders")
@EnableTransactionManagement
public class OrderController {

    private final OrderService orderService;

    @Autowired
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    public ResultOutput create(@Valid @RequestBody OrderInput orderInput, BindingResult bindingResult) throws Exception {

        if (bindingResult.hasErrors()) {
            throw new Exception(bindingResult.getFieldError().getDefaultMessage());
        }

        HashMap<String, String> map = new HashMap<>();
        map.put("orderId",orderService.Create(orderInput));
        return ResultOutput.success(map);
    }
}
