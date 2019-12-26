package tech.lancelot.shoppingproduct.controller;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.*;
import tech.lancelot.shoppingcommon.dto.ProductInfoOutput;
import tech.lancelot.shoppingcommon.dto.ProductCategoryOutput;
import tech.lancelot.shoppingcommon.utils.ListUtils;
import tech.lancelot.shoppingproduct.domain.ProductCategory;
import tech.lancelot.shoppingproduct.domain.ProductInfo;
import tech.lancelot.shoppingproduct.service.ProductService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.springframework.beans.BeanUtils.copyProperties;

/**
 * 商品控制器
 */
@RestController
@Configuration
@RequestMapping("api/v1")
public class ProductController {

    private final ProductService productService;

    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/productCategories")
    public List<ProductCategoryOutput> findCategories() {
        List<ProductCategory> productCategories = productService.findCategories();
        List<ProductCategoryOutput> productCategoryOutputs = new ArrayList<>();
        for (ProductCategory productCategory : productCategories) {
            ProductCategoryOutput productCategoryOutput = new ProductCategoryOutput();
            copyProperties(productCategory, productCategoryOutput);
            productCategoryOutputs.add(productCategoryOutput);
        }
        return productCategoryOutputs;
    }

    @GetMapping("/productCategories/{categoryId}")
    public ProductCategoryOutput findCategoriesById(@PathVariable Integer categoryId) {
        Optional<ProductCategory> productCategory = productService.findCategoriesById(categoryId);
        if (productCategory.isPresent()) {
            ProductCategoryOutput productCategoryOutput = new ProductCategoryOutput();
            copyProperties(productCategory.get(), productCategoryOutput);
            return productCategoryOutput;
        }
        return null;
    }

    @GetMapping("/productInfos")
    public List<ProductInfoOutput> findProductInfosByIds(@RequestParam(required = false) String productIds) throws Exception {
        //如果传入商品id参数
        if (StringUtils.isNotEmpty(productIds)) {
            List<String> ids = Arrays.asList(productIds.split(","));
            List<ProductInfo> productInfos = productService.findProductInfosByIds(ids);
            List<ProductInfoOutput> productInfoOutputs = ListUtils.copyProperties(productInfos, ProductInfoOutput.class);
            return productInfoOutputs;
        }else{
            List<ProductInfo> productInfos = productService.findProductInfos();
            List<ProductInfoOutput> productInfoOutputs = ListUtils.copyProperties(productInfos, ProductInfoOutput.class);
            return productInfoOutputs;
        }
    }

    @GetMapping("/productCategories/{categoryId}/productInfos")
    public List<ProductInfoOutput> findProductInfosByCategory(@PathVariable Integer categoryId) throws Exception {
        List<ProductInfo> productInfos = productService.findAllProductInfosByCategory(categoryId);
        List<ProductInfoOutput> productInfoOutputs = ListUtils.copyProperties(productInfos, ProductInfoOutput.class);
        return productInfoOutputs;
    }
}
