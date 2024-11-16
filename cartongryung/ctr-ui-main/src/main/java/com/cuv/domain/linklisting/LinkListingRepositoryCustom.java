package com.cuv.domain.linklisting;

import com.cuv.domain.linklisting.dto.LinkListingListDto;

import java.util.List;

public interface LinkListingRepositoryCustom {
    List<LinkListingListDto> searchApiProductFuel();

    List<LinkListingListDto> searchApiProductSido();

    List<LinkListingListDto> searchApiProductIstdTrans();

    List<LinkListingListDto> searchMobileFuelList();

    List<LinkListingListDto> searchMobileIstdTrans();

    List<LinkListingListDto> searchMobileSidoList();
}
