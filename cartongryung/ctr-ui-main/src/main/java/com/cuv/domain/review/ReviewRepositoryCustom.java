package com.cuv.domain.review;

import com.cuv.domain.review.dto.ReviewDetailDto;
import com.cuv.domain.review.dto.ReviewListDto;
import com.cuv.domain.review.dto.ReviewSearchDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ReviewRepositoryCustom {



//     컨텐츠를 가져오는 쿼리와 카운트 쿼리 분리하고 PageableExecutionUtils를 사용하여 응답
    Page<ReviewListDto> searchAllReviewList(ReviewSearchDto condition, Pageable request);

    List<ReviewListDto> searchAllReviewNopageList();

    // Todo 디테일
    ReviewDetailDto searchReviewDetailByReviewId(Long id);


}
