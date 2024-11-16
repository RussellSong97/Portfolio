package com.cuv.web.controller.board;

import com.cuv.domain.boardnotice.BoardNoticeService;
import com.cuv.domain.boardnotice.dto.BoardNoticeDetailDto;
import com.cuv.domain.boardnotice.dto.BoardNoticeListDto;
import com.cuv.domain.boardnotice.dto.BoardNoticeSearchDto;
import io.swagger.v3.oas.annotations.Operation;
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
        name = "사용자 - 안내 - 공지사항",
        description = "사용자 - 안내 - 공지사항"
)
@Controller
@RequiredArgsConstructor
public class BoardNoticeController {

    private final BoardNoticeService boardNoticeService;

    /**
     * 안내 > 공지사황 > 목록
     *
     * @param condition 검색 조건을 담은 DTO
     * @author SungHa
     */
    @Operation(
            summary = "안내 > 공지사항 > 목록",
            description = "공지사항 목록"
    )
    @GetMapping("/board/notice")
    public String adminBoardNotice(BoardNoticeSearchDto condition, Model model) {
        int setPage = 1;
        int setSize = 20;

        String page = condition.getPage();
        String size = condition.getSize();

        if (hasText(page) && page.matches("^\\d+$")) setPage = Math.max(Integer.parseInt(page), 1);
        if (hasText(size) && size.matches("^\\d+$")) setSize = Math.max(Integer.parseInt(size), 1);

        PageRequest request = PageRequest.of(setPage - 1, setSize, Sort.by("id").descending());

        Page<BoardNoticeListDto> boardNoticeList = boardNoticeService.searchAllBoardNotice(condition, request);

        model.addAttribute("condition", condition);
        model.addAttribute("boardNoticeList", boardNoticeList);

        return "sub/notice_list";
    }

    /**
     * 안내 > 공지사황 > 상세
     *
     * @param id 글 시퀀스
     * @author SungHa
     */
    @Operation(
            summary = "안내 > 공지사항 > 상세",
            description = "공지사항 상세"
    )
    @GetMapping("/board/notice/{id}")
    public String adminBoardNoticeDetail(@PathVariable("id") Long id, Model model) {
        BoardNoticeDetailDto boardNotice = boardNoticeService.searchBoardNoticeById(id);

        if (boardNotice == null) {
            return "redirect:/board/notice";
        }

        // 조회수 증가
        boardNoticeService.addHit(id);

        model.addAttribute("boardNotice", boardNotice);

        return "sub/notice_view";
    }

}
