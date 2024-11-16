package com.cuv.domain.productviewshistory;

import com.cuv.domain.productviewshistory.dto.ProductViewsHistoryMyPageRecentCarListDto;
import com.cuv.domain.productviewshistory.dto.ProductViewsHistoryRecentCarListDto;

import java.util.List;

public interface ProductViewsHistoryRepositoryCustom {
    //로그인 상태의 aside 최근본차량
    List<ProductViewsHistoryRecentCarListDto> searchProductViewsHistoryRecentCarList(Long memberId);

    //미 로그인 상태의 aside 최근본차량
    List<ProductViewsHistoryRecentCarListDto> searchProductViewsHistoryRecentCarListByIds(List<Long> ids);

    //마이페이지 최근본차량 페이지 목록
    List<ProductViewsHistoryMyPageRecentCarListDto> searchProductViewsHistoryMyPageRecentCarListByMemberId(Long memberId);

    //더보기 > 최근본차량 카운트
    Long searchProductViewsHistoryMyPageRecentCarCountByMemberId(Long memberId);
}
