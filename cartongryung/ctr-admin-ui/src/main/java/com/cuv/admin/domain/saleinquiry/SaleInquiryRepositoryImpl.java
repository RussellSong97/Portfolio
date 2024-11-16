package com.cuv.admin.domain.saleinquiry;

import com.cuv.admin.common.YN;
import com.cuv.admin.domain.inquirydetail.dto.InquiryDetailCountDto;
import com.cuv.admin.domain.inquirydetail.entity.QInquiryDetail;
import com.cuv.admin.domain.inquirydetail.enumset.TradeType;
import com.cuv.admin.domain.purchaseinquiry.enumset.ConsultationStatus;
import com.cuv.admin.domain.saleinquiry.dto.SaleInquiryDetailDto;
import com.cuv.admin.domain.saleinquiry.dto.SaleInquiryListDto;
import com.cuv.admin.domain.saleinquiry.dto.SaleInquirySearchDto;
import com.cuv.admin.domain.salevehicle.dto.SaleVehicleDetailDto;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.*;
import com.querydsl.core.types.dsl.BooleanExpression;
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
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.cuv.admin.domain.inquirydetail.entity.QInquiryDetail.inquiryDetail;
import static com.cuv.admin.domain.member.entity.QMember.member;
import static com.cuv.admin.domain.memberadmin.entity.QMemberAdmin.memberAdmin;
import static com.cuv.admin.domain.saleinquiry.entity.QSaleInquiry.saleInquiry;
import static com.cuv.admin.domain.salevehicle.entity.QSaleVehicle.saleVehicle;
import static org.springframework.util.StringUtils.hasText;

@Repository
public class SaleInquiryRepositoryImpl implements SaleInquiryRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public SaleInquiryRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public Page<SaleInquiryListDto> searchAllSaleInquiryLists(SaleInquirySearchDto condition, Pageable pageable) {
        BooleanExpression consultationCondition = getConsultationCondition(condition);

