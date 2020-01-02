package tech.lancelot.shoppingcommon.dto;

import lombok.Data;

@Data
public class StockResultOutput {

    //订单id
    private String orderId;

    //是否占用成功
    private Boolean isSuccess;

    //返回信息
    private String message;
}
