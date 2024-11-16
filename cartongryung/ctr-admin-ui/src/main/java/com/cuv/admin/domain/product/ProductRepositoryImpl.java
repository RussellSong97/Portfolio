package com.cuv.admin.domain.product;

import com.cuv.admin.common.YN;
import com.cuv.admin.domain.linkcode.entity.QLinkCode;
import com.cuv.admin.domain.product.dto.ProductListDto;
import com.cuv.admin.domain.product.dto.ProductSearchDto;
import com.cuv.admin.domain.product.enumset.PostStatus;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.core.types.dsl.Wildcard;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.time.temporal.TemporalAdjusters;
import java.util.List;

import static com.cuv.admin.domain.memberadmin.entity.QMemberAdmin.memberAdmin;
import static com.cuv.admin.domain.product.entity.QProduct.product;
import static org.springframework.util.StringUtils.hasText;

@Repository
public class ProductRepositoryImpl implements ProductRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public ProductRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public Page<ProductListDto> searchAllPostingProductLists(ProductSearchDto condition, Pageable pageable) {
        QLinkCode maker = new QLinkCode("maker");
        QLinkCode model = new QLinkCode("model");
        QLinkCode detailGrade = new QLinkCode("detailGrade");

        List<ProductListDto> content = queryFactory
                .select(Projections.fields(ProductListDto.class,
                        product.id.as("productId"),
                        product.productUniqueNumber,
                        product.carPlateNumber,
                        maker.linkDataNm.as("makerName"),
                        model.linkDataNm.as("modelName"),
                        detailGrade.linkDataNm.as("detailGradeName"),
                        memberAdmin.realName
                        ))
                .from(product)
                .innerJoin(memberAdmin)
                .on(memberAdmin.id.eq(product.memberDealerId))
                .innerJoin(maker)
                .on(maker.id.eq(product.makerNumber))
                .innerJoin(model)
                .on(model.id.eq(product.modelNumber))
                .innerJoin(detailGrade)
                .on(detailGrade.id.eq(product.detailGradeNumber))
                .where(
                        condCarPlateNumberLike(condition.getS()),
                        condPostStatusEqPost(),
                        condDelYnEqN()
                )
                .orderBy(product.id.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        JPQLQuery<Long> countQuery = queryFactory
                .select(Wildcard.count)
                .from(product)
                .where(
                        condCarPlateNumberLike(condition.getS()),
                        condPostStatusEqPost(),
                        condDelYnEqN()
                );

        return PageableExecutionUtils.getPage(content, pageable, countQuery::fetchCount);
    }

