package com.cuv.admin.domain.terms;

import com.cuv.admin.domain.terms.dto.TermsSaveDto;

public interface TermsRepositoryCustom {
    TermsSaveDto searchAllTerms(TermsSaveDto condition);
}
