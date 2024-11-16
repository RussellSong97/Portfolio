package com.cuv.admin.domain.product;

import com.cuv.admin.domain.product.dto.ProductListDto;
import com.cuv.admin.domain.product.dto.ProductSearchDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ProductRepositoryCustom {

    Page<ProductListDto> searchAllPostingProductLists(ProductSearchDto condition, Pageable pageable);

    Page<ProductListDto> searchAllProduct(ProductSearchDto condition, Pageable pageable);

    List<ProductListDto> searchAllProduct(ProductSearchDto condition);

    Long searchAllSoldOutProductCountByMain();

    Long searchAllSalesByMain();

    Long searchAllProductCountByMain();

    List<ProductListDto> searchAllPostingProductExcelLists(ProductSearchDto condition);

    List<ProductListDto> searchRecommendProductExcelList(ProductSearchDto condition);

}
