package tech.lancelot.shoppingprovider.enums;

import lombok.Getter;

@Getter
/*商品上下架状态*/
public enum ProductStatusType {
    OFF_SALE(0, "下架"),
    ON_SALE(1, "上架");

    private Integer code;

    private String message;

    ProductStatusType(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
}
