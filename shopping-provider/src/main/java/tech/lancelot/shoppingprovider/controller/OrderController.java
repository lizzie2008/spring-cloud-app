package tech.lancelot.shoppingprovider.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tech.lancelot.shoppingprovider.dto.ResultOutput;

/**
 * 订单控制器
 */
@RestController
@RequestMapping("/api/products")
public class OrderController {

//    private final ProductInfoService productInfoService;
//    private final ProductCategoryService productCategoryService;
//
//    @Autowired
//    public OrderController(ProductInfoService productInfoService, ProductCategoryService productCategoryService) {
//        this.productInfoService = productInfoService;
//        this.productCategoryService = productCategoryService;
//    }

    @PostMapping
    public ResultOutput create() {

        return null;
    }
}
