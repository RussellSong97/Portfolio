package com.cuv.web.controller.board;

import com.cuv.common.JSONResponse;
import com.cuv.domain.boardreview.BoardReviewService;
import com.cuv.domain.boardreview.dto.BoardReviewDetailDto;
import com.cuv.domain.boardreview.dto.BoardReviewListDto;
import com.cuv.domain.boardreview.dto.BoardReviewSearchDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import static org.springframework.util.StringUtils.hasText;

@Tag(
        name = "사용자 - 안내 - 고객후기",
        description = "사용자 - 안내 - 고객후기"
)
@RequiredArgsConstructor
@Controller
public class BoardReviewController {

    private final BoardReviewService boardReviewService;

    /**
     *  고객후기 > 목록
     *
     *  @param condition 페이징 정보 저장한 DTO
     *  @author 박성민
     */
    @Operation(
            summary = "안내 > 고객후기 > 목록",
            description = "고객후기 목록"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "HTTP Status Ok"
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "HTTP Status Internal Server Error",
                    content = @Content(
                            schema = @Schema(implementation = JSONResponse.class)
                    )
            )
    })
    @GetMapping("/review")
    public String review(BoardReviewSearchDto condition, Model model){
        int setPage = 1;
        int setSize = 12;

        String page = condition.getPage();
        String size = condition.getSize();

        if (hasText(page) && page.matches("^\\d+$")) setPage = Math.max(Integer.parseInt(page), 1);
        if (hasText(size) && size.matches("^\\d+$")) setSize = Math.max(Integer.parseInt(size), 1);

        PageRequest request = PageRequest.of(setPage - 1, setSize, Sort.by("id").descending());

        Page<BoardReviewListDto> reviewList = boardReviewService.searchAllReviewList(condition, request);

        model.addAttribute("reviewList", reviewList);
        model.addAttribute("condition", condition);

        return "sub/review_list";
    }

    /**
     * 고객 후기 > 상세
     *
     * @param id 고객 후기 시퀀스
     * @author 박성민
     */
    @Operation(
            summary = "안내 > 고객후기 > 상세",
            description = "고객후기 상세"
    )
    @GetMapping("/review/{id}")
    public String reviewDetail(Model model, @PathVariable("id") Long id) {
        BoardReviewDetailDto reviewDetail = boardReviewService.searchReviewDetailByReviewId(id);

        model.addAttribute("reviewDetail", reviewDetail);

        return "sub/review_view";
    }
}
