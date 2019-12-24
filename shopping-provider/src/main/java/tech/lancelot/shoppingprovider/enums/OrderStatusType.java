package tech.lancelot.shoppingprovider.enums;

import lombok.Getter;

@Getter
/*订单状态*/
public enum OrderStatusType {
    NEW(0, "新建"),
    COMPLETE(1, "完成"),
    CANCEL(2, "取消");

    private Integer code;

    private String message;

    OrderStatusType(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
}
