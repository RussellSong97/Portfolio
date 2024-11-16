package com.cuv.domain.boardreview;

import com.cuv.domain.boardreview.dto.BoardReviewDetailDto;
import com.cuv.domain.boardreview.dto.BoardReviewListDto;
import com.cuv.domain.boardreview.dto.BoardReviewSearchDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface BoardReviewRepositoryCustom {



//     컨텐츠를 가져오는 쿼리와 카운트 쿼리 분리하고 PageableExecutionUtils를 사용하여 응답
    Page<BoardReviewListDto> searchAllReviewList(BoardReviewSearchDto condition, Pageable request);

    List<BoardReviewListDto> searchAllReviewNopageList();

    // Todo 디테일
    BoardReviewDetailDto searchReviewDetailByReviewId(Long id);


}
