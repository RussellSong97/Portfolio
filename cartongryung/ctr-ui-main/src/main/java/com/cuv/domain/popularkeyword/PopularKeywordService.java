package com.cuv.domain.popularkeyword;

import com.cuv.domain.popularkeyword.dto.PopularKeywordListDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@RequiredArgsConstructor
public class PopularKeywordService {

    private final PopularKeywordRepository popularKeywordRepository;

    /**
     * 메인 > 샵 검색어
     *
     * @author SungHa
     */
    public List<PopularKeywordListDto> searchAllPopularKeyword() {
        return popularKeywordRepository.searchAllPopularKeyword();
    }


}
