package com.cuv.web.controller.event;

import com.cuv.domain.exhibition.ExhibitionService;
import com.cuv.domain.exhibition.dto.ExhibitionDetailDto;
import com.cuv.domain.exhibition.dto.ExhibitionListDto;
import com.cuv.domain.exhibition.dto.ExhibitionSearchDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Tag(
        name = "사용자 - 이벤트",
        description = "사용자 - 이벤트"
)
@Controller
@RequiredArgsConstructor
public class EventController {

    private final ExhibitionService exhibitionService;

    /**
     * 이벤트
     *
     * @author SungHa
     */
    @Operation(
            summary = "사용자 - 이벤트",
            description = "이벤트"
    )
    @GetMapping("/event")
    public String eventList(ExhibitionSearchDto condition, Model model) {
        List<ExhibitionListDto> eventLists = exhibitionService.searchAllEvent(condition);

        model.addAttribute("eventLists", eventLists);

        return "sub/event_list";
    }

    /**
     * 이벤트 > 상세
     *
     * @param id 글 시퀀스
     * @author SungHa
     */
    @Operation(
            summary = "사용자 - 이벤트",
            description = "이벤트 > 상세"
    )
    @GetMapping("/event/{id}")
    public String eventDetail(@PathVariable("id") Long id, Model model) {
        ExhibitionDetailDto event = exhibitionService.searchExhibitionById(id);

        // 조회수 증가
        exhibitionService.addHit(id);

        model.addAttribute("event", event);

        return "sub/event_view";
    }

}
