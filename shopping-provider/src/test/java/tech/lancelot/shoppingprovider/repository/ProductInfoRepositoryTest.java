package tech.lancelot.shoppingprovider.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.Assert;
import tech.lancelot.shoppingprovider.domain.product.ProductInfo;
import tech.lancelot.shoppingprovider.enums.ProductStatusType;
import tech.lancelot.shoppingprovider.repository.product.ProductInfoRepository;

import java.util.List;

@SpringBootTest
class ProductInfoRepositoryTest {
    @Autowired
    private ProductInfoRepository productInfoRepository;

    @Test
    void findByProductStatus() {
        List<ProductInfo> products = productInfoRepository
                .findByCategoryTypeAndProductStatus(11, ProductStatusType.ON_SALE);
        Assert.notEmpty(products, "error");
    }
}