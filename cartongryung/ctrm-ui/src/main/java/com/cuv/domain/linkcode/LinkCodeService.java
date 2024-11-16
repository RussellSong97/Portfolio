package com.cuv.domain.linkcode;

import com.cuv.common.JSONResponse;
import com.cuv.domain.linkcode.dto.LinkCodeListDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * 제조사,모델,세부모델 정보 조회
 */
@Service
@RequiredArgsConstructor
public class LinkCodeService {
    private final LinkCodeRepository linkCodeRepository;


    /**
     * 1차 카테고리 리스트 (제조사)
     * @return
     */
    public List<LinkCodeListDto> searchFirstCategoryList() {
        return linkCodeRepository.searchFirstCategoryList();
    }


    /**
     * 사용자 메인 차량 제조서,모델,세부모델
     * @param map
     * @return
     */
    public JSONResponse<?> searchApiMainCategory(Map<String, Object> map) {
        List<LinkCodeListDto> categoryList = linkCodeRepository.searchApiMainCategory(map);
        return new JSONResponse<>(200, "SUCCESS", categoryList);
    }

    /**
     * 조건에 맞는 하위 카테고리 정보
     * @param map
     * @return
     */
    public JSONResponse<?> searchApiCategoryChildren(Map<String, Object> map) {
        Long categoryId = Long.valueOf(map.get("categoryId").toString());
        Integer depth = Integer.valueOf(map.get("depth").toString());
        List<LinkCodeListDto> categoryChildrenList = linkCodeRepository.searchApiCategoryChildrenList(categoryId, depth);

        return new JSONResponse<>(200, "SUCCESS", categoryChildrenList);
    }
}
