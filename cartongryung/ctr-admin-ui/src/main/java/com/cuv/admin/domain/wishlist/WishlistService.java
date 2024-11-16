package com.cuv.admin.domain.wishlist;

import com.cuv.admin.domain.wishlist.dto.WishlistListDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 구해희망 차량 목록
 */
@Service
@Transactional
@RequiredArgsConstructor
public class WishlistService {

    private final WishlistRepository wishlistRepository;

    /**
     * 관리자 | 내 차 구입 > 연락 가능 문의 > 상세 > 구매희망 차량 목록
     *
     * @param id 글 시퀀스
     * @author SungHa
     */
    public List<WishlistListDto> searchAllWishlists(Long id) {
        return wishlistRepository.searchAllWishlists(id);
    }

}
