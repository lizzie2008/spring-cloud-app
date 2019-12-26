package tech.lancelot.shoppingproduct.domain;

import lombok.Data;
import tech.lancelot.shoppingcommon.enums.ProductStatus;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.math.BigDecimal;
import java.util.Date;

@Data
@Entity
/*商品信息*/
public class ProductInfo {

    @Id
    private String productId;

    /** 名字. */
    private String productName;

    /** 单价. */
    private BigDecimal productPrice;

    /** 库存. */
    private Integer productStock;

    /** 描述. */
    private String productDescription;

    /** 小图. */
    private String productIcon;

    /** 状态, 0正常1下架. */
    private ProductStatus productStatus;

    /** 类目编号. */
    private Integer categoryId;

    private Date createTime;

    private Date updateTime;
}
