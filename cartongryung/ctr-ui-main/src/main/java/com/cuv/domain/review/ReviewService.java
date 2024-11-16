package com.cuv.domain.review;

import com.cuv.domain.review.dto.ReviewDetailDto;
import com.cuv.domain.review.dto.ReviewListDto;
import com.cuv.domain.review.dto.ReviewSearchDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewService {
    private final ReviewRepository reviewRepository;


    /**
     * 고객 후기 목록 불러오기
     *
     * @param request 검색 조건을 담은 DTO
     * @author 박성민
     */
    public Page<ReviewListDto> searchAllReviewList(ReviewSearchDto condition, Pageable request) {
        return reviewRepository.searchAllReviewList(condition, request);
    }

    /**
     *  메인에 뿌릴 리뷰 리스트 가져오기
     * @author 박성민
     */
    public List<ReviewListDto> searchAllReviewNopageList() {
        return reviewRepository.searchAllReviewNopageList();
    }




    /**
     * 고객 후기 디테일을 불러오기
     *
     * @param
     * @author 박성민
     */
    public ReviewDetailDto searchReviewDetailByReviewId(Long id){
        return reviewRepository.searchReviewDetailByReviewId(id);
    }
}
