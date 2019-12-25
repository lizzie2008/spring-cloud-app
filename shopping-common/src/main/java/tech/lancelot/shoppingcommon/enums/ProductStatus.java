package tech.lancelot.shoppingcommon.enums;

import lombok.Getter;

@Getter
/*商品上下架状态*/
public enum ProductStatus {
    ON_SALE(0, "正常"),
    OFF_SALE(1, "下架");

    private Integer code;

    private String message;

    ProductStatus(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
}
