package com.cuv.admin.domain.wishlist;

import com.cuv.admin.domain.wishlist.dto.WishlistListDto;

import java.util.List;

public interface WishlistRepositoryCustom {

    List<WishlistListDto> searchAllWishlists(Long id);
}
