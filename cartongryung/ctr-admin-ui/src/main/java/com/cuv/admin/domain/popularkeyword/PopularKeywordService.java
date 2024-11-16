package com.cuv.admin.domain.popularkeyword;

import com.cuv.admin.domain.popularkeyword.dto.PopularKeywordListDto;
import com.cuv.admin.domain.popularkeyword.entity.PopularKeyword;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 인기 검색어 관리 (등록, 수정, 조회)
 */
@Service
@RequiredArgsConstructor
public class PopularKeywordService {

    private final PopularKeywordRepository popularKeywordRepository;

    /**
     * 관리자 | 기타 > 인기 검색어 관리 > 저장된 값 불러오기
     *
     * @author SungHa
     */
    public List<PopularKeywordListDto> searchAllPopularKeyword() {
        return popularKeywordRepository.searchAllPopularKeyword();
    }

    /**
     * 관리자 | 기타 > 인기 검색어 관리 > 등록 및 수정
     *
     * @param data 저장할 데이터
     * @author SungHa
     */
    @Transactional
    public void adminEtcKeywordWriteProc( List<Map<String , Object>> data) {
        List<PopularKeyword> popularKeywordLists = new ArrayList<>();
        for (Map<String, Object> row : data) {
            Long id = row.get("id") != null ? Long.valueOf((String) row.get("id")) : null;
            Long makerCodeId = row.get("makerId") != null ? Long.valueOf((String) row.get("makerId")) : null;
            Long modelCodeId = row.get("modelId") != null ? Long.valueOf((String) row.get("modelId")) : null;

            PopularKeyword popularKeyword = PopularKeyword.builder()
                .id(id)
                .makerCodeId(makerCodeId)
                .modelCodeId(modelCodeId)
                .build();

            popularKeywordLists.add(popularKeyword);
        }

        popularKeywordRepository.saveAll(popularKeywordLists);

    }

}
