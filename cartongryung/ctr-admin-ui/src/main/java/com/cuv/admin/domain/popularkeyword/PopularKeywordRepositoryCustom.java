package com.cuv.admin.domain.popularkeyword;

import com.cuv.admin.domain.popularkeyword.dto.PopularKeywordListDto;

import java.util.List;

public interface PopularKeywordRepositoryCustom {
    List<PopularKeywordListDto> searchAllPopularKeyword();
}
