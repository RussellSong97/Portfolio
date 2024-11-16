package com.cuv.web.controller.terms;

import com.cuv.domain.terms.TermsService;
import com.cuv.domain.terms.enumset.TermsType;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class TermsController {

    private final TermsService termsService;

    /**
     * 서브 페이지에서 API 현태로 이용약관 호출
     *
     * @return String
     * @author DONGGWAN KIM
     */
    @GetMapping("/api/terms/service")
    public String service() {
        return termsService.searchAllTerms(TermsType.SERVICE);
    }


    /**
     * 서브 페이지에서 API 현태로 개인정보처리방침 호출
     *
     * @return String
     * @author DONGGWAN KIM
     */
    @GetMapping("/api/terms/privacy")
    public String privacy() {
        return termsService.searchAllTerms(TermsType.PRIVACY);
    }
}
