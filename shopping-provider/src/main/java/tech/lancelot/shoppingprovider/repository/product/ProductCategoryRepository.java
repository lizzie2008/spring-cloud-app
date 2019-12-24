package tech.lancelot.shoppingprovider.repository.product;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tech.lancelot.shoppingprovider.domain.product.ProductCategory;

@Repository
public interface ProductCategoryRepository extends JpaRepository<ProductCategory,String> {

}
