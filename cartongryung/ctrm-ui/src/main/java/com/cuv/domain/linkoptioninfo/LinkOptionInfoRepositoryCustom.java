package com.cuv.domain.linkoptioninfo;

import com.cuv.domain.linkoptioninfo.dto.LinkOptionInfoListDto;
import com.cuv.domain.linkoptioninfo.entity.LinkOptionInfo;
import com.cuv.domain.product.dto.SpecGroupDto;

import java.util.List;

public interface LinkOptionInfoRepositoryCustom {
    List<LinkOptionInfoListDto> searchApiProductOptionList();

    List<SpecGroupDto> searchApiProductMoreOptionList();

    List<LinkOptionInfoListDto> searchMobileOptionList();
}
