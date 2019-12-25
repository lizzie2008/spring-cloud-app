package tech.lancelot.shoppingproduct.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tech.lancelot.shoppingcommon.dto.ResultOutput;
import tech.lancelot.shoppingproduct.service.ProductService;

/**
 * 商品控制器
 */
@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;

    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public ResultOutput list() {

        return ResultOutput.success(productService.list());
    }
}
