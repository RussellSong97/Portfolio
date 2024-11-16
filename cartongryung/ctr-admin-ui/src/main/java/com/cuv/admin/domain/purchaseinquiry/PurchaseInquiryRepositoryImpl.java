package com.cuv.admin.domain.purchaseinquiry;

import com.cuv.admin.common.YN;
import com.cuv.admin.domain.inquirydetail.dto.InquiryDetailCountDto;
import com.cuv.admin.domain.inquirydetail.entity.QInquiryDetail;
import com.cuv.admin.domain.inquirydetail.enumset.TradeType;
import com.cuv.admin.domain.purchaseinquiry.dto.PurchaseInquiryDetailDto;
import com.cuv.admin.domain.purchaseinquiry.dto.PurchaseInquiryListDto;
import com.cuv.admin.domain.purchaseinquiry.dto.PurchaseInquiryMemberDto;
import com.cuv.admin.domain.purchaseinquiry.dto.PurchaseInquirySearchDto;
import com.cuv.admin.domain.purchaseinquiry.enumset.ConsultationStatus;
import com.cuv.admin.domain.purchaseinquiry.enumset.InquiryType;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.*;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.Wildcard;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static com.cuv.admin.domain.inquirydetail.entity.QInquiryDetail.inquiryDetail;
import static com.cuv.admin.domain.member.entity.QMember.member;
import static com.cuv.admin.domain.memberadmin.entity.QMemberAdmin.memberAdmin;
import static com.cuv.admin.domain.product.entity.QProduct.product;
import static com.cuv.admin.domain.purchaseinquiry.entity.QPurchaseInquiry.purchaseInquiry;
import static com.cuv.admin.domain.saleinquiry.entity.QSaleInquiry.saleInquiry;
import static com.cuv.admin.domain.wishlist.entity.QWishlist.wishlist;
import static org.springframework.util.StringUtils.hasText;

@Repository
public class PurchaseInquiryRepositoryImpl implements PurchaseInquiryRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public PurchaseInquiryRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public Page<PurchaseInquiryListDto> searchAllPurchaseInquiryLists(PurchaseInquirySearchDto condition, Pageable pageable) {
        BooleanExpression consultationCondition = getConsultationCondition(condition);
        BooleanExpression carNumberCondition = getCarNumberCondition(condition);
        BooleanExpression dealerCondition = getDealerCondition(condition);
        BooleanExpression counselorCondition = getCounselorCondition(condition);

