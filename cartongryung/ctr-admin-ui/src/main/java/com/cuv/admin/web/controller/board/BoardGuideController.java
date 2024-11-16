package com.cuv.admin.web.controller.board;

import com.cuv.admin.common.JSONResponse;
import com.cuv.admin.domain.boardguide.BoardGuideService;
import com.cuv.admin.domain.boardguide.dto.BoardGuideDetailDto;
import com.cuv.admin.domain.boardguide.dto.BoardGuideListDto;
import com.cuv.admin.domain.boardguide.dto.BoardGuideSaveDto;
import com.cuv.admin.domain.boardguide.dto.BoardGuideSearchDto;
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
 * 관리자 | 게시판 > 중고차 가이드
 */
@Tag(
        name = "관리자 -> 게시판 -> 중고차 가이드",
        description = "관리자 -> 게시판 -> 중고차 가이드"
)
@Controller
@RequiredArgsConstructor
public class BoardGuideController {

    private final BoardGuideService boardGuideService;

    /**
     * 관리자 | 게시판 > 중고차 가이드 > 작성 폼
     *
     * @author SungHa
     */
    @Operation(
            summary = "관리자 | 게시판 > 중고차 가이드 > 작성 폼",
            description = "관리자 중고차 가이드 작성 폼"
    )
    @GetMapping("/admin/board/guide/write")
    private String adminBoardGuideWrite() {
        return "board/guide_write";
    }

    /**
     * 관리자 | 게시판 > 중고차 가이드 > 글 등록
     *
     * @param requestDto 등록할 글 정보를 담은 DTO
     * @author SungHa
     */
    @Operation(
            summary = "관리자 | 게시판 > 중고차 가이드 > 등록",
            description = "관리자 중고차 가이드 등록"
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
    @PostMapping("/admin/board/guide/write")
    @ResponseBody
    public JSONResponse<?> adminBoardGuideWriteProc(BoardGuideSaveDto requestDto) {
        try {
            boardGuideService.adminBoardGuideWriteProc(requestDto);
        } catch (Exception e) {
            return new JSONResponse<>(500, e.getMessage());
        }
        return new JSONResponse<>(200, "SUCCESS", requestDto);
    }

    /**
     * 관리자 | 게시판 > 중고차 가이드 > 목록
     *
     * @param condition 검색 조건을 담은 DTO
     * @author SungHa
     */
    @Operation(
            summary = "관리자 | 게시판 > 중고차 가이드 > 목록",
            description = "관리자 중고차 가이드 목록"
    )
    @GetMapping("/admin/board/guide")
    public String adminBoardGuideList(BoardGuideSearchDto condition, Model model) {
        int setPage = 1;
        int setSize = 50;

        String page = condition.getPage();
        String size = condition.getSize();
        if (hasText(page) && page.matches("^\\d+$")) setPage = Math.max(Integer.parseInt(page), 1);
        if (hasText(size) && size.matches("^\\d+$")) setSize = Math.max(Integer.parseInt(size), 1);
        PageRequest request = PageRequest.of(setPage - 1, setSize, Sort.by("id").descending());

        Page<BoardGuideListDto> boardGuideLists = boardGuideService.searchAllBoardGuide(condition, request);

        model.addAttribute("condition", condition);
        model.addAttribute("boardGuideLists", boardGuideLists);

        return "board/guide_list";
    }

    /**
     * 관리자 | 게시판 > 중고차 가이드 > 상세
     *
     * @param id 글 시퀀스
     * @author SungHa
     */
    @Operation(
            summary = "관리자 | 게시판 > 중고차 가이드 > 상세",
            description = "관리자 중고차 가이드 상세"
    )
    @GetMapping("/admin/board/guide/{id}")
    public String adminBoardGuideDetail(@PathVariable("id") Long id, Model model) {
        BoardGuideDetailDto boardGuide = boardGuideService.searchBoardGuideById(id);

        model.addAttribute("boardGuide", boardGuide);

        return "board/guide_view";
    }

    /**
     * 관리자 | 게시판 > 중고차 가이드 > 수정
     *
     * @param requestDto 등록할 글 정보를 담은 DTO
     * @author SungHa
     */
    @Operation(
            summary = "관리자 | 게시판 > 중고차 가이드 > 수정",
            description = "관리자 중고차 가이드 수정"
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
    @PostMapping("/admin/board/guide/edit")
    @ResponseBody
    public JSONResponse<?> adminBoardGuideEditProc(BoardGuideSaveDto requestDto) {
        try {
            boardGuideService.adminBoardGuideEditProc(requestDto);
        } catch (Exception e) {
            return new JSONResponse<>(500, e.getMessage());
        }
        return new JSONResponse<>(200, "SUCCESS", requestDto);
    }

    /**
     * 관리자 | 게시판 > 중고차 가이드 > 삭제
     *
     * @param id 글 시퀀스
     * @author SungHa
     */
    @Operation(
            summary = "관리자 | 게시판 > 중고차 가이드 > 삭제",
            description = "관리자 중고차 가이드 삭제"
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
    @DeleteMapping("/admin/board/guide/{id}")
    @ResponseBody
    public JSONResponse<?> adminBoardGuideDeleteProc(@PathVariable("id") Long id) {
        try {
            boardGuideService.adminBoardGuideDeleteProc(id);
        } catch (Exception e) {
            return new JSONResponse<>(500, e.getMessage());
        }
        return new JSONResponse<>(200, "SUCCESS", id);
    }
}
