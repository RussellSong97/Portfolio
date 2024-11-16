package com.cuv.admin.web.controller.board;

import com.cuv.admin.common.JSONResponse;
import com.cuv.admin.domain.boardfaq.BoardFaqService;
import com.cuv.admin.domain.boardfaq.dto.BoardFaqDetailDto;
import com.cuv.admin.domain.boardfaq.dto.BoardFaqListDto;
import com.cuv.admin.domain.boardfaq.dto.BoardFaqSearchDto;
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

import java.util.Locale;
import java.util.Map;

import static org.springframework.util.StringUtils.hasText;

/**
 * 관리자 | 게시판 > 자주 묻는 질문
 */
@Tag(
        name = "관리자 -> 게시판 -> 자주 묻는 질문",
        description = "관리자 -> 게시판 -> 자주 묻는 질문"
)
@Controller
@RequiredArgsConstructor
public class BoardFaqController {
    private final BoardFaqService boardFaqService;

    /**
     * 관리자 | 게시판 > 자주 묻는 질문 목록
     *
     * @param condition 검색 조건을 담은 DTO
     * @author 송다운
     */
    @Operation(
            summary = "관리자 | 게시판 > 자주 묻는 질문 목록",
            description = "관리자 게시판 자주 묻는 질문 목록"
    )
    @GetMapping("/admin/board/faq")
    public String adminBoardFaq(BoardFaqSearchDto condition, Model model) {
        int setPage = 1;
        int setSize = 50;

        String page = condition.getPage();
        String size = condition.getSize();

        if(hasText(page) && page.matches("^\\d+$")) setPage = Math.max(Integer.parseInt(page), 1);
        if(hasText(size) && size.matches("^\\d+$")) setSize = Math.max(Integer.parseInt(size), 1);

        PageRequest request = PageRequest.of(setPage - 1, setSize, Sort.by("id").descending());

        Page<BoardFaqListDto> list = boardFaqService.searchAllBoardFaq(condition, request);
        model.addAttribute("faqList", list);
        model.addAttribute("condition", condition);
        return "board/faq_list";
    }


    /**
     * 관리자 | 게시판 > 자주 묻는 질문 작성 폼
     *
     * @author 송다운
     */
    @Operation(
            summary = "관리자 | 게시판 > 자주 묻는 질문 작성 폼",
            description = "관리자 게시판 자주 묻는 질문 작성 폼"
    )
    @GetMapping("/admin/board/faq/write")
    public String adminBoardFaqWrite() {
        return "board/faq_write";
    }


    /**
     * 관리자 | 게시판 > 자주 묻는 질문 상세
     *
     * @param id 글 시퀀스
     * @author 송다운
     */
    @Operation(
            summary = "관리자 | 게시판 > 자주 묻는 질문 상세",
            description = "관리자 게시판 자주 묻는 질문 상세"
    )
    @GetMapping("/admin/board/faq/{id}")
    public String adminBoardFaqDetail(@PathVariable("id") Long id, Model model) {
        BoardFaqDetailDto faqDetail = boardFaqService.searchBoardFaqById(id);

        model.addAttribute("faqDetail", faqDetail);
        return "board/faq_view";
    }


    /**
     * 관리자 | 게시판 > 자주 묻는 질문 삭제
     *
     * @param map 글 정보를 담은 map
     * @author 송다운
     */
    @Operation(
            summary = "관리자 | 게시판 > 자주 묻는 질문 삭제",
            description = "관리자 게시판 자주 묻는 질문 삭제"
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
    @PostMapping("/admin/board/faq/delete")
    @ResponseBody
    public JSONResponse<?> adminBoardFaqDeleteProc(@RequestBody Map<String, Object> map) {
        JSONResponse<?> response;
        try {
            response = boardFaqService.adminBoardFaqDeleteProc(map);
        } catch (Exception e) {
            return new JSONResponse<>(500, e.getMessage());
        }
        return response;
    }


    /**
     * 관리자 | 게시판 > 자주 묻는 질문 수정
     *
     * @param map 글 정보를 담은 map
     * @author 송다운
     */
    @Operation(
            summary = "관리자 | 게시판 > 자주 묻는 질문 수정",
            description = "관리자 게시판 자주 묻는 질문 수정"
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
    @PostMapping("/admin/board/faq/update")
    @ResponseBody
    public JSONResponse<?> adminBoardFaqUpdate(@RequestBody Map<String, Object> map) {
        JSONResponse<?> response;

        try {
            response = boardFaqService.adminBoardFaqUpdate(map);
        } catch (Exception e) {
            return new JSONResponse<>(500, e.getMessage());
        }
        return response;
    }


    /**
     * 관리자 | 게시판 > 자주 묻는 질문 > 저장
     *
     * @param map 글 정보를 담은 map
     * @author 송다운
     */
    @Operation(
            summary = "관리자 | 게시판 > 자주 묻는 질문 > 저장",
            description = "관리자 자주 묻는 질문 저장"
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
    @PostMapping("/admin/board/faq/save")
    @ResponseBody
    public JSONResponse<?> adminBoardFaqSave(@RequestBody Map<String, Object> map) {
        JSONResponse<?> response;
        try {
            response = boardFaqService.adminBoardFaqSave(map);
        } catch (Exception e) {
            return new JSONResponse<>(500, e.getMessage());
        }
        return response;
    }
}
