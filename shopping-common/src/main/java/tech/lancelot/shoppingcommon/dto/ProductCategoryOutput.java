package tech.lancelot.shoppingcommon.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class ProductCategoryOutput {

    @JsonProperty("id")
    private Integer categoryId;

    @JsonProperty("name")
    private String categoryName;

}
