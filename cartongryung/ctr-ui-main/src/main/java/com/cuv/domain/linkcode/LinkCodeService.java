package com.cuv.domain.linkcode;

import com.cuv.common.JSONResponse;
import com.cuv.domain.linkcode.dto.LinkCodeListDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LinkCodeService {
    private final LinkCodeRepository linkCodeRepository;

    public List<LinkCodeListDto> searchAllLinkCodeList() {
        return linkCodeRepository.searchAlinkCodeList();
    }

    public List<LinkCodeListDto> searchFirstCategoryList() {
        return linkCodeRepository.searchFirstCategoryList();
    }

    public JSONResponse<?> searchCategoryListByCategoryId(Long categoryId) {
        List<LinkCodeListDto> categoryList = linkCodeRepository.searchCategoryListByCategoryId(categoryId);
        return new JSONResponse<>(200, "SUCCESS", categoryList);
    }
}
