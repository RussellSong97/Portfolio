package com.cuv.web.controller.review;

import com.cuv.domain.review.ReviewService;
import com.cuv.domain.review.dto.ReviewDetailDto;
import com.cuv.domain.review.dto.ReviewListDto;
import com.cuv.domain.review.dto.ReviewSearchDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

import static org.springframework.util.StringUtils.hasText;

@RequiredArgsConstructor
@Controller
public class ReviewController {

    private final ReviewService reviewService;

    /**
     * 모든 리뷰 select
     *  @author 박성민
     *  @param condition 페이징 정보 저장한 DTO
     */
    @GetMapping("/review")
    public String selectAllReview(ReviewSearchDto condition, Model model){

        int setPage = 1;
        int setSize = 12;

        String page = condition.getPage();
        String size = condition.getSize();

        if (hasText(page) && page.matches("^\\d+$")) setPage = Math.max(Integer.parseInt(page), 1);
        if (hasText(size) && size.matches("^\\d+$")) setSize = Math.max(Integer.parseInt(size), 1);

        PageRequest request = PageRequest.of(setPage - 1, setSize, Sort.by("id").descending());

        Page<ReviewListDto> reviewList = reviewService.searchAllReviewList(condition, request);

        model.addAttribute("reviewList", reviewList);
        model.addAttribute("condition", condition);

        return "sub/review_list";
    }

    /**
     * 리뷰 > 상세
     *
     *  @author 박성민
     */
    @GetMapping("/review/{id}")
    public String selectReview(Model model, @PathVariable("id") Long id) {
        ReviewDetailDto reviewDetail = reviewService.searchReviewDetailByReviewId(id);

        model.addAttribute("reviewDetail", reviewDetail);

        return "sub/review_view";
    }
}
