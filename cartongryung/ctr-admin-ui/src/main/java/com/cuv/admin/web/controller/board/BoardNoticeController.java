package com.cuv.admin.web.controller.board;

import com.cuv.admin.common.JSONResponse;
import com.cuv.admin.domain.boardnotice.BoardNoticeService;
import com.cuv.admin.domain.boardnotice.dto.BoardNoticeDetailDto;
import com.cuv.admin.domain.boardnotice.dto.BoardNoticeListDto;
import com.cuv.admin.domain.boardnotice.dto.BoardNoticeSaveDto;
import com.cuv.admin.domain.boardnotice.dto.BoardNoticeSearchDto;
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

import java.util.Map;

import static org.springframework.util.StringUtils.hasText;

/**
 * 관리자 | 게시판 > 공지사항
 */
@Tag(
        name = "관리자 -> 게시판 -> 공지사항",
        description = "관리자 -> 게시판 -> 공지사항"
)
@Controller
@RequiredArgsConstructor
public class BoardNoticeController {

    private final BoardNoticeService boardNoticeService;

    /**
     * 관리자 | 게시판 > 공지사항 > 목록
     *
     * @param condition 검색 조건을 담은 DTO
     * @author 송다운
     */
    @Operation(
            summary = "관리자 | 게시판 > 공지사항 > 목록",
            description = "관리자 게시판 공지사항 목록"
    )
    @GetMapping("/admin/board/notice")
    public String adminBoardNotice(BoardNoticeSearchDto condition, Model model) {
        int setPage = 1;
        int setSize = 50;

        String page = condition.getPage();
        String size = condition.getSize();

        if (hasText(page) && page.matches("^\\d+$")) setPage = Math.max(Integer.parseInt(page), 1);
        if (hasText(size) && size.matches("^\\d+$")) setSize = Math.max(Integer.parseInt(size), 1);

        PageRequest request = PageRequest.of(setPage - 1, setSize, Sort.by("id").descending());

        Page<BoardNoticeListDto> boardNoticeList = boardNoticeService.searchAllBoardNotice(condition, request);

        model.addAttribute("boardNoticeList", boardNoticeList);
        model.addAttribute("condition", condition);

        return "board/notice_list";
    }

    /**
     * 관리자 | 게시판 > 공지사항 > 작성 폼
     *
     * @author 송다운
     */
    @Operation(
            summary = "관리자 | 게시판 > 공지사항 > 작성 폼",
            description = "관리자 게시판 공지사항 작성 폼"
    )
    @GetMapping("/admin/board/notice/write")
    public String adminBoardNoticeWrite() {
        return "board/notice_write";
    }

    /**
     * 관리자 | 게시판 > 공지사항 > 글 등록
     *
     * @param requestDto 등록할 글 정보를 담은 DTO
     * @author 송다운
     */
    @Operation(
            summary = "관리자 | 게시판 > 공지사항 > 글 등록",
            description = "관리자 게시판 공지사항 글 등록"
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
    @PostMapping("/admin/board/notice/write")
    @ResponseBody
    public JSONResponse<?> adminBoardNoticeWriteProc(BoardNoticeSaveDto requestDto) {
        JSONResponse<?> response;
        try {
            response = boardNoticeService.adminBoardNoticeWriteProc(requestDto);
        } catch (Exception e) {
            return new JSONResponse<>(500, e.getMessage());
        }
        return response;
    }

    /**
     * 관리자 | 게시판 > 공지사항 > 상세/수정 폼
     *
     * @param id 글 시퀀스
     * @author 송다운
     */
    @Operation(
            summary = "관리자 | 게시판 > 공지사항 > 상세/수정 폼",
            description = "관리자 게시판 공지사항 상세/수정 폼"
    )
    @GetMapping("/admin/board/notice/{id}")
    public String adminBoardNoticeDetail(@PathVariable("id") Long id, Model model) {
        BoardNoticeDetailDto detail = boardNoticeService.searchBoardNoticeById(id);
        model.addAttribute("boardNotice", detail);
        return "board/notice_view";
    }

    /**
     * 관리자 | 게시판 > 공지사항 > 삭제
     *
     * @param map 글 시퀀스를 담은 map
     * @author 송다운
     */
    @Operation(
            summary = "관리자 | 게시판 > 공지사항 > 삭제",
            description = "관리자 게시판 공지사항 삭제"
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
    @PostMapping("/admin/board/delete")
    @ResponseBody
    public JSONResponse<?> adminBoardDeleteProc(@RequestBody Map<String, Object> map) {
        JSONResponse<?> response;
        try {
            response = boardNoticeService.adminBoardDeleteProc(map);
        } catch (Exception e) {
            return new JSONResponse<>(500, e.getMessage());
        }

        return response;
    }

    /**
     * 관리자 | 게시판 > 공지사항 > 수정
     *
     * @param requestDto 등록할 글 정보를 담은 DTO
     * @author 송다운
     */
    @Operation(
            summary = "관리자 | 게시판 > 공지사항 > 수정",
            description = "관리자 게시판 공지사항 수정"
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
    @PostMapping("/admin/board/notice/edit")
    @ResponseBody
    public JSONResponse<?> adminBoardNoticeEditProc(BoardNoticeSaveDto requestDto) {
        JSONResponse<?> response;
        try {
            response = boardNoticeService.adminBoardNoticeEditProc(requestDto);
        } catch (Exception e) {
            return new JSONResponse<>(500, e.getMessage());
        }
        return response;
    }
}
