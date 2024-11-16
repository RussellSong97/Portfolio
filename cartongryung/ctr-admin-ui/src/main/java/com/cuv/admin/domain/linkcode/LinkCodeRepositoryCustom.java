package com.cuv.admin.domain.linkcode;

import com.cuv.admin.domain.linkcode.dto.LinkCodeDetailDto;
import com.cuv.admin.domain.linkcode.dto.LinkCodeListDto;
import com.cuv.admin.domain.linkcode.dto.LinkCodeSearchDto;

import java.util.List;

public interface LinkCodeRepositoryCustom {
    List<LinkCodeListDto> searchAllLinkCode(LinkCodeSearchDto condition);

    Integer searchLastOrderSeq(Long parentLinkNbrId, Integer depth);

    LinkCodeDetailDto searchLinkCodeById(Long id);

}
