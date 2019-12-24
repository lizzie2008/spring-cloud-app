package tech.lancelot.shoppingprovider.repository.product;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tech.lancelot.shoppingprovider.domain.product.ProductInfo;
import tech.lancelot.shoppingprovider.enums.ProductStatusType;

import java.util.List;

@Repository
public interface ProductInfoRepository extends JpaRepository<ProductInfo, String> {

    //根据商品类目和商品状态查询
    List<ProductInfo> findByCategoryTypeAndProductStatus(Integer categoryType, ProductStatusType productStatus);
}
