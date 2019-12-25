package tech.lancelot.shoppingcommon.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.util.List;

@Data
public class OrderInput {

    /** 买家名字. */
    @NotEmpty(message = "姓名必填")
    @JsonProperty("name")
    private String buyerName;

    /** 买家手机号. */
    @NotEmpty(message = "手机号必填")
    @JsonProperty("phone")
    private String buyerPhone;

    /** 买家地址. */
    @NotEmpty(message = "地址必填")
    @JsonProperty("address")
    private String buyerAddress;

    /** 买家微信Openid. */
    @NotEmpty(message = "openid必填")
    @JsonProperty("openid")
    private String buyerOpenid;

    @NotEmpty(message = "购物车不能为空")
    @JsonProperty("items")
    private List<OrderItemInput> orderItemInputs;
}
