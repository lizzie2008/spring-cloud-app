package tech.lancelot.shoppingproduct.controller;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.bind.annotation.*;
import tech.lancelot.shoppingcommon.dto.OrderItemInput;
import tech.lancelot.shoppingcommon.dto.ProductCategoryOutput;
import tech.lancelot.shoppingcommon.dto.ProductInfoOutput;
import tech.lancelot.shoppingcommon.dto.ResultVo;
import tech.lancelot.shoppingcommon.utils.ListUtils;
import tech.lancelot.shoppingproduct.domain.ProductCategory;
import tech.lancelot.shoppingproduct.domain.ProductInfo;
import tech.lancelot.shoppingproduct.service.ProductService;

import java.util.Arrays;
import java.util.List;

import static org.springframework.beans.BeanUtils.copyProperties;
import static tech.lancelot.shoppingcommon.dto.ResultVo.*;

/**
 * 商品控制器
 */
@RestController
@EnableTransactionManagement
@RequestMapping("api/v1/product")
public class ProductController {

    private final ProductService productService;

    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/info")
    public String info()  {
        return "OK";
    }

    @GetMapping("/productCategories")
    public ResultVo findCategories() throws Exception {
        List<ProductCategory> productCategories = productService.findCategories();
        List<ProductCategoryOutput> productCategoryOutputs = ListUtils.copyProperties(productCategories, ProductCategoryOutput.class);
        return success(productCategoryOutputs);
    }

    @GetMapping("/productCategories/{categoryId}")
    public ResultVo findCategoriesById(@PathVariable Integer categoryId) throws Exception {
        ProductCategory productCategory = productService.findCategoriesById(categoryId);
        ProductCategoryOutput productCategoryOutput = new ProductCategoryOutput();
        copyProperties(productCategory, productCategoryOutput);
        return success(productCategoryOutput);
    }

    @GetMapping("/productCategories/{categoryId}/productInfos")
    public ResultVo findProductInfosByCategory(@PathVariable Integer categoryId) throws Exception {
        List<ProductInfo> productInfos = productService.findAllProductInfosByCategory(categoryId);
        List<ProductInfoOutput> productInfoOutputs = ListUtils.copyProperties(productInfos, ProductInfoOutput.class);
        return success(productInfoOutputs);
    }

    @GetMapping("/productInfos")
    public ResultVo findProductInfosByIds(@RequestParam(required = false) String productIds) throws Exception {
        //如果传入商品id参数
        if (StringUtils.isNotEmpty(productIds)) {
            List<String> ids = Arrays.asList(productIds.split(","));
            List<ProductInfo> productInfos = productService.findProductInfosByIds(ids);
            List<ProductInfoOutput> productInfoOutputs = ListUtils.copyProperties(productInfos, ProductInfoOutput.class);
            return success(productInfoOutputs);
        } else {
            List<ProductInfo> productInfos = productService.findProductInfos();
            List<ProductInfoOutput> productInfoOutputs = ListUtils.copyProperties(productInfos, ProductInfoOutput.class);
            return success(productInfoOutputs);
        }
    }

    @PostMapping("/decreaseStock")
    public ResultVo decreaseStock(@RequestBody List<OrderItemInput> orderItemInputs) throws Exception {
        productService.decreaseStock(orderItemInputs);
        return ResultVo.success();
    }
}
