package tech.lancelot.shoppingcommon.enums;

import lombok.Getter;

@Getter
/*订单状态*/
public enum OrderStatus {
    NEW(0, "新建"),
    OCCUPY_SUCCESS(1, "占用库存成功"),
    OCCUPY_FAILURE(2, "占用库存失败"),
    COMPLETE(3, "完成"),
    CANCEL(4, "取消");

    private Integer code;

    private String message;

    OrderStatus(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
}
