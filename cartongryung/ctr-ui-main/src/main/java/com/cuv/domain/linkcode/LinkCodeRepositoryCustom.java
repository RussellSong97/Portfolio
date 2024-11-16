package com.cuv.domain.linkcode;

import com.cuv.domain.linkcode.dto.LinkCodeListDto;

import java.util.List;

public interface LinkCodeRepositoryCustom {

    Integer searchLastOrderSeq(Long parentLinkNbrId, Integer depth);

    List<LinkCodeListDto> searchAlinkCodeList();

    List<LinkCodeListDto> searchFirstCategoryList();

    List<LinkCodeListDto> searchCategoryListByCategoryId(Long categoryId);

    List<LinkCodeListDto> searchApiCategoryChildrenList(Long categoryId, Integer depth);

    List<LinkCodeListDto> searchCategory2ListName(List<Long> modelIds);
}
