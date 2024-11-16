package com.cuv.domain.linkinfo;

import com.cuv.domain.linkinfo.dto.LinkInfoListDto;

import java.util.List;

public interface LinkInfoRepositoryCustom {

    List<LinkInfoListDto> searchApiProductColor();

    List<LinkInfoListDto> searchApiProductMoreColor();
}