        List<SaleInquiryListDto> content = queryFactory
                .select(Projections.fields(SaleInquiryListDto.class,
                        saleInquiry.id,
                        saleInquiry.inquiryNumber,
                        saleInquiry.attachments,
                        saleVehicle.carPlateNumber,
                        ExpressionUtils.as(JPAExpressions
                                        .select(inquiryDetail.consultationStatus)
                                        .from(inquiryDetail)
                                        .where(
                                                inquiryDetail.id.eq(
                                                        JPAExpressions
                                                                .select(inquiryDetail.id.max())
                                                                .from(inquiryDetail)
                                                                .where(
                                                                        inquiryDetail.inquiryId.eq(saleInquiry.id)
                                                                )
                                                ).and(inquiryDetail.tradeTypeCode.eq(TradeType.SELL))
                                        )
                                , "consultationStatus"),
                        saleInquiry.memberId,
                        member.realName,
                        member.email,
                        member.mobileNumber,
                        saleInquiry.visitReservationAt,
                        saleInquiry.createdAt,
                        saleVehicle.paymentYn,
                        memberAdmin.realName.as("dealer"),
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
                                                                        inquiryDetail.inquiryId.eq(saleInquiry.id)
                                                                )
                                                ).and(inquiryDetail.tradeTypeCode.eq(TradeType.SELL))
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
                                                                        inquiryDetail.inquiryId.eq(saleInquiry.id)
                                                                )
                                                ).and(inquiryDetail.tradeTypeCode.eq(TradeType.SELL))
                                        )
                                , "inquiryDetailContent")
                ))
                .from(saleInquiry)
                .leftJoin(saleVehicle)
                .on(saleVehicle.id.eq(saleInquiry.saleVehicleId))
                .leftJoin(member)
                .on(member.id.eq(saleInquiry.memberId))
                .leftJoin(memberAdmin)
                .on(memberAdmin.id.eq(saleInquiry.memberDealerId))
                .where(
                        consultationCondition,
                        condMultiFieldLike(condition.getField(), condition.getS()),
                        condDealerEq(condition.getDealer()),
                        condCarNumberLike(condition.getCarNumber()),
                        condDelYnEqN()
                )
                .orderBy(getSort(pageable))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        JPQLQuery<Long> countQuery = queryFactory
                .select(Wildcard.count)
                .from(saleInquiry)
                .leftJoin(saleVehicle)
                .on(saleVehicle.id.eq(saleInquiry.saleVehicleId))
                .leftJoin(member)
                .on(member.id.eq(saleInquiry.memberId))
                .leftJoin(memberAdmin)
                .on(memberAdmin.id.eq(saleInquiry.memberDealerId))
                .where(
                        consultationCondition,
                        condMultiFieldLike(condition.getField(), condition.getS()),
                        condDealerEq(condition.getDealer()),
                        condCarNumberLike(condition.getCarNumber()),
                        condDelYnEqN()
                );

        return PageableExecutionUtils.getPage(content, pageable, countQuery::fetchCount);
    }

    @Override
    public SaleInquiryDetailDto searchSaleInquiryById(Long id) {
        return queryFactory
                .select(Projections.fields(SaleInquiryDetailDto.class,
                        saleInquiry.id,
                        saleInquiry.inquiryNumber,
                        saleVehicle.carPlateNumber,
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
                                                )
                                        )
                                , "consultationStatus"),
                        saleInquiry.memberId,
                        member.realName,
                        member.mobileNumber,
                        member.email,
                        saleInquiry.createdAt,
                        memberAdmin.id.as("memberDealerId"),
                        memberAdmin.realName.as("dealer"),
                        saleInquiry.visitReservationAt
                ))
                .from(saleInquiry)
                .leftJoin(saleVehicle)
                .on(saleVehicle.id.eq(saleInquiry.saleVehicleId))
                .leftJoin(member)
                .on(member.id.eq(saleInquiry.memberId))
                .leftJoin(memberAdmin)
                .on(memberAdmin.id.eq(saleInquiry.memberDealerId))
                .where(
                        condIdEq(id)
                )
                .fetchFirst();
    }

    @Override
    public SaleVehicleDetailDto searchSaleVehicleById(Long id) {
        return queryFactory
                .select(Projections.fields(SaleVehicleDetailDto.class,
                        saleInquiry.id,
                        saleVehicle.carPlateNumber,
                        saleVehicle.ownerName,
                        saleInquiry.attachments,
                        saleInquiry.createdAt
                ))
                .from(saleInquiry)
                .innerJoin(saleVehicle)
                .on(saleVehicle.id.eq(saleInquiry.saleVehicleId))
                .where(
                        condIdEq(id)
                )
                .fetchFirst();
    }

    @Override
    public Long searchAllSaleInquiryCountByMain() {
        return queryFactory
                .select(Wildcard.count)
                .from(saleInquiry)
                .where(
                        condCreatedDateEqNow(),
                        condDelYnEqN()
                )
                .fetchOne();
    }

    @Override
    public List<InquiryDetailCountDto> searchAllSaleInquiryDetailCountByMain() {
        QInquiryDetail subInquiryDetail = new QInquiryDetail("subInquiryDetail");
        QInquiryDetail condition = new QInquiryDetail("condition");

        List<Tuple> subQueryResults = queryFactory
                .select(
                        subInquiryDetail.inquiryId,
                        subInquiryDetail.id.max()
                )
                .from(subInquiryDetail)
                .innerJoin(saleInquiry)
                .on(saleInquiry.id.eq(subInquiryDetail.inquiryId))
                .where(
                        subInquiryDetail.tradeTypeCode.eq(TradeType.SELL)
                                .and(subInquiryDetail.delYn.eq(YN.N))
                                .and(subInquiryDetail.createdAt.goe(LocalDate.now().atStartOfDay()))
                                .and(saleInquiry.delYn.eq(YN.N))
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
    public List<SaleInquiryListDto> searchAllSaleInquiryByMain() {
        return queryFactory
                .select(Projections.fields(SaleInquiryListDto.class,
                        saleInquiry.id,
                        saleVehicle.carPlateNumber,
                        member.realName,
                        member.mobileNumber
                ))
                .from(saleInquiry)
                .leftJoin(saleVehicle)
                .on(saleVehicle.id.eq(saleInquiry.saleVehicleId))
                .leftJoin(member)
                .on(member.id.eq(saleInquiry.memberId))
                .where(
                        condDelYnEqN()
                )
                .limit(5)
                .fetch();
    }

    @Override
    public List<SaleInquiryListDto> searchAllSaleInquiryExcelLists(SaleInquirySearchDto condition) {
        BooleanExpression consultationCondition = getConsultationCondition(condition);

        return queryFactory
                .select(Projections.fields(SaleInquiryListDto.class,
                        saleInquiry.id,
                        saleInquiry.inquiryNumber,
                        saleInquiry.attachments,
                        saleVehicle.carPlateNumber,
                        ExpressionUtils.as(JPAExpressions
                                        .select(inquiryDetail.consultationStatus)
                                        .from(inquiryDetail)
                                        .where(
                                                inquiryDetail.id.eq(
                                                        JPAExpressions
                                                                .select(inquiryDetail.id.max())
                                                                .from(inquiryDetail)
                                                                .where(
                                                                        inquiryDetail.inquiryId.eq(saleInquiry.id)
                                                                )
                                                ).and(inquiryDetail.tradeTypeCode.eq(TradeType.SELL))
                                        )
                                , "consultationStatus"),
                        saleInquiry.memberId,
                        member.realName,
                        member.email,
                        member.mobileNumber,
                        saleInquiry.visitReservationAt,
                        saleInquiry.createdAt,
                        memberAdmin.realName.as("dealer"),
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
                                                                        inquiryDetail.inquiryId.eq(saleInquiry.id)
                                                                )
                                                ).and(inquiryDetail.tradeTypeCode.eq(TradeType.SELL))
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
                                                                        inquiryDetail.inquiryId.eq(saleInquiry.id)
                                                                )
                                                ).and(inquiryDetail.tradeTypeCode.eq(TradeType.SELL))
                                        )
                                , "inquiryDetailContent")
                ))
                .from(saleInquiry)
                .leftJoin(saleVehicle)
                .on(saleVehicle.id.eq(saleInquiry.saleVehicleId))
                .leftJoin(member)
                .on(member.id.eq(saleInquiry.memberId))
                .leftJoin(memberAdmin)
                .on(memberAdmin.id.eq(saleInquiry.memberDealerId))
                .where(
                        consultationCondition,
                        condMultiFieldLike(condition.getField(), condition.getS()),
                        condDealerEq(condition.getDealer()),
                        condCarNumberLike(condition.getCarNumber()),
                        condDelYnEqN()
                )
                .orderBy(saleInquiry.id.desc())
                .fetch();
    }

    /**
     * 상담 상태 검색 조건
     *
     * @author SungHa
     */
    public BooleanExpression getConsultationCondition(SaleInquirySearchDto condition) {
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
                                                        inquiryDetail.inquiryId.eq(saleInquiry.id)
                                                )
                                ).and(inquiryDetail.tradeTypeCode.eq(TradeType.SELL)),
                                condSaleConsultationStatusEq(condition.getStatus())
                        )
        );
    }

    private Predicate condIdEq(Long id) {
        return id != null ? saleInquiry.id.eq(id) : null;
    }

    private Predicate condSaleConsultationStatusEq(List<String> strSaleConsultationStatus) {
        BooleanBuilder builder = new BooleanBuilder();

        if (strSaleConsultationStatus == null || strSaleConsultationStatus.isEmpty()) {
            return builder;
        }

        for (String status : strSaleConsultationStatus) {
            ConsultationStatus saleConsultationStatus = ConsultationStatus.ofCode(status);
            builder.or(inquiryDetail.consultationStatus.eq(saleConsultationStatus));
        }

        return builder;
    }

    private Predicate condMultiFieldLike(String field, String s) {
        BooleanBuilder builder = new BooleanBuilder();
        if (!hasText(field) || !hasText(s)) return builder;

        switch (field) {
            case "all":
                builder.or(condNameLike(s));
                builder.or(condEmailLike(s));
                builder.or(condInquiryNumberLike(s));
                break;

            case "name":
                builder.or(condNameLike(s));
                break;

            case "email":
                builder.or(condEmailLike(s));
                break;

            case "inquiryNumber":
                return condInquiryNumberLike(s);
        }

        return builder;
    }

    private Predicate condNameLike(String s) {
        return hasText(s) ? member.realName.contains(s) : null;
    }

    private Predicate condEmailLike(String s) {
        return hasText(s) ? member.email.contains(s) : null;
    }

    private Predicate condInquiryNumberLike(String s) {
        return hasText(s) ? saleInquiry.inquiryNumber.contains(s) : null;
    }

    private Predicate condDealerEq(Long dealerId) {
        BooleanBuilder builder = new BooleanBuilder();

        if (dealerId == null || String.valueOf(dealerId).isEmpty()) {
            return builder;
        }

        builder.or(saleInquiry.memberDealerId.eq(dealerId));

        return builder;
    }

    private Predicate condCarNumberLike(String carNumber) {
        return hasText(carNumber) ? saleVehicle.carPlateNumber.contains(carNumber) : null;
    }

    private Predicate condCreatedDateEqNow() {
        return new BooleanBuilder().and(saleInquiry.createdAt.goe(LocalDate.now().atStartOfDay()));
    }

    private Predicate condDelYnEqN() {
        return new BooleanBuilder().and(saleInquiry.delYn.eq(YN.N));
    }

    private OrderSpecifier<?>[] getSort(Pageable pageable) {
        List<OrderSpecifier<?>> orderSpecifiers = new ArrayList<>();

        for (Sort.Order order : pageable.getSort()) {
            Order direction = order.isAscending() ? Order.ASC : Order.DESC;

            switch (order.getProperty()) {
                case "visitReservationAt":
                    orderSpecifiers.add(new OrderSpecifier<>(direction, saleInquiry.visitReservationAt));
                    break;

                case "createdAt":
                    orderSpecifiers.add(new OrderSpecifier<>(direction, saleInquiry.createdAt));
                    break;

                default:
                    orderSpecifiers.add(new OrderSpecifier<>(direction, saleInquiry.id));
                    break;
            }
        }

        return orderSpecifiers.toArray(OrderSpecifier[]::new);
    }

}
