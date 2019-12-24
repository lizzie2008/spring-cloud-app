package tech.lancelot.shoppingprovider.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.Assert;
import tech.lancelot.shoppingprovider.domain.product.ProductCategory;
import tech.lancelot.shoppingprovider.repository.product.ProductCategoryRepository;

import java.util.List;

@SpringBootTest
class ProductCategoryRepositoryTest {

    @Autowired
    private ProductCategoryRepository productCategoryRepository;

    @Test
    void findByCategoryTypeIn() {

        List<ProductCategory> productCategories = productCategoryRepository.findAll();
        Assert.notEmpty(productCategories,"error");
    }
}