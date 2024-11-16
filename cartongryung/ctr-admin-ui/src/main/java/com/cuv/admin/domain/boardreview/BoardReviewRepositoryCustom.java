package com.cuv.admin.domain.boardreview;

import com.cuv.admin.domain.boardreview.dto.BoardReviewDetailDto;
import com.cuv.admin.domain.boardreview.dto.BoardReviewListDto;
import com.cuv.admin.domain.boardreview.dto.BoardReviewSearchDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BoardReviewRepositoryCustom {

    //     컨텐츠를 가져오는 쿼리와 카운트 쿼리 분리하고 PageableExecutionUtils를 사용하여 응답
    Page<BoardReviewListDto> searchAllReviewList(BoardReviewSearchDto condition, Pageable request);

    BoardReviewDetailDto searchReviewDetailByReviewId(Long reviewId);

}
