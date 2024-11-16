package com.cuv.web.controller.board;

import com.cuv.domain.boardguide.BoardGuideService;
import com.cuv.domain.boardguide.dto.BoardGuideDetailDto;
import com.cuv.domain.boardguide.dto.BoardGuideListDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class BoardGuideController {

    private final BoardGuideService boardGuideService;

    /**
     * 안내 > 중고차 가이드 > 목록
     *
     * @author SungHa
     */
    @GetMapping("/board/guide")
    public String boardGuideList(Model model) {
        List<BoardGuideListDto> boardGuideLists = boardGuideService.searchAllBoardGuide();

        model.addAttribute("boardGuideLists", boardGuideLists);

        return "sub/guide_list";
    }

    /**
     * 안내 > 중고차 가이드 > 상세
     *
     * @param id 글 시퀀스
     * @author SungHa
     */
    @GetMapping("/board/guide/{id}")
    public String boardGuideDetail(@PathVariable("id") Long id, Model model) {
        BoardGuideDetailDto boardGuide = boardGuideService.searchBoardGuideById(id);

        if (boardGuide == null) {
            return "redirect:/board/guide";
        }

        model.addAttribute("boardGuide", boardGuide);

        return "sub/guide_view";
    }

}
