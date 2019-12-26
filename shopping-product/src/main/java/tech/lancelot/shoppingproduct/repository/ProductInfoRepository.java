package tech.lancelot.shoppingproduct.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tech.lancelot.shoppingproduct.domain.ProductInfo;
import tech.lancelot.shoppingcommon.enums.ProductStatus;

import java.util.List;

@Repository
public interface ProductInfoRepository extends JpaRepository<ProductInfo, String> {

    //根据商品状态查询所有商品
    List<ProductInfo> findByProductStatus(ProductStatus productStatus);

    //根据商品Id和商品状态查询
    List<ProductInfo> findByProductIdInAndProductStatus(Iterable<String> productIds, ProductStatus productStatus);

    //根据类别Id和商品状态查询
    List<ProductInfo> findByCategoryIdAndProductStatus(Integer categoryId, ProductStatus productStatus);
}