    @Override
    public Page<ProductListDto> searchAllProduct(ProductSearchDto condition, Pageable pageable) {
        QLinkCode maker = new QLinkCode("maker");
        QLinkCode model = new QLinkCode("model");
        QLinkCode detailGrade = new QLinkCode("detailGrade");

        List<ProductListDto> content = queryFactory
                .select(Projections.fields(ProductListDto.class,
                        product.id.as("productId"),
                        product.productUniqueNumber,
                        product.carPlateNumber,
                        product.carImageUrl,
                        product.shopName,
                        maker.linkDataNm.as("makerName"),
                        model.linkDataNm.as("modelName"),
                        detailGrade.linkDataNm.as("detailGradeName"),
                        product.carRegYear,
                        product.carUseKm,
                        product.carAmountSale,
                        product.postStatus,
                        memberAdmin.realName,
                        product.city,
                        product.createdAt
                ))
                .from(product)
                .innerJoin(memberAdmin)
                .on(memberAdmin.id.eq(product.memberDealerId))
                .innerJoin(maker)
                .on(maker.id.eq(product.makerNumber))
                .innerJoin(model)
                .on(model.id.eq(product.modelNumber))
                .innerJoin(detailGrade)
                .on(detailGrade.id.eq(product.detailGradeNumber))
                .where(
                        condMultiFieldLike(condition.getField(), condition.getS(), maker, model, detailGrade),
                        condCreatedDateBetween(condition.getSdate(), condition.getEdate()),
                        condMultiCategory(condition.getMaker(), condition.getModel(), condition.getDetailModel(), condition.getDetailGrade()),
                        condPostStatusEq(condition.getStatus()),
                        condDealerEq(condition.getDealer()),
                        condShopNameLike(condition.getShopName()),
                        condRecommendType(condition.getType(), condition.getRecommendYn()),
                        condDelYnEqN()
                )
                .orderBy(getPostStatusOrder(), product.id.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        JPQLQuery<Long> countQuery = queryFactory
                .select(Wildcard.count)
                .from(product)
                .innerJoin(memberAdmin)
                .on(memberAdmin.id.eq(product.memberDealerId))
                .innerJoin(maker)
                .on(maker.id.eq(product.makerNumber))
                .innerJoin(model)
                .on(model.id.eq(product.modelNumber))
                .innerJoin(detailGrade)
                .on(detailGrade.id.eq(product.detailGradeNumber))
                .where(
                        condMultiFieldLike(condition.getField(), condition.getS(), maker, model, detailGrade),
                        condCreatedDateBetween(condition.getSdate(), condition.getEdate()),
                        condMultiCategory(condition.getMaker(), condition.getModel(), condition.getDetailModel(), condition.getDetailGrade()),
                        condPostStatusEq(condition.getStatus()),
                        condDealerEq(condition.getDealer()),
                        condShopNameLike(condition.getShopName()),
                        condRecommendType(condition.getType(), condition.getRecommendYn()),
                        condDelYnEqN()
                );

        return PageableExecutionUtils.getPage(content, pageable, countQuery::fetchCount);
    }

    @Override
    public List<ProductListDto> searchAllProduct(ProductSearchDto condition) {
        QLinkCode maker = new QLinkCode("maker");
        QLinkCode model = new QLinkCode("model");
        QLinkCode detailGrade = new QLinkCode("detailGrade");

        return queryFactory
                .select(Projections.fields(ProductListDto.class,
                        product.id.as("productId"),
                        product.productUniqueNumber,
                        product.carPlateNumber,
                        product.carImageUrl,
                        product.shopName,
                        maker.linkDataNm.as("makerName"),
                        model.linkDataNm.as("modelName"),
                        detailGrade.linkDataNm.as("detailGradeName"),
                        product.carRegYear,
                        product.carUseKm,
                        product.carAmountSale,
                        product.postStatus,
                        memberAdmin.realName,
                        product.city,
                        product.createdAt
                ))
                .from(product)
                .innerJoin(memberAdmin)
                .on(memberAdmin.id.eq(product.memberDealerId))
                .innerJoin(maker)
                .on(maker.id.eq(product.makerNumber))
                .innerJoin(model)
                .on(model.id.eq(product.modelNumber))
                .innerJoin(detailGrade)
                .on(detailGrade.id.eq(product.detailGradeNumber))
                .where(
                        condMultiFieldLike(condition.getField(), condition.getS(), maker, model, detailGrade),
                        condCreatedDateBetween(condition.getSdate(), condition.getEdate()),
                        condMultiCategory(condition.getMaker(), condition.getModel(), condition.getDetailModel(), condition.getDetailGrade()),
                        condPostStatusEq(condition.getStatus()),
                        condDealerEq(condition.getDealer()),
                        condShopNameLike(condition.getShopName()),
                        condRecommendType(condition.getType(), condition.getRecommendYn()),
                        condDelYnEqN()
                )
                .orderBy(product.id.desc())
                .fetch();
    }

    @Override
    public Long searchAllSoldOutProductCountByMain() {
        return queryFactory
                .select(Wildcard.count)
                .from(product)
                .where(
                        condPostStatusEqSoldOut(),
                        condSoldOutBetween(),
                        condDelYnEqN()
                )
                .fetchOne();
    }

    @Override
    public Long searchAllSalesByMain() {
        return queryFactory
                .select(product.carAmountSale.sum())
                .from(product)
                .where(
                        condPostStatusEqSoldOut(),
                        condSoldOutBetween(),
                        condDelYnEqN()
                )
                .fetchOne();
    }

    @Override
    public Long searchAllProductCountByMain() {
        return queryFactory
                .select(Wildcard.count)
                .from(product)
                .where(
                        condDelYnEqN()
                )
                .fetchOne();
    }

    @Override
    public List<ProductListDto> searchAllPostingProductExcelLists(ProductSearchDto condition) {
        QLinkCode maker = new QLinkCode("maker");
        QLinkCode model = new QLinkCode("model");
        QLinkCode detailGrade = new QLinkCode("detailGrade");

        return queryFactory
                .select(Projections.fields(ProductListDto.class,
                        product.id.as("productId"),
                        product.productUniqueNumber,
                        product.carPlateNumber,
                        product.carImageUrl,
                        product.shopName,
                        maker.linkDataNm.as("makerName"),
                        model.linkDataNm.as("modelName"),
                        detailGrade.linkDataNm.as("detailGradeName"),
                        memberAdmin.realName,
                        product.carRegYear,
                        product.carUseKm,
                        product.carAmountSale,
                        product.postStatus,
                        product.city,
                        product.createdAt
                ))
                .from(product)
                .innerJoin(memberAdmin)
                .on(memberAdmin.id.eq(product.memberDealerId))
                .innerJoin(maker)
                .on(maker.id.eq(product.makerNumber))
                .innerJoin(model)
                .on(model.id.eq(product.modelNumber))
                .innerJoin(detailGrade)
                .on(detailGrade.id.eq(product.detailGradeNumber))
                .where(
                        condMultiFieldLike(condition.getField(), condition.getS(), maker, model, detailGrade),
                        condCreatedDateBetween(condition.getSdate(), condition.getEdate()),
                        condMultiCategory(condition.getMaker(), condition.getModel(), condition.getDetailModel(), condition.getDetailGrade()),
                        condPostStatusEq(condition.getStatus()),
                        condDealerEq(condition.getDealer()),
                        condShopNameLike(condition.getShopName()),
                        condRecommendType(condition.getType(), condition.getRecommendYn()),
                        condDelYnEqN()
                )
                .orderBy(getPostStatusOrder(), product.id.desc())
                .fetch();
    }

    // 추천차량
    @Override
    public List<ProductListDto> searchRecommendProductExcelList(ProductSearchDto condition) {
            QLinkCode maker = new QLinkCode("maker");
            QLinkCode model = new QLinkCode("model");
            QLinkCode detailGrade = new QLinkCode("detailGrade");

            return queryFactory
                    .select(Projections.fields(ProductListDto.class,
                            product.id.as("productId"),
                            product.productUniqueNumber,
                            product.carPlateNumber,
                            product.carImageUrl,
                            product.shopName,
                            maker.linkDataNm.as("makerName"),
                            model.linkDataNm.as("modelName"),
                            detailGrade.linkDataNm.as("detailGradeName"),
                            memberAdmin.realName,
                            product.carRegYear,
                            product.carUseKm,
                            product.carAmountSale,
                            product.postStatus,
                            product.city,
                            product.createdAt
                    ))
                    .from(product)
                    .innerJoin(memberAdmin)
                    .on(memberAdmin.id.eq(product.memberDealerId))
                    .innerJoin(maker)
                    .on(maker.id.eq(product.makerNumber))
                    .innerJoin(model)
                    .on(model.id.eq(product.modelNumber))
                    .innerJoin(detailGrade)
                    .on(detailGrade.id.eq(product.detailGradeNumber))
                    .where(
                            // 게시된 거만 올리기
                            condMultiFieldLike(condition.getField(), condition.getS(), maker, model, detailGrade), // 키워드
                            condDealerEq(condition.getDealer()), // 딜러
                            condRecommendType(condition.getType(), condition.getRecommendYn()), // 추천 상태
                            condDelYnEqN(),
                            condPostStatusEqPost(),
                            condShopNameLike(condition.getShopName()) // 파는 회사 명
                    )
                    .orderBy(product.id.desc())
                    .fetch();
    }

    private static OrderSpecifier<Integer> getPostStatusOrder() {
        return new CaseBuilder()
                .when(product.postStatus.eq(PostStatus.SOLDOUT))
                .then(1)
                .otherwise(0)
                .asc();
    }

    private Predicate condPostStatusEqPost() {
        return new BooleanBuilder().and(product.postStatus.eq(PostStatus.POST));
    }

    private Predicate condPostStatusEqSoldOut() {
        return new BooleanBuilder().and(product.postStatus.eq(PostStatus.SOLDOUT));
    }

    private Predicate condSoldOutBetween() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime startOfDay = getStartOfCurrentMonth(now);
        LocalDateTime endOfDay = getEndOfCurrentMonth(now);

        return new BooleanBuilder().and(product.soldOutAt.between(startOfDay, endOfDay));
    }

