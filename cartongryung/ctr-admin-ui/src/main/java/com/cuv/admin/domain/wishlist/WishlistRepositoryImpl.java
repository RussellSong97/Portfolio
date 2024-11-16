package com.cuv.admin.domain.wishlist;

import com.cuv.admin.common.YN;
import com.cuv.admin.domain.wishlist.dto.WishlistListDto;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.cuv.admin.domain.memberadmin.entity.QMemberAdmin.memberAdmin;
import static com.cuv.admin.domain.product.entity.QProduct.product;
import static com.cuv.admin.domain.wishlist.entity.QWishlist.wishlist;

@Repository
public class WishlistRepositoryImpl implements WishlistRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public WishlistRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public List<WishlistListDto> searchAllWishlists(Long id) {
        return queryFactory
                .select(Projections.fields(WishlistListDto.class,
                        wishlist.id,
                        product.id.as("productId"),
                        product.productUniqueNumber,
                        product.carPlateNumber,
                        product.postStatus,
                        memberAdmin.realName
                        ))
                .from(wishlist)
                .innerJoin(product)
                .on(product.id.eq(wishlist.productId))
                .leftJoin(memberAdmin)
                .on(memberAdmin.id.eq(product.memberDealerId))
                .where(
                        condInquiryIdEq(id),
                        condDelYnEqN()
                )
                .orderBy(product.postStatus.desc())
                .fetch();
    }

    private Predicate condInquiryIdEq(Long id) {
        return id != null ? wishlist.purchaseInquiryId.eq(id) : null;
    }

    private Predicate condDelYnEqN() {
        return new BooleanBuilder().and(wishlist.delYn.eq(YN.N));
    }

}
