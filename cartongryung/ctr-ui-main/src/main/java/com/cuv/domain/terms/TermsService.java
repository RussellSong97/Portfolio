package com.cuv.domain.terms;

import com.cuv.domain.terms.enumset.TermsType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TermsService {

    private final TermsRepository termsRepository;

    /**
     * 메인 > 이용약관 및 개인정보처리방침
     *
     * @param termsType 약관 종류
     * @author SungHa
     */
    public String searchAllTerms(TermsType termsType) {
        return termsRepository.searchAllTerms(termsType);
    }

}
