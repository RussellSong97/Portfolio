package com.cuv.domain.product;

import com.cuv.domain.product.dto.*;
import com.cuv.domain.product.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;
import java.util.Map;

public interface ProductRepositoryCustom {

    Page<ProductRecommendListDto> searchProductRecommendList(PageRequest request);

    ProductDetailDto searchProductDetail(Long id);

    List<ProductRecommendListDto> searchProductRecommendNoPageList();

    List<SpecGroupDto> searchProductLinkSpecList(Long productDetailGradeNumber);

    List<SpecGroupDto> searchProductLinkOptionList(Long productDetailGradeNumber);

    List<ProductRecentCarListDto> searchProductRecentCarList();

    List<ProductCuvRecommendListDto> searchProductCuvRecommendList();

    List<ProductBestValueRecommendListDto> searchProductBestValueRecommendList();

    Long searchProductCount();

    List<ProductListDto> searchProductTotalSearchList(ProductTotalSearchDto condition);

    List<ProductListDto> searchApiProduct(Map<String, Object> map);

    Long searchPickCount(Long id);

    Long searchMobileProductCount(Map<String, Object> map);

    Long countByProductId();

    List<Long> searchExtShape(String engName);
}
