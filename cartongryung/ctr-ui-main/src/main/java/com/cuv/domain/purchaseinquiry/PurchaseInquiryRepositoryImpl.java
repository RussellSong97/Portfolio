package com.cuv.domain.purchaseinquiry;

import com.cuv.common.YN;
import com.cuv.domain.inquirydetail.enumset.TradeType;
import com.cuv.domain.linkcode.entity.QLinkCode;
import com.cuv.domain.purchaseinquiry.dto.PurchaseInquiryListDto;
import com.cuv.domain.purchaseinquiry.enumset.ConsultationType;
import com.cuv.domain.purchaseinquiry.enumset.InquiryType;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Wildcard;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.cuv.domain.inquirydetail.entity.QInquiryDetail.inquiryDetail;
import static com.cuv.domain.memberadmin.entity.QMemberAdmin.memberAdmin;
import static com.cuv.domain.product.entity.QProduct.product;
import static com.cuv.domain.purchaseinquiry.entity.QPurchaseInquiry.purchaseInquiry;
import static com.cuv.domain.wishlist.entity.QWishlist.wishlist;

@Repository
public class PurchaseInquiryRepositoryImpl implements PurchaseInquiryRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public PurchaseInquiryRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public Long searchPurchaseInquiryCountByMemberId(Long memberId) {
        return queryFactory
                .select(Wildcard.count)
                .from(purchaseInquiry)
                .where(
                        condMemberIdEq(memberId),
                        condInquiryTypeCodeEqContactable(),
                        condConsultationTypeCodeEqCallOrChatting(),
                        condDelYnEqN()
                )
                .fetchOne();
    }

    @Override
    public Long searchVisitReservationCountByMemberId(Long memberId) {
        return queryFactory
                .select(Wildcard.count)
                .from(purchaseInquiry)
                .where(
                        condMemberIdEq(memberId),
                        condConsultationTypeCodeEqVisit(),
                        condDelYnEqN()
                )
                .fetchOne();
    }

    @Override
    public List<PurchaseInquiryListDto> searchPurchaseInquiryByMemberId(Long memberId) {
        return queryFactory
                .select(Projections.fields(PurchaseInquiryListDto.class,
                        ExpressionUtils.as(JPAExpressions
                                        .select(inquiryDetail.consultationStatus)
                                        .from(inquiryDetail)
                                        .where(
                                                inquiryDetail.id.eq(
                                                        JPAExpressions
                                                                .select(inquiryDetail.id.max())
                                                                .from(inquiryDetail)
                                                                .where(
                                                                        inquiryDetail.inquiryId.eq(purchaseInquiry.id)
                                                                )
                                                ).and(inquiryDetail.tradeTypeCode.eq(TradeType.BUY))
                                        )
                                , "consultationStatus"),
                        ExpressionUtils.as(JPAExpressions
                                        .select(memberAdmin.role)
                                        .from(inquiryDetail)
                                        .leftJoin(memberAdmin)
                                        .on(memberAdmin.id.eq(inquiryDetail.memberAdminId))
                                        .where(
                                                inquiryDetail.id.eq(
                                                        JPAExpressions
                                                                .select(inquiryDetail.id.max())
                                                                .from(inquiryDetail)
                                                                .where(
                                                                        inquiryDetail.inquiryId.eq(purchaseInquiry.id)
                                                                )
                                                ).and(inquiryDetail.tradeTypeCode.eq(TradeType.BUY))
                                        )
                                , "inquiryDetailRole"),
                        ExpressionUtils.as(JPAExpressions
                                        .select(memberAdmin.realName)
                                        .from(inquiryDetail)
                                        .leftJoin(memberAdmin)
                                        .on(memberAdmin.id.eq(inquiryDetail.memberAdminId))
                                        .where(
                                                inquiryDetail.id.eq(
                                                        JPAExpressions
                                                                .select(inquiryDetail.id.max())
                                                                .from(inquiryDetail)
                                                                .where(
                                                                        inquiryDetail.inquiryId.eq(purchaseInquiry.id)
                                                                )
                                                ).and(inquiryDetail.tradeTypeCode.eq(TradeType.BUY))
                                        )
                                , "inquiryDetailWriter"),
                        ExpressionUtils.as(JPAExpressions
                                        .select(inquiryDetail.createdAt)
                                        .from(inquiryDetail)
                                        .where(
                                                inquiryDetail.id.eq(
                                                        JPAExpressions
                                                                .select(inquiryDetail.id.max())
                                                                .from(inquiryDetail)
                                                                .where(
                                                                        inquiryDetail.inquiryId.eq(purchaseInquiry.id)
                                                                )
                                                ).and(inquiryDetail.tradeTypeCode.eq(TradeType.BUY))
                                        )
                                , "createdAt")
                ))
                .from(purchaseInquiry)
                .where(
                        condMemberIdEq(memberId),
                        condInquiryTypeCodeEqContactable(),
                        condConsultationTypeCodeEqCallOrChatting(),
                        condDelYnEqN()
                )
                .orderBy(purchaseInquiry.id.desc())
                .fetch();
    }

    public List<PurchaseInquiryListDto> searchVisitReservationByMemberId(Long memberId) {
        QLinkCode maker = new QLinkCode("maker");
        QLinkCode detailModel = new QLinkCode("detailModel");
        QLinkCode detailGrade = new QLinkCode("detailGrade");

        return queryFactory
                .select(Projections.fields(PurchaseInquiryListDto.class,
                        product.id.as("productId"),
                        product.carImageUrl,
                        product.carPlateNumber,
                        maker.linkDataNm.as("makerName"),
                        detailModel.linkDataNm.as("detailModelName"),
                        detailGrade.linkDataNm.as("detailGradeName"),
                        product.carRegYear,
                        product.carUseKm,
                        product.carFuel,
                        product.city,
                        purchaseInquiry.visitReservationAt,
                        ExpressionUtils.as(JPAExpressions
                                        .select(inquiryDetail.consultationStatus)
                                        .from(inquiryDetail)
                                        .where(
                                                inquiryDetail.id.eq(
                                                        JPAExpressions
                                                                .select(inquiryDetail.id.max())
                                                                .from(inquiryDetail)
                                                                .where(
                                                                        inquiryDetail.inquiryId.eq(purchaseInquiry.id)
                                                                )
                                                ).and(inquiryDetail.tradeTypeCode.eq(TradeType.BUY))
                                        )
                                , "consultationStatus")
                ))
                .from(purchaseInquiry)
                .innerJoin(wishlist)
                .on(wishlist.purchaseInquiryId.eq(purchaseInquiry.id))
                .innerJoin(product)
                .on(product.id.eq(wishlist.productId))
                .innerJoin(maker)
                .on(maker.id.eq(product.makerNumber))
                .innerJoin(detailModel)
                .on(detailModel.id.eq(product.detailModelNumber))
                .innerJoin(detailGrade)
                .on(detailGrade.id.eq(product.detailGradeNumber))
                .where(
                        condMemberIdEq(memberId),
                        condConsultationTypeCodeEqVisit(),
                        condDelYnEqN()
                )
                .orderBy(purchaseInquiry.id.desc())
                .fetch();
    }

    private Predicate condMemberIdEq(Long id) {
        return id != null ? purchaseInquiry.memberId.eq(id) : null;
    }

    private Predicate condInquiryTypeCodeEqContactable() {
        return purchaseInquiry.inquiryTypeCode.eq(InquiryType.CONTACTABLE);
    }

    private Predicate condConsultationTypeCodeEqCallOrChatting() {
        return purchaseInquiry.consultationTypeCode.eq(ConsultationType.CALL)
                .or(purchaseInquiry.consultationTypeCode.eq(ConsultationType.CHATTING));
    }

    private Predicate condConsultationTypeCodeEqVisit() {
        return purchaseInquiry.consultationTypeCode.eq(ConsultationType.VISIT);
    }

    private Predicate condDelYnEqN() {
        return new BooleanBuilder().and(purchaseInquiry.delYn.eq(YN.N));
    }

}
