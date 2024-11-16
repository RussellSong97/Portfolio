package com.cuv.domain.popularkeyword;


import com.cuv.domain.popularkeyword.dto.PopularKeywordListDto;

import java.util.List;

public interface PopularKeywordRepositoryCustom {
    List<PopularKeywordListDto> searchAllPopularKeyword();
}
