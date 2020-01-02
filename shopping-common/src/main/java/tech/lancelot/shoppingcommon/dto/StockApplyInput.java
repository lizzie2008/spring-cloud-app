package tech.lancelot.shoppingcommon.dto;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.util.List;

@Data
public class StockApplyInput {

    //订单id
    @NotEmpty(message = "商品id必填")
    private String orderId;

    //订单明细
    @NotEmpty(message = "明细不能为空")
    private List<OrderItemInput> orderItemInputs;
}
