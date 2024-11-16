package com.cuv.domain.popularkeyword;

import com.cuv.domain.popularkeyword.dto.PopularKeywordListDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
* 인기 검색어 서비스 (메인 > 인기 검색어 조회)
 */
@Service
@RequiredArgsConstructor
public class PopularKeywordService {

    private final PopularKeywordRepository popularKeywordRepository;

    /**
     * 메인 > 샵 검색어
     * @author SungHa
     */
    public List<PopularKeywordListDto> searchAllPopularKeyword() {
        return popularKeywordRepository.searchAllPopularKeyword();
    }


}
