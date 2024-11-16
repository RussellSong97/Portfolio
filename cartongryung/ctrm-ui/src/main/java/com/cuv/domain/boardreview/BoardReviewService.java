package com.cuv.domain.boardreview;

import com.cuv.domain.boardreview.dto.BoardReviewDetailDto;
import com.cuv.domain.boardreview.dto.BoardReviewListDto;
import com.cuv.domain.boardreview.dto.BoardReviewSearchDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 이용후기 서비스 (이용후기 조회)
 */
@Service
@RequiredArgsConstructor
public class BoardReviewService {
    private final BoardReviewRepository reviewRepository;


    /**
     * 고객 후기 목록 불러오기
     *
     * @param request 검색 조건을 담은 DTO
     * @author 박성민
     */
    public Page<BoardReviewListDto> searchAllReviewList(BoardReviewSearchDto condition, Pageable request) {
        return reviewRepository.searchAllReviewList(condition, request);
    }

    /**
     *  메인에 뿌릴 리뷰 리스트 가져오기
     * @author 박성민
     */
    public List<BoardReviewListDto> searchAllReviewNopageList() {
        return reviewRepository.searchAllReviewNopageList();
    }

    /**
     * 고객 후기 > 상세
     *
     * @param id 고객 후기 시퀀스
     * @author 박성민
     */
    public BoardReviewDetailDto searchReviewDetailByReviewId(Long id){
        return reviewRepository.searchReviewDetailByReviewId(id);
    }

}
