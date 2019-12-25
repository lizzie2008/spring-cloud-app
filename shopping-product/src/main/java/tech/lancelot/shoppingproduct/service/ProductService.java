package tech.lancelot.shoppingproduct.service;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tech.lancelot.shoppingcommon.dto.ProductItemOutput;
import tech.lancelot.shoppingcommon.dto.ProductOutput;
import tech.lancelot.shoppingproduct.domain.ProductCategory;
import tech.lancelot.shoppingproduct.domain.ProductInfo;

import tech.lancelot.shoppingcommon.enums.ProductStatus;
import tech.lancelot.shoppingproduct.repository.ProductCategoryRepository;
import tech.lancelot.shoppingproduct.repository.ProductInfoRepository;

import java.util.ArrayList;
import java.util.List;

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
     * 获取所有目类下的上架商品
     * @return
     */
    public List<ProductOutput> list() {

        List<ProductOutput> productOutputs = new ArrayList<>();

        //1.找到商品所有类目
        List<ProductCategory> categories = productCategoryRepository.findAll();

        //2.遍历商品，找到类目下所有上架商品
        for (ProductCategory category : categories) {

            //构建类目信息
            ProductOutput productOutput = new ProductOutput();
            BeanUtils.copyProperties(category, productOutput);
            productOutputs.add(productOutput);

            List<ProductInfo> productInfos = productInfoRepository
                    .findByCategoryTypeAndProductStatus(category.getCategoryType(), ProductStatus.ON_SALE);

            List<ProductItemOutput> productItemOutputs = new ArrayList<>();
            for (ProductInfo productInfo : productInfos) {
                //构建类目下商品信息
                ProductItemOutput productItemOutput = new ProductItemOutput();
                BeanUtils.copyProperties(productInfo, productItemOutput);
                productItemOutputs.add(productItemOutput);
            }
            productOutput.setProductItemOutputs(productItemOutputs);
        }
        return productOutputs;
    }
}
