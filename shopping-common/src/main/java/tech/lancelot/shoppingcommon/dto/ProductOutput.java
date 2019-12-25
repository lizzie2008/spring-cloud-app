package tech.lancelot.shoppingcommon.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class ProductOutput {

    @JsonProperty("name")
    private String categoryName;

    @JsonProperty("type")
    private Integer categoryType;

    @JsonProperty("food")
    private List<ProductItemOutput> productItemOutputs;
}
