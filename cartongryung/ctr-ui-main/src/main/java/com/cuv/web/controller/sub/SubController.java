package com.cuv.web.controller.sub;

import com.cuv.domain.terms.TermsService;
import com.cuv.domain.terms.enumset.TermsType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class SubController {

    private final TermsService termsService;

    /**
     * 오시는 길 > 본사
     *
     * @author SungHa
     */
    @GetMapping("/map01")
    public String map01() {
        return "sub/map01";
    }

    /**
     * 오시는 길 > 공식 서비스센터
     *
     * @author SungHa
     */
    @GetMapping("/map02")
    public String map02() {
        return "sub/map02";
    }

    /**
     * 평생책임서비스 > 무상점검
     *
     * @author SungHa
     */
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
    @GetMapping("/service03")
    public String service03(Model model) {
        // 사용자 이용약관
        String service = termsService.searchAllTerms(TermsType.SERVICE);

        model.addAttribute("service", service);

        return "sub/service03";
    }

    /**
     * 우측 메뉴 > 설정
     *
     * @author SungHa
     */
    @GetMapping("/setting")
    public String setting() {
        return "sub/setting";
    }

    /**
     * 우측 메뉴 > 푸시 설정
     *
     * @author SungHa
     */
    @GetMapping("/setting/push")
    public String settingPush() {
        return "sub/setting_push";
    }

}
