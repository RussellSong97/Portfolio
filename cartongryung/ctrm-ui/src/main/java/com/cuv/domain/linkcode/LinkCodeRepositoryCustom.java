package com.cuv.domain.linkcode;

import com.cuv.domain.linkcode.dto.LinkCodeListDto;

import java.util.List;
import java.util.Map;

public interface LinkCodeRepositoryCustom {

    Integer searchLastOrderSeq(Long parentLinkNbrId, Integer depth);

    List<LinkCodeListDto> searchFirstCategoryList();

    List<LinkCodeListDto> searchApiCategoryChildrenList(Long categoryId, Integer depth);

    List<LinkCodeListDto> searchCategory2ListName(List<Long> modelIds);

    List<LinkCodeListDto> searchApiMainCategory(Map<String, Object> map);
}
