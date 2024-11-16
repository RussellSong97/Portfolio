package com.cuv.admin.domain.linklisting;

import com.cuv.admin.domain.linklisting.dto.LinkListingListDto;
import com.cuv.admin.domain.linklisting.dto.LinkListingSearchDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface LinkListingRepositoryCustom {

    void clear();

    void deleteAllByState();

    void revertState();

    void updateShopName(String oldName, String newName);

    Page<LinkListingListDto> searchAllLinkage(LinkListingSearchDto condition, Pageable pageable);
}