    private LocalDateTime getStartOfCurrentMonth(LocalDateTime now) {
        return now.withDayOfMonth(1).withHour(0).withMinute(0).withSecond(0).withNano(0);
    }

    private LocalDateTime getEndOfCurrentMonth(LocalDateTime now) {
        return now.with(TemporalAdjusters.lastDayOfMonth()).withHour(23).withMinute(59).withSecond(59).withNano(999999999);
    }

    private Predicate condMultiFieldLike(String field, String s, QLinkCode maker, QLinkCode model, QLinkCode detailGrade) {
        BooleanBuilder builder = new BooleanBuilder();
        if (!hasText(field) || !hasText(s)) return builder;

        switch (field) {
            case "all":
                builder.or(condProductUniqueNumberLike(s));
                builder.or(condCarPlateNumberLike(s));
                builder.or(condShopNameLike(s));
                builder.or(condCarNameLike(s, maker, model, detailGrade));
                break;

            case "productUniqueNumber": return condProductUniqueNumberLike(s);
            case "carPlateNumber": return condCarPlateNumberLike(s);
            case "shopName": return condShopNameLike(s);
            case "carName": return condCarNameLike(s, maker, model, detailGrade);
        }

        return builder;
    }

    private Predicate condProductUniqueNumberLike(String s) {
        return hasText(s) ? product.productUniqueNumber.contains(s) : null;
    }