        List<PurchaseInquiryListDto> content = queryFactory
                .select(Projections.fields(PurchaseInquiryListDto.class,
                        purchaseInquiry.id,
                        purchaseInquiry.inquiryNumber,
                        ExpressionUtils.as(JPAExpressions
                                        .select(product.carPlateNumber.min())
                                        .from(wishlist)
                                        .innerJoin(product)
                                        .on(product.id.eq(wishlist.productId))
                                        .where(
                                                wishlist.purchaseInquiryId.eq(
                                                        JPAExpressions
                                                                .select(wishlist.purchaseInquiryId.max())
                                                                .from(wishlist)
                                                                .where(
                                                                        wishlist.purchaseInquiryId.eq(purchaseInquiry.id)
                                                                )
                                                ).and(wishlist.delYn.eq(YN.N))
                                        )
                                , "carPlateNumber"),
                        ExpressionUtils.as(JPAExpressions
                                        .select(Wildcard.count)
                                        .from(wishlist)
                                        .where(
                                                wishlist.purchaseInquiryId.eq(
                                                        JPAExpressions
                                                                .select(wishlist.purchaseInquiryId.max())
                                                                .from(wishlist)
                                                                .where(
                                                                        wishlist.purchaseInquiryId.eq(purchaseInquiry.id)
                                                                )
                                                ).and(wishlist.delYn.eq(YN.N))
                                        )
                                , "wishCount"),
                        purchaseInquiry.consultationTypeCode,
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
                        purchaseInquiry.memberId,
                        purchaseInquiry.realName,
                        purchaseInquiry.mobileNumber,
                        purchaseInquiry.email,
                        member.realName.as("memberRealName"),
                        member.mobileNumber.as("memberMobileNumber"),
                        member.email.as("memberEmail"),
                        purchaseInquiry.visitReservationAt,
                        purchaseInquiry.createdAt,
                        ExpressionUtils.as(JPAExpressions
                                        .select(memberAdmin.realName.min())
                                        .from(product)
                                        .innerJoin(memberAdmin)
                                        .on(memberAdmin.id.eq(product.memberDealerId))
                                        .leftJoin(wishlist)
                                        .on(wishlist.productId.eq(product.id))
                                        .where(
                                                wishlist.purchaseInquiryId.eq(
                                                        JPAExpressions
                                                                .select(wishlist.purchaseInquiryId.max())
                                                                .from(wishlist)
                                                                .where(
                                                                        wishlist.purchaseInquiryId.eq(purchaseInquiry.id)
                                                                )
                                                )
                                        )
                                , "dealer"),
                        ExpressionUtils.as(JPAExpressions
                                        .select(Wildcard.count)
                                        .from(product)
                                        .innerJoin(memberAdmin)
                                        .on(memberAdmin.id.eq(product.memberDealerId))
                                        .leftJoin(wishlist)
                                        .on(wishlist.productId.eq(product.id))
                                        .where(
                                                wishlist.purchaseInquiryId.eq(
                                                        JPAExpressions
                                                                .select(wishlist.purchaseInquiryId.max())
                                                                .from(wishlist)
                                                                .where(
                                                                        wishlist.purchaseInquiryId.eq(purchaseInquiry.id)
                                                                )
                                                )
                                        )
                                , "dealerCount"),
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
                                        .select(inquiryDetail.content)
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
                                , "inquiryDetailContent")
                ))
                .from(purchaseInquiry)
                .leftJoin(member)
                .on(member.id.eq(purchaseInquiry.memberId))
                .where(
                        condInquiryTypeEq(condition.getType()),
                        condMultiFieldLike(condition.getField(), condition.getS()),
                        consultationCondition,
                        carNumberCondition,
                        dealerCondition,
                        counselorCondition,
                        condReservationDateBetween(condition.getSdate(), condition.getEdate()),
                        condDelYnEqN()
                )
                .orderBy(getSort(pageable))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        JPQLQuery<Long> countQuery = queryFactory
                .select(Wildcard.count)
                .from(purchaseInquiry)
                .leftJoin(member)
                .on(member.id.eq(purchaseInquiry.memberId))
                .where(
                        condInquiryTypeEq(condition.getType()),
                        condMultiFieldLike(condition.getField(), condition.getS()),
                        consultationCondition,
                        carNumberCondition,
                        dealerCondition,
                        counselorCondition,
                        condReservationDateBetween(condition.getSdate(), condition.getEdate()),
                        condDelYnEqN()
                );

        return PageableExecutionUtils.getPage(content, pageable, countQuery::fetchCount);
    }

    @Override
    public Page<PurchaseInquiryListDto> searchAllWithoutContactInquiryLists(PurchaseInquirySearchDto condition, Pageable pageable) {
        List<PurchaseInquiryListDto> content = queryFactory
                .select(Projections.fields(PurchaseInquiryListDto.class,
                        purchaseInquiry.id,
                        ExpressionUtils.as(JPAExpressions
                                        .select(product.carPlateNumber.min())
                                        .from(wishlist)
                                        .innerJoin(product)
                                        .on(product.id.eq(wishlist.productId))
                                        .where(
                                                wishlist.purchaseInquiryId.eq(
                                                        JPAExpressions
                                                                .select(wishlist.purchaseInquiryId.max())
                                                                .from(wishlist)
                                                                .where(
                                                                        wishlist.purchaseInquiryId.eq(purchaseInquiry.id)
                                                                )
                                                )
                                        )
                                , "carPlateNumber"),
                        purchaseInquiry.consultationTypeCode,
                        purchaseInquiry.connectionIp,
                        purchaseInquiry.createdAt
                ))
                .from(purchaseInquiry)
                .leftJoin(member)
                .on(member.id.eq(purchaseInquiry.memberId))
                .where(
                        condInquiryTypeEq(condition.getType()),
                        condDelYnEqN()
                )
                .orderBy(getSort(pageable))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        JPQLQuery<Long> countQuery = queryFactory
                .select(Wildcard.count)
                .from(purchaseInquiry)
                .leftJoin(member)
                .on(member.id.eq(purchaseInquiry.memberId))
                .where(
                        condInquiryTypeEq(condition.getType()),
                        condDelYnEqN()
                );

        return PageableExecutionUtils.getPage(content, pageable, countQuery::fetchCount);
    }

    @Override
    public List<Long> searchAllPurchaseInquiryCount() {
        Long contactCount = queryFactory
                .select(Wildcard.count)
                .from(purchaseInquiry)
                .where(
                        condInquiryTypeEq(InquiryType.CONTACTABLE.getCode()),
                        condDelYnEqN()
                )
                .fetchFirst();

        Long withoutCount = queryFactory
                .select(Wildcard.count)
                .from(purchaseInquiry)
                .where(
                        condInquiryTypeEq(InquiryType.WITHOUT_CONTACT.getCode()),
                        condDelYnEqN()
                )
                .fetchFirst();

        Long visitCount = queryFactory
                .select(Wildcard.count)
                .from(purchaseInquiry)
                .where(
                        condInquiryTypeEq(InquiryType.VISIT_RESERVATION.getCode()),
                        condDelYnEqN()
                )
                .fetchFirst();

        return Arrays.asList(contactCount, withoutCount, visitCount);
    }

    @Override
    public PurchaseInquiryMemberDto searchMemberInfoByPurchaseInquiryId(Long inquiryId) {
        return queryFactory
                .select(Projections.fields(PurchaseInquiryMemberDto.class,
                    purchaseInquiry.id,
                    purchaseInquiry.memberId,
                    purchaseInquiry.realName,
                    purchaseInquiry.mobileNumber,
                    purchaseInquiry.email,
                    member.realName.as("memberRealName"),
                    member.mobileNumber.as("memberMobileNumber"),
                    member.email.as("memberEmail"),
                    purchaseInquiry.consultationTypeCode
                ))
                .from(purchaseInquiry)
                .leftJoin(member)
                .on(member.id.eq(purchaseInquiry.memberId))
                .where(
                        condIdEq(inquiryId)
                )
                .fetchFirst();
    }

    @Override
    public PurchaseInquiryDetailDto searchPurchaseInquiryById(Long id) {
        return queryFactory
                .select(Projections.fields(PurchaseInquiryDetailDto.class,
                        purchaseInquiry.id,
                        purchaseInquiry.inquiryNumber,
                        ExpressionUtils.as(JPAExpressions
                                        .select(inquiryDetail.consultationStatus)
                                        .from(inquiryDetail)
                                        .where(
                                                inquiryDetail.id.eq(
                                                        JPAExpressions
                                                                .select(inquiryDetail.id.max())
                                                                .from(inquiryDetail)
                                                                .where(
                                                                        inquiryDetail.inquiryId.eq(id)
                                                                )
                                                ).and(inquiryDetail.tradeTypeCode.eq(TradeType.BUY))
                                        )
                                , "consultationStatus"),
                        purchaseInquiry.consultationTypeCode,
                        purchaseInquiry.memberId,
                        purchaseInquiry.realName,
                        purchaseInquiry.mobileNumber,
                        purchaseInquiry.email,
                        member.realName.as("memberRealName"),
                        member.mobileNumber.as("memberMobileNumber"),
                        member.email.as("memberEmail"),
                        purchaseInquiry.createdAt,
                        purchaseInquiry.visitReservationAt
                        ))
                .from(purchaseInquiry)
                .leftJoin(member)
                .on(member.id.eq(purchaseInquiry.memberId))
                .where(
                        condIdEq(id)
                        )
                .fetchFirst();
    }

    @Override
    public Long searchAllPurchaseInquiryCountByMain() {
        return queryFactory
                .select(Wildcard.count)
                .from(purchaseInquiry)
                .where(
                        condInquiryTypeEq(InquiryType.CONTACTABLE.getCode()),
                        condCreatedDateEqNow(),
                        condDelYnEqN()
                )
                .fetchOne();
    }

    @Override
    public List<InquiryDetailCountDto> searchAllPurchaseInquiryDetailCountByMain() {
        QInquiryDetail subInquiryDetail = new QInquiryDetail("subInquiryDetail");
        QInquiryDetail condition = new QInquiryDetail("condition");

        List<Tuple> subQueryResults = queryFactory
                .select(
                        subInquiryDetail.inquiryId,
                        subInquiryDetail.id.max()
                )
                .from(subInquiryDetail)
                .innerJoin(purchaseInquiry)
                .on(purchaseInquiry.id.eq(subInquiryDetail.inquiryId))
                .where(
                        subInquiryDetail.tradeTypeCode.eq(TradeType.BUY)
                                .and(subInquiryDetail.delYn.eq(YN.N))
                                .and(subInquiryDetail.createdAt.goe(LocalDate.now().atStartOfDay()))
                                .and(purchaseInquiry.delYn.eq(YN.N))
                )
                .groupBy(subInquiryDetail.inquiryId)
                .fetch();

        return queryFactory
                .select(Projections.fields(InquiryDetailCountDto.class,
                        inquiryDetail.consultationStatus,
                        inquiryDetail.consultationStatus.count().as("statusCount")
                        ))
                .from(condition)
                .join(inquiryDetail).on(condition.id.eq(inquiryDetail.id))
                .where(
                        condition.inquiryId.in(
                                subQueryResults.stream()
                                        .map(tuple -> tuple.get(subInquiryDetail.inquiryId))
                                        .collect(Collectors.toList())
                        ).and(
                                condition.id.in(
                                        subQueryResults.stream()
                                                .map(tuple -> tuple.get(subInquiryDetail.id.max()))
                                                .collect(Collectors.toList())
                                )
                        )
                )
                .groupBy(inquiryDetail.consultationStatus)
                .fetch();
    }

    @Override
    public List<PurchaseInquiryListDto> searchAllPurchaseInquiryByMain(PurchaseInquirySearchDto condition) {
        return queryFactory
                .select(Projections.fields(PurchaseInquiryListDto.class,
                        purchaseInquiry.id,
                        ExpressionUtils.as(JPAExpressions
                                        .select(product.carPlateNumber.min())
                                        .from(wishlist)
                                        .innerJoin(product)
                                        .on(product.id.eq(wishlist.productId))
                                        .where(
                                                wishlist.purchaseInquiryId.eq(
                                                        JPAExpressions
                                                                .select(wishlist.purchaseInquiryId.max())
                                                                .from(wishlist)
                                                                .where(
                                                                        wishlist.purchaseInquiryId.eq(purchaseInquiry.id)
                                                                )
                                                ).and(wishlist.delYn.eq(YN.N))
                                        )
                                , "carPlateNumber"),
                        ExpressionUtils.as(JPAExpressions
                                        .select(Wildcard.count)
                                        .from(wishlist)
                                        .where(
                                                wishlist.purchaseInquiryId.eq(
                                                        JPAExpressions
                                                                .select(wishlist.purchaseInquiryId.max())
                                                                .from(wishlist)
                                                                .where(
                                                                        wishlist.purchaseInquiryId.eq(purchaseInquiry.id)
                                                                )
                                                ).and(wishlist.delYn.eq(YN.N))
                                        )
                                , "wishCount"),
                        purchaseInquiry.realName,
                        purchaseInquiry.mobileNumber,
                        member.realName.as("memberRealName"),
                        member.mobileNumber.as("memberMobileNumber")
                ))
                .from(purchaseInquiry)
                .leftJoin(member)
                .on(member.id.eq(purchaseInquiry.memberId))
                .where(
                        condInquiryTypeEq(condition.getType()),
                        condDelYnEqN()
                )
                .orderBy(purchaseInquiry.id.desc())
                .limit(5)
                .fetch();
    }

    @Override
    public List<PurchaseInquiryListDto> searchAllContactableInquiryExcelLists(PurchaseInquirySearchDto condition) {
        BooleanExpression consultationCondition = getConsultationCondition(condition);
        BooleanExpression carNumberCondition = getCarNumberCondition(condition);
        BooleanExpression dealerCondition = getDealerCondition(condition);
        BooleanExpression counselorCondition = getCounselorCondition(condition);

        return queryFactory
                .select(Projections.fields(PurchaseInquiryListDto.class,
                        purchaseInquiry.id,
                        purchaseInquiry.inquiryNumber,
                        ExpressionUtils.as(JPAExpressions
                                        .select(product.carPlateNumber.min())
                                        .from(wishlist)
                                        .innerJoin(product)
                                        .on(product.id.eq(wishlist.productId))
                                        .where(
                                                wishlist.purchaseInquiryId.eq(
                                                        JPAExpressions
                                                                .select(wishlist.purchaseInquiryId.max())
                                                                .from(wishlist)
                                                                .where(
                                                                        wishlist.purchaseInquiryId.eq(purchaseInquiry.id)
                                                                )
                                                ).and(wishlist.delYn.eq(YN.N))
                                        )
                                , "carPlateNumber"),
                        ExpressionUtils.as(JPAExpressions
                                        .select(Wildcard.count)
                                        .from(wishlist)
                                        .where(
                                                wishlist.purchaseInquiryId.eq(
                                                        JPAExpressions
                                                                .select(wishlist.purchaseInquiryId.max())
                                                                .from(wishlist)
                                                                .where(
                                                                        wishlist.purchaseInquiryId.eq(purchaseInquiry.id)
                                                                )
                                                ).and(wishlist.delYn.eq(YN.N))
                                        )
                                , "wishCount"),
                        purchaseInquiry.consultationTypeCode,
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
                        purchaseInquiry.memberId,
                        purchaseInquiry.realName,
                        purchaseInquiry.mobileNumber,
                        purchaseInquiry.email,
                        member.realName.as("memberRealName"),
                        member.mobileNumber.as("memberMobileNumber"),
                        member.email.as("memberEmail"),
                        purchaseInquiry.visitReservationAt,
                        purchaseInquiry.createdAt,
                        ExpressionUtils.as(JPAExpressions
                                        .select(memberAdmin.realName.min())
                                        .from(product)
                                        .innerJoin(memberAdmin)
                                        .on(memberAdmin.id.eq(product.memberDealerId))
                                        .leftJoin(wishlist)
                                        .on(wishlist.productId.eq(product.id))
                                        .where(
                                                wishlist.purchaseInquiryId.eq(
                                                        JPAExpressions
                                                                .select(wishlist.purchaseInquiryId.max())
                                                                .from(wishlist)
                                                                .where(
                                                                        wishlist.purchaseInquiryId.eq(purchaseInquiry.id)
                                                                )
                                                )
                                        )
                                , "dealer"),
                        ExpressionUtils.as(JPAExpressions
                                        .select(Wildcard.count)
                                        .from(product)
                                        .innerJoin(memberAdmin)
                                        .on(memberAdmin.id.eq(product.memberDealerId))
                                        .leftJoin(wishlist)
                                        .on(wishlist.productId.eq(product.id))
                                        .where(
                                                wishlist.purchaseInquiryId.eq(
                                                        JPAExpressions
                                                                .select(wishlist.purchaseInquiryId.max())
                                                                .from(wishlist)
                                                                .where(
                                                                        wishlist.purchaseInquiryId.eq(purchaseInquiry.id)
                                                                )
                                                )
                                        )
                                , "dealerCount"),
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
                                        .select(inquiryDetail.content)
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
                                , "inquiryDetailContent")
                ))
                .from(purchaseInquiry)
                .leftJoin(member)
                .on(member.id.eq(purchaseInquiry.memberId))
                .where(
                        condInquiryTypeEq(condition.getType()),
                        condMultiFieldLike(condition.getField(), condition.getS()),
                        consultationCondition,
                        carNumberCondition,
                        dealerCondition,
                        counselorCondition,
                        condReservationDateBetween(condition.getSdate(), condition.getEdate()),
                        condDelYnEqN()
                )
                .orderBy(purchaseInquiry.id.desc())
                .fetch();
    }

    @Override
    public List<PurchaseInquiryListDto> searchAllWithoutContactInquiryExcelLists(PurchaseInquirySearchDto condition) {
        return queryFactory
                .select(Projections.fields(PurchaseInquiryListDto.class,
                        purchaseInquiry.id,
                        ExpressionUtils.as(JPAExpressions
                                        .select(product.carPlateNumber.min())
                                        .from(wishlist)
                                        .innerJoin(product)
                                        .on(product.id.eq(wishlist.productId))
                                        .where(
                                                wishlist.purchaseInquiryId.eq(
                                                        JPAExpressions
                                                                .select(wishlist.purchaseInquiryId.max())
                                                                .from(wishlist)
                                                                .where(
                                                                        wishlist.purchaseInquiryId.eq(purchaseInquiry.id)
                                                                )
                                                )
                                        )
                                , "carPlateNumber"),
                        purchaseInquiry.consultationTypeCode,
                        purchaseInquiry.connectionIp,
                        purchaseInquiry.createdAt
                ))
                .from(purchaseInquiry)
                .leftJoin(member)
                .on(member.id.eq(purchaseInquiry.memberId))
                .where(
                        condInquiryTypeEq(condition.getType()),
                        condDelYnEqN()
                )
                .orderBy(purchaseInquiry.id.desc())
                .fetch();
    }

    @Override
    public List<PurchaseInquiryListDto> searchAllVisitInquiryExcelLists(PurchaseInquirySearchDto condition) {
        return queryFactory
                .select(Projections.fields(PurchaseInquiryListDto.class,
                        purchaseInquiry.id,
                        ExpressionUtils.as(JPAExpressions
                                        .select(product.carPlateNumber.min())
                                        .from(wishlist)
                                        .innerJoin(product)
                                        .on(product.id.eq(wishlist.productId))
                                        .where(
                                                wishlist.purchaseInquiryId.eq(
                                                        JPAExpressions
                                                                .select(wishlist.purchaseInquiryId.max())
                                                                .from(wishlist)
                                                                .where(
                                                                        wishlist.purchaseInquiryId.eq(purchaseInquiry.id)
                                                                )
                                                ).and(wishlist.delYn.eq(YN.N))
                                        )
                                , "carPlateNumber"),
                        ExpressionUtils.as(JPAExpressions
                                        .select(Wildcard.count)
                                        .from(wishlist)
                                        .where(
                                                wishlist.purchaseInquiryId.eq(
                                                        JPAExpressions
                                                                .select(wishlist.purchaseInquiryId.max())
                                                                .from(wishlist)
                                                                .where(
                                                                        wishlist.purchaseInquiryId.eq(purchaseInquiry.id)
                                                                )
                                                ).and(wishlist.delYn.eq(YN.N))
                                        )
                                , "wishCount"),
                        purchaseInquiry.realName,
                        purchaseInquiry.mobileNumber,
                        purchaseInquiry.email,
                        member.realName.as("memberRealName"),
                        member.mobileNumber.as("memberMobileNumber"),
                        member.email.as("memberEmail"),
                        purchaseInquiry.visitReservationAt,
                        purchaseInquiry.createdAt,
                        new CaseBuilder()
                                .when(member.id.isNull()).then("비회원")
                                .otherwise("회원").as("userType") // 회원 구분 추가
                ))
                .from(purchaseInquiry)
                .leftJoin(member)
                .on(member.id.eq(purchaseInquiry.memberId))
                .where(
                        condInquiryTypeEq(condition.getType()),
                        condDelYnEqN()
                )
                .orderBy(purchaseInquiry.id.desc())
                .fetch();
    }

    /**
     * 상담 상태 검색 조건
     *
     * @author SungHa
     */
    public BooleanExpression getConsultationCondition(PurchaseInquirySearchDto condition) {
        if (condition.getStatus() == null) {
            return null;
        }

        return Expressions.booleanOperation(Ops.IS_NOT_NULL,
                JPAExpressions
                        .select(inquiryDetail.consultationStatus.min())
                        .from(inquiryDetail)
                        .where(
                                inquiryDetail.id.eq(
                                        JPAExpressions
                                                .select(inquiryDetail.id.max())
                                                .from(inquiryDetail)
                                                .where(
                                                        inquiryDetail.inquiryId.eq(purchaseInquiry.id)
                                                )
                                ).and(inquiryDetail.tradeTypeCode.eq(TradeType.BUY)),
                                condPurchaseConsultationStatusEq(condition.getStatus())
                        )
        );
    }

    /**
     * 구매 희망 차량 번호 검색 조건
     *
     * @author SungHa
     */
    public BooleanExpression getCarNumberCondition(PurchaseInquirySearchDto condition) {
        if ((!hasText(condition.getField()) || !hasText(condition.getS()))
                || (!"carNumber".equals(condition.getField()) && !"all".equals(condition.getField()))) {
            return null;
        }

        return Expressions.booleanOperation(Ops.IS_NOT_NULL,
                JPAExpressions
                        .select(product.carPlateNumber.min())
                        .from(wishlist)
                        .innerJoin(product)
                        .on(product.id.eq(wishlist.productId))
                        .where(
                                wishlist.purchaseInquiryId.eq(
                                        JPAExpressions
                                                .select(wishlist.purchaseInquiryId.max())
                                                .from(wishlist)
                                                .where(
                                                        wishlist.purchaseInquiryId.eq(purchaseInquiry.id)
                                                )
                                ),
                                condCarNumberLike(condition.getS())
                        )
        );
    }

    /**
     * 배정 딜러 검색 조건
     *
     * @author SungHa
     */
    public BooleanExpression getDealerCondition(PurchaseInquirySearchDto condition) {
        if ((!hasText(condition.getField()) && !hasText(condition.getS())) ||
            condition.getDealer() == null) {
            return null;
        }

        return Expressions.booleanOperation(Ops.IS_NOT_NULL,
                JPAExpressions
                        .select(memberAdmin.realName.min())
                        .from(product)
                        .innerJoin(memberAdmin)
                        .on(memberAdmin.id.eq(product.memberDealerId))
                        .leftJoin(wishlist)
                        .on(wishlist.productId.eq(product.id))
                        .where(
                                wishlist.purchaseInquiryId.eq(
                                                JPAExpressions
                                                .select(wishlist.purchaseInquiryId.max())
                                                .from(wishlist)
                                                        .where(
                                                        wishlist.purchaseInquiryId.eq(purchaseInquiry.id)
                                                        )
                                        ),
                                condDealerEq(condition.getDealer())
                        )
        );
    }

    /**
     * 상담원 검색 조건
     *
     * @author SungHa
     */
    public BooleanExpression getCounselorCondition(PurchaseInquirySearchDto condition) {
        if ((!hasText(condition.getField()) && !hasText(condition.getS())) ||
            condition.getCounselor() == null) {
            return null;
        }

        return Expressions.booleanOperation(Ops.IS_NOT_NULL,
                JPAExpressions
                        .select(memberAdmin.realName.min())
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
                                ),
                                condCounselorEq(condition.getCounselor())
                        )
        );
    }

    private Predicate condIdEq(Long id) {
        return id != null ? purchaseInquiry.id.eq(id) : null;
    }

    private Predicate condInquiryTypeEq(String inquiryType) {
        return hasText(inquiryType) ? purchaseInquiry.inquiryTypeCode.eq(InquiryType.ofCode(inquiryType)) : null;
    }

    private Predicate condPurchaseConsultationStatusEq(List<String> strPurchaseConsultationStatus) {
        BooleanBuilder builder = new BooleanBuilder();

        if (strPurchaseConsultationStatus == null || strPurchaseConsultationStatus.isEmpty()) {
            return builder;
        }

        for (String status : strPurchaseConsultationStatus) {
            ConsultationStatus consultationStatus = ConsultationStatus.ofCode(status);
            builder.or(inquiryDetail.consultationStatus.eq(consultationStatus));
        }

        return builder;
    }

    private Predicate condMultiFieldLike(String field, String s) {
        BooleanBuilder builder = new BooleanBuilder();
        if (!hasText(field) || !hasText(s)) return builder;

        switch (field) {
            case "all":
                builder.or(condNameLike(s));
                builder.or(condMemberNameLike(s));
                builder.or(condMobileNumberLike(s));
                builder.or(condMemberMobileNumberLike(s));
                builder.or(condMemberEmailLike(s));
                builder.or(condInquiryNumberLike(s));
                break;

            case "name":
                builder.or(condNameLike(s));
                builder.or(condMemberNameLike(s));
                break;

            case "mobileNumber":
                builder.or(condMobileNumberLike(s));
                builder.or(condMemberMobileNumberLike(s));
                break;

            case "email":
                builder.or(condEmailLike(s));
                builder.or(condMemberEmailLike(s));
                break;

            case "inquiryNumber":
                return condInquiryNumberLike(s);
        }

        return builder;
    }

    private Predicate condNameLike(String s) {
        return hasText(s) ? purchaseInquiry.realName.contains(s) : null;
    }

    private Predicate condMemberNameLike(String s) {
        return hasText(s) ? member.realName.contains(s) : null;
    }

    private Predicate condMobileNumberLike(String s) {
        return hasText(s) ? purchaseInquiry.mobileNumber.contains(s) : null;
    }

    private Predicate condMemberMobileNumberLike(String s) {
        return hasText(s) ? member.mobileNumber.contains(s) : null;
    }

    private Predicate condEmailLike(String s) {
        return hasText(s) ? purchaseInquiry.email.contains(s) : null;
    }

    private Predicate condMemberEmailLike(String s) {
        return hasText(s) ? member.email.contains(s) : null;
    }

    private Predicate condCarNumberLike(String s) {
        return hasText(s) ? product.carPlateNumber.contains(s) : null;
    }

    private Predicate condInquiryNumberLike(String s) {
        return hasText(s) ? purchaseInquiry.inquiryNumber.contains(s) : null;
    }

    private Predicate condDealerEq(Long dealerId) {
        BooleanBuilder builder = new BooleanBuilder();

        if (dealerId == null || String.valueOf(dealerId).isEmpty()) {
            return builder;
        }

        builder.or(product.memberDealerId.eq(dealerId));

        return builder;
    }

    private Predicate condCounselorEq(Long counselorId) {
        BooleanBuilder builder = new BooleanBuilder();

        if (counselorId == null || String.valueOf(counselorId).isEmpty()) {
            return builder;
        }

        builder.or(inquiryDetail.memberAdminId.eq(counselorId));

        return builder;
    }

    private Predicate condReservationDateBetween(String sdate, String edate) {
        BooleanBuilder builder = new BooleanBuilder();

        if (hasText(sdate))
            try {
                builder.and(purchaseInquiry.visitReservationAt.goe(LocalDate.parse(sdate).atStartOfDay()));
            } catch (DateTimeParseException ignored) {}

        if (hasText(edate))
            try {
                builder.and(purchaseInquiry.visitReservationAt.lt(LocalDate.parse(edate).atStartOfDay().plusDays(1L)));
            } catch (DateTimeParseException ignored) {}

        return builder;
    }

    private Predicate condCreatedDateEqNow() {
        return new BooleanBuilder().and(purchaseInquiry.createdAt.goe(LocalDate.now().atStartOfDay()));
    }

    private Predicate condDelYnEqN() {
        return new BooleanBuilder().and(purchaseInquiry.delYn.eq(YN.N));
    }

    private OrderSpecifier<?>[] getSort(Pageable pageable) {
        List<OrderSpecifier<?>> orderSpecifiers = new ArrayList<>();

        for (Sort.Order order : pageable.getSort()) {
            Order direction = order.isAscending() ? Order.ASC : Order.DESC;

            switch (order.getProperty()) {
                case "ip":
                    orderSpecifiers.add(new OrderSpecifier<>(direction, purchaseInquiry.connectionIp));
                    break;

                case "createdAt":
                    orderSpecifiers.add(new OrderSpecifier<>(direction, purchaseInquiry.createdAt));
                    break;

                case "visitReservationAt":
                    orderSpecifiers.add(new OrderSpecifier<>(direction, purchaseInquiry.visitReservationAt));
                    break;

                default:
                    orderSpecifiers.add(new OrderSpecifier<>(direction, purchaseInquiry.id));
                    break;
            }
        }

        return orderSpecifiers.toArray(OrderSpecifier[]::new);
    }

}
