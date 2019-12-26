package tech.lancelot.shoppingorder.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import tech.lancelot.shoppingcommon.dto.OrderInput;
import tech.lancelot.shoppingcommon.dto.ProductInfoOutput;
import tech.lancelot.shoppingorder.client.ProductClient;
import tech.lancelot.shoppingorder.service.OrderService;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;

/**
 * 订单控制器
 */
@RestController
@RequestMapping("api/v1")
@EnableTransactionManagement
@Slf4j
public class OrderController {

    private final OrderService orderService;
    private final ProductClient productClient;

    @Autowired
    public OrderController(OrderService orderService,ProductClient productClient) {
        this.orderService = orderService;
        this.productClient=productClient;
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

    @GetMapping("/orders/findProductInfosByIds")
    public List<ProductInfoOutput> findProductInfosByIds(){
        List<ProductInfoOutput> productInfoOutputs = productClient
                .findProductInfosByIds("157875196366160022, 157875227953464068");
        return productInfoOutputs;
    }

}
