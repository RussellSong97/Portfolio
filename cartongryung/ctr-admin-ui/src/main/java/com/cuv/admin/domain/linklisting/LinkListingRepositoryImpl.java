package com.cuv.admin.domain.linklisting;

import com.cuv.admin.common.YN;
import com.cuv.admin.domain.linklisting.dto.LinkListingListDto;
import com.cuv.admin.domain.linklisting.dto.LinkListingSearchDto;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.core.types.dsl.Wildcard;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;

import static com.cuv.admin.domain.linkinfo.entity.QLinkInfo.linkInfo;
import static com.cuv.admin.domain.linklisting.entity.QLinkListing.linkListing;
import static com.cuv.admin.domain.product.entity.QProduct.product;
import static org.springframework.util.StringUtils.hasText;

@Repository
public class LinkListingRepositoryImpl implements LinkListingRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    private final EntityManager entityManager;

    public LinkListingRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
        this.entityManager = em;
    }

    @Override
    public void clear() {
        entityManager.clear();
    }

    @Override
    @Transactional
    public void updateShopName(String oldName, String newName) {
        queryFactory
                .update(linkListing)
                .set(linkListing.shopName, newName)
                .where(linkListing.shopName.eq(oldName))
                .execute();
    }

    @Override
    @Transactional
    public void deleteAllByState() {
        queryFactory
                .update(product)
                .set(product.delYn, YN.Y)
                .where(
                        product.vehicleIdentificationNumber.in(
                                JPAExpressions.select(linkListing.carFrameNo)
                                        .from(linkListing)
                                        .where(linkListing.state.eq(2))
                        ))
                .execute();

        queryFactory
                .update(linkInfo)
                .set(linkInfo.delYn, YN.Y)
                .where(
                        linkInfo.vin.in(
                                JPAExpressions.select(linkListing.carFrameNo)
                                        .from(linkListing)
                                        .where(linkListing.state.eq(2))
                        ))
                .execute();

        queryFactory
                .update(linkListing)
                .set(linkListing.delYn, YN.Y)
                .set(linkListing.state, 4)
                .where(linkListing.state.eq(2))
                .execute();
    }

    @Override
    @Transactional
    public void revertState() {
        queryFactory
                .update(linkListing)
                .set(linkListing.state, 2)
                .where(linkListing.state.in(1, 3))
                .execute();
    }

    @Override
    public Page<LinkListingListDto> searchAllLinkage(LinkListingSearchDto condition, Pageable pageable) {
        BooleanExpression completeActive = getCompleteExpression();

        List<LinkListingListDto> content = queryFactory
                .select(Projections.fields(LinkListingListDto.class,
                        linkListing.id,
                        linkListing.carPlateNumber,
                        linkListing.carFrameNo,
                        linkInfo.brandNm,
                        linkInfo.repCarClassNm,
                        linkInfo.carClassNm,
                        linkInfo.carGradeNm,
                        linkListing.shopName,
                        linkListing.carRegYear,
                        linkListing.carUseKm,
                        linkListing.carAmountSale,
                        linkListing.createdAt,
                        completeActive.as("isCompleted")

                ))
                .from(linkListing)
                .leftJoin(linkInfo)
                .on(linkInfo.vin.eq(linkListing.carFrameNo))
                .where(
                        condMultiFieldLike(condition.getField(), condition.getS()),
                        condCreatedDateBetween(condition.getSdate(), condition.getEdate()),
                        condMultiCategory(condition.getMaker(), condition.getModel(), condition.getDetailModel(), condition.getDetailGrade()),
                        condIsCompleted(condition.getIsCompleted()),
                        condListedYnEqN(),
                        condDelYnEqN()
                )
                .orderBy(linkListing.updatedAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        JPQLQuery<Long> countQuery = queryFactory
                .select(Wildcard.count)
                .from(linkListing)
                .leftJoin(linkInfo)
                .on(linkInfo.vin.eq(linkListing.carFrameNo))
                .where(
                        condMultiFieldLike(condition.getField(), condition.getS()),
                        condCreatedDateBetween(condition.getSdate(), condition.getEdate()),
                        condMultiCategory(condition.getMaker(), condition.getModel(), condition.getDetailModel(), condition.getDetailGrade()),
                        condIsCompleted(condition.getIsCompleted()),
                        condListedYnEqN(),
                        condDelYnEqN()
                );

        return PageableExecutionUtils.getPage(content, pageable, countQuery::fetchCount);
    }

    /**
     * 데이터 존재 여부 검사
     *
     * @author SungHa
     */
    private static BooleanExpression getCompleteExpression() {
        return new CaseBuilder()
                .when(linkListing.carPlateNumber.isNotNull()
                        .and(linkListing.carFrameNo.isNotNull())
                        .and(linkInfo.brandNm.isNotNull())
                        .and(linkInfo.repCarClassNm.isNotNull())
                        .and(linkInfo.carClassNm.isNotNull())
                        .and(linkInfo.carGradeNm.isNotNull())
                        .and(linkListing.carRegYear.isNotNull())
                        .and(linkListing.carUseKm.isNotNull())
                        .and(linkListing.carAmountSale.isNotNull())
                )
                .then(true)
                .otherwise(false);
    }

    private Predicate condDelYnEqN() {
        return new BooleanBuilder().and(linkListing.delYn.eq(YN.N));
    }

    private Predicate condListedYnEqN() {
        return new BooleanBuilder().and(linkListing.listedYn.eq(YN.N));
    }

    private Predicate condIsCompleted(String isCompleted) {
        return hasText(isCompleted) ?
                linkListing.carPlateNumber.isNotNull()
                .and(linkListing.carFrameNo.isNotNull())
                .and(linkInfo.brandNm.isNotNull())
                .and(linkInfo.repCarClassNm.isNotNull())
                .and(linkInfo.carClassNm.isNotNull())
                .and(linkInfo.carGradeNm.isNotNull())
                .and(linkListing.carRegYear.isNotNull())
                .and(linkListing.carUseKm.isNotNull())
                .and(linkListing.carAmountSale.isNotNull()) : null;
    }

    private Predicate condMultiFieldLike(String field, String s) {
        BooleanBuilder builder = new BooleanBuilder();
        if (!hasText(field) || !hasText(s)) return builder;

        switch (field) {
            case "all":
                builder.or(condCarPlateNumberLike(s));
                builder.or(condCarFrameNoLike(s));
                builder.or(condShopNameLike(s));
                break;

            case "carPlateNumber": return condCarPlateNumberLike(s);
            case "carFrameNo": return condCarFrameNoLike(s);
            case "shopName": return condShopNameLike(s);
        }

        return builder;
    }

    private Predicate condCarPlateNumberLike(String s) {
        return hasText(s) ? linkListing.carPlateNumber.contains(s) : null;
    }

    private Predicate condCarFrameNoLike(String s) {
        return hasText(s) ? linkListing.carFrameNo.contains(s) : null;
    }

    private Predicate condShopNameLike(String s) {
        return hasText(s) ? linkListing.shopName.contains(s) : null;
    }

    private Predicate condCreatedDateBetween(String sdate, String edate) {
        BooleanBuilder builder = new BooleanBuilder();

        if (hasText(sdate))
            try {
                builder.and(linkListing.createdAt.goe(LocalDate.parse(sdate).atStartOfDay()));
            } catch (DateTimeParseException ignored) {
            }

        if (hasText(edate))
            try {
                builder.and(linkListing.createdAt.lt(LocalDate.parse(edate).atStartOfDay().plusDays(1L)));
            } catch (DateTimeParseException ignored) {
            }

        return builder;
    }

    private Predicate condMultiCategory(Long maker, Long model, Long detailModel, Long detailGrade) {
        BooleanBuilder builder = new BooleanBuilder();

        if(maker != null && maker > 0)
            builder.and(linkInfo.brandNbr.eq(maker));

        if(model != null && model > 0)
            builder.and(linkInfo.repCarClassNbr.eq(model));

        if(detailModel != null && detailModel > 0)
            builder.and(linkInfo.carClassNbr.eq(detailModel));

        if(detailGrade != null && detailGrade > 0)
            builder.and(linkInfo.carGradeNbr.eq(detailGrade));

        return builder;
    }

}
