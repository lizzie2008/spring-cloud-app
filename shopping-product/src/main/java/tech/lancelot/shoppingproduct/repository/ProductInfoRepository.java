package tech.lancelot.shoppingproduct.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tech.lancelot.shoppingproduct.domain.ProductInfo;
import tech.lancelot.shoppingcommon.enums.ProductStatus;

import java.util.List;

@Repository
public interface ProductInfoRepository extends JpaRepository<ProductInfo, String> {

    //根据商品类目和商品状态查询
    List<ProductInfo> findByCategoryTypeAndProductStatus(Integer categoryType, ProductStatus productStatus);
}
