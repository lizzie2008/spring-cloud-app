package tech.lancelot.shoppingorder.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import tech.lancelot.shoppingcommon.dto.OrderItemInput;
import tech.lancelot.shoppingcommon.dto.ProductInfoOutput;
import tech.lancelot.shoppingcommon.dto.ResultVo;

import java.util.List;

/**
 * 声明式服务
 */
@FeignClient("shopping-product/api/v1/product")
public interface ProductClient {

    @GetMapping("/info")
    String info();

    @GetMapping("/productInfos")
    ResultVo<List<ProductInfoOutput>> findProductInfosByIds(@RequestParam(required = false) String productIds);

    @PostMapping("/decreaseStock")
    ResultVo decreaseStock(@RequestBody List<OrderItemInput> orderItemInputs);
}
