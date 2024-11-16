package com.cuv.domain.product;

import com.cuv.domain.product.dto.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;

public interface ProductRepositoryCustom {

    Page<ProductRecommendListDto> searchProductRecommendList(PageRequest request);

    ProductDetailDto searchProductDetail(Long id);

    List<ProductRecommendListDto> searchProductRecommendNoPageList();

    List<SpecGroupDto> searchProductLinkSpecList(Long productDetailGradeNumber);

    List<SpecGroupDto> searchProductLinkOptionList(Long productDetailGradeNumber);

    List<ProductRecentCarListDto> searchProductRecentCarList();

    List<ProductHitCarListDto> searchProductHitCarList();

    List<ProductCuvRecommendListDto> searchProductCuvRecommendList();

    List<ProductBestValueRecommendListDto> searchProductBestValueRecommendList();

    Long searchProductCount();

    Page<ProductListDto> searchProductTotalSearchList(ProductTotalSearchDto condition, Pageable request);

    Page<ProductListDto> searchApiProduct(Map<String, String> map);

    Long searchPickCount(Long id);

    List<Long> searchExtShape(String engName);

    List<ProductRecommendListDto> searchApiRecommendProduct(Map<String, Object> map);

    List<Long> searchCarSize(String engName);
}
