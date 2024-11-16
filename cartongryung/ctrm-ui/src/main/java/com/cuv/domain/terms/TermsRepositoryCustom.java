package com.cuv.domain.terms;

import com.cuv.domain.terms.enumset.TermsType;

public interface TermsRepositoryCustom {
    String searchAllTerms(TermsType termsType);
}
