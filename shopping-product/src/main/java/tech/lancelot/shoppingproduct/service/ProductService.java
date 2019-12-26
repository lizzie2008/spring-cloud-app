package tech.lancelot.shoppingproduct.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tech.lancelot.shoppingcommon.dto.ProductCategoryOutput;
import tech.lancelot.shoppingcommon.dto.ProductInfoOutput;
import tech.lancelot.shoppingproduct.domain.ProductCategory;
import tech.lancelot.shoppingproduct.domain.ProductInfo;

import tech.lancelot.shoppingcommon.enums.ProductStatus;
import tech.lancelot.shoppingproduct.repository.ProductCategoryRepository;
import tech.lancelot.shoppingproduct.repository.ProductInfoRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.springframework.beans.BeanUtils.*;

@Service
public class ProductService {

    private final ProductInfoRepository productInfoRepository;
    private final ProductCategoryRepository productCategoryRepository;

    @Autowired
    public ProductService(ProductInfoRepository productInfoRepository,
                          ProductCategoryRepository productCategoryRepository) {
        this.productInfoRepository = productInfoRepository;
        this.productCategoryRepository = productCategoryRepository;
    }

    /**
     * 获取所有商品类目
     *
     * @return
     */
    public List<ProductCategory> findCategories() {
        return productCategoryRepository.findAll();
    }

    /**
     * 获取所有商品类目(根据id获取)
     *
     * @return
     */
    public Optional<ProductCategory> findCategoriesById(Integer id) {
        return productCategoryRepository.findById(id);
    }

    /**
     * 获取所有上架商品信息
     *
     * @return
     */
    public List<ProductInfo> findProductInfos() {
        return productInfoRepository.findByProductStatus(ProductStatus.ON_SALE);
    }

    /**
     * 获取某个类目下的所有上架商品
     * @param id
     * @return
     */
    public List<ProductInfo> findAllProductInfosByCategory(Integer id) {
        return productInfoRepository.findByCategoryIdAndProductStatus(id,ProductStatus.ON_SALE);
    }

    /**
     * 获取某些Id的上架商品
     *
     * @param productIds
     * @return
     */
    public List<ProductInfo> findProductInfosByIds(List<String> productIds) {
        return productInfoRepository.findByProductIdInAndProductStatus(productIds, ProductStatus.ON_SALE);
    }



}
