package com.cuv.web.controller.board;

import com.cuv.domain.boardfaq.BoardFaqService;
import com.cuv.domain.boardfaq.dto.BoardFaqListDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Tag(
        name = "사용자 - 안내 - 자주 묻는 질문",
        description = "사용자 - 안내 - 자주 묻는 질문"
)
@Controller
@RequiredArgsConstructor
public class BoardFaqController {
    private final BoardFaqService boardFaqService;

    /**
     * 안내 > 자주 묻는 질문 > 목록
     *
     * @author SungHa
     */
    @Operation(
            summary = "안내 > 자주 묻는 질문 > 목록",
            description = "자주 묻는 질문 목록"
    )
    @GetMapping("/board/faq")
    public String boardFaq(Model model) {
        List<BoardFaqListDto> list = boardFaqService.searchAllBoardFaq();

        model.addAttribute("faqList", list);

        return "sub/faq_list";
    }

}
