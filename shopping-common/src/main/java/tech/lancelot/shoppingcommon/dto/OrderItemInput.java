package tech.lancelot.shoppingcommon.dto;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class OrderItemInput {

    /**
     * 商品id.
     */
    @NotEmpty(message = "商品id必填")
    private String productId;

    /**
     * 商品数量.
     */
    @NotEmpty(message = "商品数量必填")
    private Integer productQuantity;
}
