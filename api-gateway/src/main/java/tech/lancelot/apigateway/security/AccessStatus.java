package tech.lancelot.apigateway.security;

import lombok.Getter;

@Getter
/**
 * 访问状态
 */
public enum AccessStatus {
    NOT_ALLOWED(0, "不允许访问"),
    ALLOWED(1, "允许访问");

    private Integer code;

    private String message;

    AccessStatus(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
}