    private Predicate condCarPlateNumberLike(String s) {
        BooleanBuilder builder = new BooleanBuilder();
        if (!hasText(s)) return builder;

        return product.carPlateNumber.contains(s);
    }

    private Predicate condShopNameLike(String s) {
        return hasText(s) ? product.shopName.contains(s) : null;
    }

    private Predicate condCarNameLike(String s, QLinkCode maker, QLinkCode model, QLinkCode detailGrade) {
        return hasText(s) ? maker.linkDataNm.contains(s).or(model.linkDataNm.contains(s).or(detailGrade.linkDataNm.contains(s))) : null;
    }

    private Predicate condCreatedDateBetween(String sdate, String edate) {
        BooleanBuilder builder = new BooleanBuilder();

        if (hasText(sdate))
            try {
                builder.and(product.createdAt.goe(LocalDate.parse(sdate).atStartOfDay()));
            } catch (DateTimeParseException ignored) {
            }

        if (hasText(edate))
            try {
                builder.and(product.createdAt.lt(LocalDate.parse(edate).atStartOfDay().plusDays(1L)));
            } catch (DateTimeParseException ignored) {
            }

        return builder;
    }

    private Predicate condMultiCategory(Long maker, Long model, Long detailModel, Long detailGrade) {
        BooleanBuilder builder = new BooleanBuilder();

        if(maker != null && maker > 0)
            builder.and(product.makerNumber.eq(maker));

        if(model != null && model > 0)
            builder.and(product.modelNumber.eq(model));

        if(detailModel != null && detailModel > 0)
            builder.and(product.detailModelNumber.eq(detailModel));

        if(detailGrade != null && detailGrade > 0)
            builder.and(product.detailGradeNumber.eq(detailGrade));

        return builder;
    }

    private Predicate condPostStatusEq(List<String> strPostStatus) {
        BooleanBuilder builder = new BooleanBuilder();

        if (strPostStatus == null || strPostStatus.isEmpty()) {
            return builder;
        }

        for (String status : strPostStatus) {
            PostStatus postStatus = PostStatus.ofCode(status);
            builder.or(product.postStatus.eq(postStatus));
        }

        return builder;
    }

    private Predicate condDealerEq(Long dealerId) {
        BooleanBuilder builder = new BooleanBuilder();

        if (dealerId == null || String.valueOf(dealerId).isEmpty()) {
            return builder;
        }

        builder.or(product.memberDealerId.eq(dealerId));

        return builder;
    }

    private Predicate condRecommendType(String type, String yn) {
        BooleanBuilder builder = new BooleanBuilder();
        if (!hasText(type) || !hasText(yn)) return builder;

        return switch (type) {
            case "car" -> product.cartongryeongRecommendYn.eq(YN.ofYn(yn));
            case "value" -> product.bestValueRecommendYn.eq(YN.ofYn(yn));
            default -> builder;
        };

    }



    private Predicate condDelYnEqN() {
        return new BooleanBuilder().and(product.delYn.eq(YN.N));
    }



}
