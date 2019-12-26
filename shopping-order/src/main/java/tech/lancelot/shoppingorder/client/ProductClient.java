package tech.lancelot.shoppingorder.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import tech.lancelot.shoppingcommon.dto.ProductInfoOutput;

import java.util.List;

/**
 * 声明式服务
 */
@FeignClient("shopping-product/api/v1")
public interface ProductClient {

    @GetMapping("/productInfos")
    List<ProductInfoOutput> findProductInfosByIds(@RequestParam(required = false) String productIds);
}
