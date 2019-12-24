package tech.lancelot.shoppingprovider.enums;

import lombok.Getter;

@Getter
/**
 * 支付状态
 */
public enum PayStatusType {
    WAIT(0, "等待支付"),
    SUCCESS(1, "支付成功");

    private Integer code;

    private String message;

    PayStatusType(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
}
