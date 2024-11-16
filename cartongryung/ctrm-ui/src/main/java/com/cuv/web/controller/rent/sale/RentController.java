package com.cuv.web.controller.rent.sale;

import com.cuv.domain.boardfaq.BoardFaqService;
import com.cuv.domain.boardfaq.dto.BoardFaqListDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

import static com.cuv.util.CheckDevice.findDevice;

@Tag(
        name = "사용자 - 렌트",
        description = "사용자 - 렌트"
)
@Controller
@RequiredArgsConstructor
public class RentController {
    private final BoardFaqService boardFaqService;

    /**
     * 렌트
     */
    @Operation(
            summary = "렌트 > 개인 차량",
            description = "상단 메뉴 클릭할 경우 렌트 > 개인차량으로 이동"
    )
    @GetMapping("/rent/list")
    public String goToRentList(HttpServletRequest request, Model model) {
        String header = request.getHeader("User-Agent").toLowerCase().replaceAll(" ", "");

        model.addAttribute("isMobile", findDevice(header));
        model.addAttribute("whichFile", "personal");

        List<BoardFaqListDto> list = boardFaqService.searchAllBoardFaq();

        model.addAttribute("faqList", list);

        return "rent/rent_list";
    }

    /**
     * 렌트
     */
    @Operation(
            summary = "렌트 > 개인 차량",
            description = "중간 탭을 이용하여 개인차량으로 이동"
    )
    @GetMapping("/rent/personal")
    public String goToPersonalRentCar(HttpServletRequest request, Model model) {
        String header = request.getHeader("User-Agent").toLowerCase().replaceAll(" ", "");

        model.addAttribute("isMobile", findDevice(header));
        model.addAttribute("whichFile", "personal");

        return "rent/rent_list";
    }

    /**
     * 렌트
     */
    @Operation(
            summary = "렌트 > 사업자/법인 차량",
            description = "중간 탭을 이용하여 사업자/법인 차량으로 이동"
    )
    @GetMapping("/rent/corporate")
    public String goToCorporateRentCar(HttpServletRequest request, Model model) {
        String header = request.getHeader("User-Agent").toLowerCase().replaceAll(" ", "");

        model.addAttribute("isMobile", findDevice(header));
        model.addAttribute("whichFile", "corporate");

        return "rent/rent_list";
    }
}