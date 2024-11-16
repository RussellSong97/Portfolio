package com.cuv.admin.web.controller.board;

import com.cuv.admin.common.JSONResponse;
import com.cuv.admin.domain.product.ProductService;
import com.cuv.admin.domain.product.dto.ProductListDto;
import com.cuv.admin.domain.product.dto.ProductSearchDto;
import com.cuv.admin.domain.boardreview.BoardReviewService;
import com.cuv.admin.domain.boardreview.dto.BoardReviewDetailDto;
import com.cuv.admin.domain.boardreview.dto.BoardReviewListDto;
import com.cuv.admin.domain.boardreview.dto.BoardReviewSaveDto;
import com.cuv.admin.domain.boardreview.dto.BoardReviewSearchDto;

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
import org.springframework.web.bind.annotation.*;

import static org.springframework.util.StringUtils.hasText;

/**
 * 관리자 | 게시판 > 이용후기
 */
@Tag(
        name = "관리자 -> 게시판 -> 이용후기",
        description = "관리자 -> 게시판 -> 이용후기 경로"
)
@RequiredArgsConstructor
@Controller
public class BoardReviewController {

    private final BoardReviewService boardReviewService;
    private final ProductService productService;

    /**
     * 관리자 | 게시판 > 리뷰 > 목록
     *
     * @param condition 페이징 정보 저장한 DTO
     * @author 박성민
     *
     */
    @Operation(
            summary = "관리자 | 게시판 > 이용후기 > 이용후기 리스트",
            description = "이용후기 리스트"
    )
    @GetMapping("/admin/board/review")
    public String adminBoardReview(BoardReviewSearchDto condition, Model model) {
        int setPage = 1;
        int setSize = 50;

        String page = condition.getPage();
        String size = condition.getSize();

        if (hasText(page) && page.matches("^\\d+$")) setPage = Math.max(Integer.parseInt(page), 1);
        if (hasText(size) && size.matches("^\\d+$")) setSize = Math.max(Integer.parseInt(size), 1);

        PageRequest request = PageRequest.of(setPage - 1, setSize, Sort.by("id").descending());

        Page<BoardReviewListDto> reviewList = boardReviewService.searchAllReviewList(condition, request);

        model.addAttribute("reviewList", reviewList);
        model.addAttribute("condition", condition);

        return "board/review_list";
    }

    /**
     * 관리자 | 게시판 > 리뷰 > 상세보기
     *
     * @author 박성민
     */
    @Operation(
            summary = "관리자 | 게시판 > 이용후기 > 이용후기 상세보기",
            description = "등록한 이용후기 상세보기"
    )
    @GetMapping("/admin/board/review/{id}")
    public String adminBoardReviewDetail(Model model, @PathVariable("id") Long id) {
        BoardReviewDetailDto reviewDetail = boardReviewService.searchReviewDetailByReviewId(id);

        model.addAttribute("reviewDetail", reviewDetail);

        return "board/review_view";
    }


    /**
     * 관리자 | 게시판 > 리뷰 > 작성 폼
     *
     * @author 박성민
     */
    @Operation(
            summary = "관리자 | 게시판 > 이용후기 > 이용후기 작성 폼",
            description = "등록한 이용후기 작성 폼"
    )
    @GetMapping("/admin/board/review/write")
    public String adminReviewWriteForm() {
        return "board/review_write";
    }

    /**
     * 관리자 | 게시판 > 리뷰 > 작성
     *
     * @author 박성민
     */
    @Operation(
            summary = "관리자 | 게시판 > 이용후기 > 이용후기 작성",
            description = "이용후기 작성하기"
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
    @PostMapping("/admin/board/review/write")
    @ResponseBody
    public JSONResponse<?> adminReviewWriteProc(BoardReviewSaveDto requestDto) {
        try {
            boardReviewService.adminReviewWriteProc(requestDto);
        } catch (Exception e) {
            return new JSONResponse<>(500, e.getMessage());
        }
        return new JSONResponse<>(200, "SUCCESS");
    }

    /**
     * 관리자 | 게시판 > 리뷰 > 작성 폼 > 차량 조회 팝업
     * @author 박성민
     */
    @Operation(
            summary = "관리자 | 게시판 > 이용후기 > 이용후기 작성 > 차량 조회 팝업",
            description = "이용후기 작성하기 폼에 있는 차량 조회기능"
    )
    @GetMapping("/admin/board/review/write/findCar")
    public String adminReviewFindCarForm(ProductSearchDto condition, Model model) {
        int setPage = 1;
        int setSize = 50;

        String page = condition.getPage();
        String size = condition.getSize();
        if (hasText(page) && page.matches("^\\d+$")) setPage = Math.max(Integer.parseInt(page), 1);
        if (hasText(size) && size.matches("^\\d+$")) setSize = Math.max(Integer.parseInt(size), 1);
        PageRequest request = PageRequest.of(setPage - 1, setSize);
        Page<ProductListDto> productLists = productService.searchAllPostingProductLists(condition, request);

        model.addAttribute("productLists", productLists);

        return "popup/vehicle_search";
    }

    /**
     * 관리자 | 게시판 > 리뷰 > 상세 > 수정
     * @author 박성민
     */

    @Operation(
            summary = "관리자 | 게시판 > 이용후기 > 이용후기 수정",
            description = "이용후기 수정하기"
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
    @PostMapping("/admin/board/review/edit")
    @ResponseBody
    public JSONResponse<?> adminReviewEditProc(BoardReviewSaveDto requestDto) {

        try {
            boardReviewService.adminReviewEditProc(requestDto);
        } catch (Exception e) {
            return new JSONResponse<>(500, e.getMessage());
        }
        return new JSONResponse<>(200, "SUCCESS", requestDto);
    }



    /**
     * 관리자 | 게시판 > 리뷰 > 상세 > 삭제
     *
     * @author 박성민
     */
    @Operation(
            summary = "관리자 | 게시판 > 이용후기 > 이용후기 삭제",
            description = "이용후기 삭제하기"
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
    @PostMapping ("/admin/review/delete/{id}")
    @ResponseBody
    public JSONResponse<?> adminReviewDeleteProc( @PathVariable("id") Long id) {

        try {
            boardReviewService.adminReviewDeleteProc(id);
        } catch (Exception e) {
            return new JSONResponse<>(500, e.getMessage());
        }
        return new JSONResponse<>(200, "SUCCESS", id);
    }

}
