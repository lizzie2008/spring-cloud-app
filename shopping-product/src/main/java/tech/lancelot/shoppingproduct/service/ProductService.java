package tech.lancelot.shoppingproduct.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.lancelot.shoppingcommon.dto.OrderItemInput;
import tech.lancelot.shoppingcommon.dto.StockApplyInput;
import tech.lancelot.shoppingcommon.dto.StockResultOutput;
import tech.lancelot.shoppingcommon.enums.ProductStatus;
import tech.lancelot.shoppingproduct.domain.ProductCategory;
import tech.lancelot.shoppingproduct.domain.ProductInfo;
import tech.lancelot.shoppingproduct.repository.ProductCategoryRepository;
import tech.lancelot.shoppingproduct.repository.ProductInfoRepository;
import tech.lancelot.shoppingproduct.stream.ProductStream;

import java.util.List;
import java.util.Optional;


@Service
@Slf4j
@EnableBinding(ProductStream.class)
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
    public ProductCategory findCategoriesById(Integer id) throws Exception {
        Optional<ProductCategory> productCategoryOptional = productCategoryRepository.findById(id);
        if (productCategoryOptional.isPresent())
            return productCategoryOptional.get();
        throw new Exception("商品不存在.");
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
     *
     * @param id
     * @return
     */
    public List<ProductInfo> findAllProductInfosByCategory(Integer id) {
        return productInfoRepository.findByCategoryIdAndProductStatus(id, ProductStatus.ON_SALE);
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

    /**
     * 扣减库存
     *
     */
    @Transactional
    @StreamListener(ProductStream.STOCK_APPLY_INPUT)
    @SendTo(ProductStream.STOCK_RESULT_OUTPUT)
    public StockResultOutput processStockApply(StockApplyInput stockApplyInput) throws Exception {

        log.info("占用库存消息被消费...");
        StockResultOutput stockResultOutput = new StockResultOutput();
        stockResultOutput.setOrderId(stockApplyInput.getOrderId());

        try {
            for (OrderItemInput orderItemInput : stockApplyInput.getOrderItemInputs()) {

                Optional<ProductInfo> productInfoOptional = productInfoRepository.findById(orderItemInput.getProductId());
                if (!productInfoOptional.isPresent())
                    throw new Exception("商品不存在.");

                ProductInfo productInfo = productInfoOptional.get();
                int result = productInfo.getProductStock() - orderItemInput.getProductQuantity();
                if (result < 0)
                    throw new Exception("商品库存不满足.");

                productInfo.setProductStock(result);
                productInfoRepository.save(productInfo);
            }

            stockResultOutput.setIsSuccess(true);
            stockResultOutput.setMessage("OK");
            return stockResultOutput;
        } catch (Exception e) {
            stockResultOutput.setIsSuccess(false);
            stockResultOutput.setMessage(e.getMessage());
            return stockResultOutput;
        }

    }

}
