package com.cuv.web.controller.sub;

import com.cuv.common.WarrantyCost;
import com.cuv.domain.member.MemberService;
import com.cuv.domain.member.dto.MemberInfoDto;
import com.cuv.domain.terms.TermsService;
import com.cuv.domain.terms.enumset.TermsType;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import static com.cuv.util.CheckDevice.findDevice;

@Tag(
        name = "사용자 - 헤더 메뉴",
        description = "사용자 - 헤더 메뉴"
)
@Slf4j
@Controller
@RequiredArgsConstructor
public class SubController {

    private final TermsService termsService;
    private final MemberService memberService;

    /**
     * CUV 할부
     *
     * @author jooree
     */
    @Operation(
            summary = "CUV 할부",
            description = "CUV 할부"
    )
    @GetMapping("/installments")
    public String installments(Model model, HttpServletRequest request) {
        String header = request.getHeader("User-Agent").toLowerCase().replaceAll(" ", "");

        model.addAttribute("isMobile", findDevice(header));
        return "sub/installments";
    }

    /**
     * CUV 할부
     *
     * @author jooree
     */
    @Operation(
            summary = "CUV 리스",
            description = "CUV 리스"
    )
    @GetMapping("/lease")
    public String lease(Model model, HttpServletRequest request) {
        String header = request.getHeader("User-Agent").toLowerCase().replaceAll(" ", "");

        model.addAttribute("isMobile", findDevice(header));
        return "sub/lease";
    }

    /**
     * CUV 워런티
     *
     * @author jooree
     */
    @Operation(
            summary = "CUV 워런티",
            description = "CUV 워런티"
    )
    @GetMapping("/warranty")
    public String warranty(Model model) {
        model.addAttribute("warrantyCostVal", WarrantyCost.LOCAL_BASIC_QUARTER_PV16.costLabel());

        return "sub/warranty";
    }

    /**
     * CUV워런티 > 보증내용
     *
     * @param costID 값을 가져오는 매개변수
     */
    @Operation(
            summary = "CUV 워런티 > 보증 내용",
            description = "CUV 워런티 > 보증 내용"
    )
    @ResponseBody
    @GetMapping("/api/sub/warranty/{costID}")
    public String getWarrantyCost(@PathVariable("costID") String costID) {
        try{
            WarrantyCost wcCostID = WarrantyCost.valueOf(costID);

            return wcCostID.costLabel();
        }catch (Exception e){
            log.error(e.getMessage());
            return null;
        }
    }

    /**
     * 오시는 길 > 본사
     *
     * @author SungHa
     */
    @Operation(
            summary = "오시는 길 > 본사",
            description = "오시는 길 > 본사"
    )
    @GetMapping("/map01")
    public String map01() {
        return "sub/map01";
    }

    /**
     * 오시는 길 > 공식 서비스센터
     *
     * @author SungHa
     */
    @Operation(
            summary = "오시는 길 > 공식 서비스센터",
            description = "오시는 길 > 공식 서비스센터"
    )
    @GetMapping("/map02")
    public String map02() {
        return "sub/map02";
    }

    /**
     * 평생책임서비스 > 무상점검
     *
     * @author SungHa
     */
    @Operation(
            summary = "오시는 길 > 무상점검",
            description = "오시는 길 > 무상점검"
    )
    @GetMapping("/service01")
    public String service01(Model model) {
        // 사용자 이용약관
        String service = termsService.searchAllTerms(TermsType.SERVICE);

        model.addAttribute("service", service);

        return "sub/service01";
    }

    /**
     * 평생책임서비스 > 소모품 교체
     *
     * @author SungHa
     */
    @Operation(
            summary = "오시는 길 > 소모품 교체",
            description = "오시는 길 > 소모품 교체"
    )
    @GetMapping("/service02")
    public String service02(Model model) {
        // 사용자 이용약관
        String service = termsService.searchAllTerms(TermsType.SERVICE);

        model.addAttribute("service", service);

        return "sub/service02";
    }

    /**
     * 평생책임서비스 > 공임비 무료
     *
     * @author SungHa
     */
    @Operation(
            summary = "오시는 길 > 공임비 무료",
            description = "오시는 길 > 공임비 무료"
    )
    @GetMapping("/service03")
    public String service03(Model model) {
        // 사용자 이용약관
        String service = termsService.searchAllTerms(TermsType.SERVICE);

        model.addAttribute("service", service);

        return "sub/service03";
    }

    /**
     * 우측 메뉴 > 알림 설정
     *
     * @author SungHa
     */
    @Operation(
            summary = "우측 메뉴 > 알림 설정",
            description = "우측 메뉴 > 알림 설정"
    )
    @GetMapping("/setting/notify")
    public String settingPush(Authentication authentication, Model model) {
        MemberInfoDto memberDto = memberService.searchMemberInfo(authentication.getName());
        model.addAttribute("memberDto", memberDto);

        return "sub/setting_push";
    }

    /**
     * 차량 상세 > 페이스북 공유
     *
     * @author 박성민
     */
    @Operation(
            summary = "차량 상세 > 페이스북 공유",
            description = "차량 상세 > 페이스북 공유"
    )
    @GetMapping("/api/sub/facebook")
    public String sendInfoForOg(
            @RequestParam(value = "productName", required = false) String productName,
            @RequestParam(value = "url", required = false) String url,
            @RequestParam(value = "img",required = false) String img,
            Model model) {

        model.addAttribute("url", url);
        model.addAttribute("productName", productName);
        model.addAttribute("img", img);

        return "vehicle/test";
    }
}
