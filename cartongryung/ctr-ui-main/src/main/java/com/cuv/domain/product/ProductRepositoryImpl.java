package com.cuv.domain.product;

import com.cuv.domain.product.enumset.ExteriorShape;
import com.querydsl.core.types.dsl.StringTemplate;
import com.cuv.common.YN;
import com.cuv.domain.linkcode.entity.QLinkCode;
import com.cuv.domain.member.enumset.MemberRole;
import com.cuv.domain.product.dto.*;
import com.cuv.domain.product.enumset.PostStatus;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.*;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.eclipse.angus.mail.imap.protocol.BODY;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.Year;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.cuv.domain.linkinfo.entity.QLinkInfo.linkInfo;
import static com.cuv.domain.linklisting.entity.QLinkListing.linkListing;
import static com.cuv.domain.linkoptioninfo.entity.QLinkOptionInfo.linkOptionInfo;
import static com.cuv.domain.linkspecinfo.entity.QLinkSpecInfo.linkSpecInfo;
import static com.cuv.domain.memberadmin.entity.QMemberAdmin.memberAdmin;
import static com.cuv.domain.pick.entity.QPick.pick;
import static com.cuv.domain.product.entity.QProduct.product;
import static org.springframework.util.StringUtils.hasText;

@Repository
public class ProductRepositoryImpl implements ProductRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public ProductRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }


    @Override
    public Page<ProductRecommendListDto> searchProductRecommendList(PageRequest request) {
        QLinkCode maker = new QLinkCode("maker");
        QLinkCode model = new QLinkCode("model");
        QLinkCode detailGrade = new QLinkCode("detailGrade");

        // 1. 추천 차량 리스트 조회
        List<ProductRecommendListDto> content;
        content = queryFactory
                .select(Projections.fields(ProductRecommendListDto.class,
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
                        product.city,
                        product.createdAt,
                        product.recommendYn,
                        pick.id.as("pickId"),
                        pick.delYn.as("pickDelYn")
                ))
                .from(product)
                .leftJoin(pick).on(pick.productId.eq(product.id))
                .innerJoin(maker).on(maker.id.eq(product.makerNumber))
                .innerJoin(model).on(model.id.eq(product.modelNumber))
                .innerJoin(detailGrade).on(detailGrade.id.eq(product.detailGradeNumber))
                .where(product.recommendYn.eq(YN.Y).and(product.delYn.eq(YN.N)))
                .orderBy(product.createdAt.desc())
                .offset(request.getOffset())
                .limit(request.getPageSize())
                .fetch();

        JPQLQuery<Long> contentQuery = queryFactory
                .select(Wildcard.count)
                .from(product)
                .innerJoin(maker).on(maker.id.eq(product.makerNumber))
                .innerJoin(model).on(model.id.eq(product.modelNumber))
                .innerJoin(detailGrade).on(detailGrade.id.eq(product.detailGradeNumber))
                .where(product.recommendYn.eq(YN.Y).and(product.delYn.eq(YN.N)));

        return PageableExecutionUtils.getPage(content, request, contentQuery::fetchCount);
    }


    @Override
    public ProductDetailDto searchProductDetail(Long id) {
        QLinkCode maker = new QLinkCode("maker");
        QLinkCode model = new QLinkCode("model");
        QLinkCode detailGrade = new QLinkCode("detailGrade");
        QLinkCode detailModel = new QLinkCode("detailModel");

        return queryFactory.select(Projections.fields(ProductDetailDto.class,
                        product.id.as("productId"),
                        product.productUniqueNumber,
                        product.carPlateNumber,
                        product.hits,
                        product.carImageUrl,
                        product.detailGradeNumber.as("productDetailGradeNumber"),
                        product.shopName,
                        maker.linkDataNm.as("makerName"),
                        model.linkDataNm.as("modelName"),
                        detailGrade.linkDataNm.as("detailGradeName"),
                        detailModel.linkDataNm.as("detailModelName"),
                        product.carRegYear,
                        product.carUseKm,
                        product.carAmountSale,
                        product.postStatus,
                        product.city,
                        product.recommendYn,
                        product.carFuel,
                        product.carColor,
                        detailGrade.linkDataNm,
                        product.carMission,
                        // 딜러 정보 select
                        memberAdmin.realName.as("dealerName"),
                        memberAdmin.mobileNumber,
                        memberAdmin.intro,
                        memberAdmin.employeeNumber,
                        memberAdmin.profileImageJson,
                        // 기본정보 select
                        linkInfo.gradeFuelRate,
                        linkInfo.tireSizeFront,
                        linkInfo.tireSizeBack,
                        linkInfo.carGradeNm,
                        linkInfo.carClassNbr,
                        linkInfo.enginesize,
                        linkInfo.istdTrans,
                        linkInfo.extShape,
                        linkInfo.person,
                        linkInfo.engineForm,
                        linkInfo.prye,
                        linkInfo.insptValidPdDe,
                        linkInfo.frstRegistDe,
                        linkSpecInfo.specImageId,
                        linkSpecInfo.specNm,
                        pick.count().as("pickCount"),
                        pick.productId.as("pickProductId"),
                        maker.afterServiceDate.as("afterServiceDate"),
                        linkListing.checkouturl,
                        linkListing.carContent
                ))
                .from(product)
                .innerJoin(maker)
                .on(maker.id.eq(product.makerNumber))
                .innerJoin(model)
                .on(model.id.eq(product.modelNumber))
                .innerJoin(detailGrade)
                .on(detailGrade.id.eq(product.detailGradeNumber))
                .innerJoin(detailModel)
                .on(detailModel.id.eq(product.detailModelNumber))
                .leftJoin(linkOptionInfo).on(linkOptionInfo.carGradeNbr.eq(product.detailGradeNumber))
                // 딜러 정보 조인
                .leftJoin(memberAdmin).on(memberAdmin.id.eq(product.memberDealerId).and(memberAdmin.role.eq(MemberRole.DEALER)))
                // 기본정보 조인
                .leftJoin(linkInfo).on(linkInfo.vin.eq(product.vehicleIdentificationNumber))
                // 제원 정보 조인
                .leftJoin(linkSpecInfo).on(linkSpecInfo.carGradeNbr.eq(product.detailGradeNumber))
                .leftJoin(pick).on(pick.productId.eq(product.id))
                .leftJoin(linkListing).on(product.vehicleIdentificationNumber.eq(linkListing.carFrameNo))
                .where(product.id.eq(id))
                .fetchOne();
    }

    // 픽 카운트 가져오기
    @Override
    public Long searchPickCount(Long id) {
        return queryFactory
                .select(Wildcard.count)
                .from(pick)
                .where(condProductIdOfPickEqProductId(id), pick.delYn.eq(YN.N))
                .fetchOne();
    }

    @Override
    public Long searchMobileProductCount(Map<String, Object> map) {
        List<String> carFuel = (List<String>) map.get("fuel");
        List<String> carMission = (List<String>) map.get("trans");
        List<String> carSido = (List<String>) map.get("sido");
        Map<String, Object> carAmountMap = (Map<String, Object>) map.get("priceRange");
        Long downPrice = Long.valueOf(carAmountMap.get("downPrice").toString());
        Long upPrice = Long.valueOf(carAmountMap.get("upPrice").toString());
        System.out.println("carAmountMap 확인하기 : " + carAmountMap);

        return queryFactory
                .select(product.id.count())
                .from(product)
                .where(product.carFuel.in(carFuel)
                        .and(product.carMission.in(carMission))
                        .and(product.carAmountSale.between(downPrice, upPrice))
                        .and(product.city.in(carSido))
                        .and(product.delYn.eq(YN.N)).and(product.postStatus.eq(PostStatus.POST)))
                .fetchFirst();
    }

    @Override
    public Long countByProductId() {
        return queryFactory.select(Wildcard.count).from(product).where(product.delYn.eq(YN.N).and(product.postStatus.eq(PostStatus.POST))).fetchFirst();
    }

    @Override
    public List<Long> searchExtShape(String engName) {

        return queryFactory.select(product.modelNumber)
                .from(product)
                .where(product.extShape.eq(ExteriorShape.ofEngName(engName)))
                .fetch();
    }


    @Override
    public List<ProductRecommendListDto> searchProductRecommendNoPageList() {
        QLinkCode maker = new QLinkCode("maker");
        QLinkCode model = new QLinkCode("model");
        QLinkCode detailGrade = new QLinkCode("detailGrade");

        List<ProductRecommendListDto> content;
        content = queryFactory
                .select(Projections.fields(ProductRecommendListDto.class,
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
                        product.city,
                        product.createdAt,
                        product.recommendYn
                ))
                .from(product)
                .innerJoin(maker)
                .on(maker.id.eq(product.makerNumber))
                .innerJoin(model)
                .on(model.id.eq(product.modelNumber))
                .innerJoin(detailGrade)
                .on(detailGrade.id.eq(product.detailGradeNumber))
                .where(product.recommendYn.eq(YN.Y).and(product.delYn.eq(YN.N)))
                .orderBy(product.createdAt.desc())
                .fetch();

        return content;
    }

    @Override
    public List<SpecGroupDto> searchProductLinkSpecList(Long productDetailGradeNumber) {

        StringTemplate specUomTemplate = Expressions.stringTemplate(
                "COALESCE({0}, '')", linkSpecInfo.specUom);
        StringTemplate specValueTemplate = Expressions.stringTemplate(
                "COALESCE({0}, '')", linkSpecInfo.specValue);
        List<ProductLinkSpecDto> result = queryFactory.select(Projections.fields(ProductLinkSpecDto.class,
                        linkSpecInfo.specCtgry,
                        linkSpecInfo.specNm,
                        linkSpecInfo.alphanumCtgry,
                        specUomTemplate.as("specUom"),
                        specValueTemplate.as("specValue")
                ))
                .from(linkSpecInfo)
                .where(linkSpecInfo.carGradeNbr.eq(productDetailGradeNumber))
                .fetch();

        Map<String, List<ProductLinkSpecDto>> groupedMap = result.stream()
                .collect(Collectors.groupingBy(ProductLinkSpecDto::getSpecCtgry));


        return groupedMap.entrySet().stream()
                .map(entry -> {
                    SpecGroupDto specGroupDto = new SpecGroupDto();
                    specGroupDto.setSpecCtgry(entry.getKey());
                    specGroupDto.setSpecList(entry.getValue());
                    return specGroupDto;
                }).collect(Collectors.toList());
    }

    @Override
    public List<SpecGroupDto> searchProductLinkOptionList(Long productDetailGradeNumber) {
        // Null이면 빈 문자열로 설정하는 표현식

        List<ProductLinkOptionDto> result = queryFactory.select(Projections.fields(ProductLinkOptionDto.class,
                        linkOptionInfo.carGradeNbr,
                        linkOptionInfo.optType,
                        linkOptionInfo.optPickType.as("optPickType"),
                        linkOptionInfo.optCtgry,
                        linkOptionInfo.optNm
                ))
                .from(linkOptionInfo)
                .where(linkOptionInfo.carGradeNbr.eq(productDetailGradeNumber)
                        .and(linkOptionInfo.optType.eq(1))
                        .and(linkOptionInfo.delYn.eq(YN.N)))
                .orderBy(linkOptionInfo.optType.asc(), linkOptionInfo.optCtgry.asc())
                .fetch();

        // 결과를 그룹화합니다.
        Map<String, List<ProductLinkOptionDto>> groupedMap = result.stream()
                .collect(Collectors.groupingBy(dto -> {
                    String optCtgry = dto.getOptCtgry();
                    return optCtgry != null ? optCtgry : "";
                }));

        return groupedMap.entrySet().stream()
                .map(entry -> {
                    SpecGroupDto specGroupDto = new SpecGroupDto();
                    specGroupDto.setSpecCtgry(entry.getKey());
                    specGroupDto.setOptionList(entry.getValue());
                    return specGroupDto;
                }).collect(Collectors.toList());
    }

    //올라 온 지 얼마 안된 매물
    @Override
    public List<ProductRecentCarListDto> searchProductRecentCarList() {
        QLinkCode maker = new QLinkCode("maker");
        QLinkCode model = new QLinkCode("model");
        QLinkCode detailGrade = new QLinkCode("detailGrade");

        return queryFactory.select(Projections.fields(ProductRecentCarListDto.class,
                        product.id.as("productId"),
                        product.carImageUrl,
                        maker.linkDataNm.as("makerName"),
                        model.linkDataNm.as("modelName"),
                        detailGrade.linkDataNm.as("detailGradeName"),
                        product.shopName,
                        product.carRegYear,
                        product.carUseKm,
                        product.carAmountSale,
                        product.postStatus,
                        product.city
                ))
                .from(product)
                .innerJoin(maker)
                .on(maker.id.eq(product.makerNumber))
                .innerJoin(model)
                .on(model.id.eq(product.modelNumber))
                .innerJoin(detailGrade)
                .on(detailGrade.id.eq(product.detailGradeNumber))
                .where(product.postStatus.eq(PostStatus.POST))
                .orderBy(product.createdAt.desc())
                .limit(8)
                .fetch();
    }

    //카통령 추천 차량
    @Override
    public List<ProductCuvRecommendListDto> searchProductCuvRecommendList() {
        QLinkCode maker = new QLinkCode("maker");
        QLinkCode model = new QLinkCode("model");
        QLinkCode detailGrade = new QLinkCode("detailGrade");

        return queryFactory.select(Projections.fields(ProductCuvRecommendListDto.class,
                        product.id.as("productId"),
                        product.carImageUrl,
                        maker.linkDataNm.as("makerName"),
                        model.linkDataNm.as("modelName"),
                        detailGrade.linkDataNm.as("detailGradeName"),
                        product.shopName,
                        product.carRegYear,
                        product.carUseKm,
                        product.carAmountSale,
                        product.postStatus,
                        product.city,
                        product.cartongryeongRecommendYn
                ))
                .from(product)
                .innerJoin(maker)
                .on(maker.id.eq(product.makerNumber))
                .innerJoin(model)
                .on(model.id.eq(product.modelNumber))
                .innerJoin(detailGrade)
                .on(detailGrade.id.eq(product.detailGradeNumber))
                .where(product.postStatus.eq(PostStatus.POST)
                        .and(product.cartongryeongRecommendYn.eq(YN.Y)))
                .orderBy(product.createdAt.desc())
                .limit(4)
                .fetch();
    }

    //가성비 추천 차량
    @Override
    public List<ProductBestValueRecommendListDto> searchProductBestValueRecommendList() {
        QLinkCode maker = new QLinkCode("maker");
        QLinkCode model = new QLinkCode("model");
        QLinkCode detailGrade = new QLinkCode("detailGrade");

        return queryFactory.select(Projections.fields(ProductBestValueRecommendListDto.class,
                        product.id.as("productId"),
                        product.carImageUrl,
                        maker.linkDataNm.as("makerName"),
                        model.linkDataNm.as("modelName"),
                        detailGrade.linkDataNm.as("detailGradeName"),
                        product.shopName,
                        product.carRegYear,
                        product.carUseKm,
                        product.carAmountSale,
                        product.postStatus,
                        product.city,
                        product.bestValueRecommendYn
                ))
                .from(product)
                .innerJoin(maker)
                .on(maker.id.eq(product.makerNumber))
                .innerJoin(model)
                .on(model.id.eq(product.modelNumber))
                .innerJoin(detailGrade)
                .on(detailGrade.id.eq(product.detailGradeNumber))
                .where(product.postStatus.eq(PostStatus.POST)
                        .and(product.bestValueRecommendYn.eq(YN.Y)))
                .orderBy(product.createdAt.desc())
                .limit(10)
                .fetch();
    }

    @Override
    public Long searchProductCount() {
        return queryFactory
                .select(Wildcard.count)
                .from(product)
                .where(
                        condPostStatusEqPost()
                )
                .fetchOne();
    }

    @Override
    public List<ProductListDto> searchProductTotalSearchList(ProductTotalSearchDto condition) {
        QLinkCode maker = new QLinkCode("maker");
        QLinkCode model = new QLinkCode("model");
        QLinkCode detailGrade = new QLinkCode("detailGrade");
        return queryFactory.select(Projections.fields(ProductListDto.class,
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
                        product.city,
                        product.createdAt,
                        product.recommendYn,
                        pick.id.as("pickId"),
                        pick.delYn.as("pickDelYn")
                ))
                .from(product)
                .leftJoin(pick).on(pick.productId.eq(product.id))
                .innerJoin(maker)
                .on(maker.id.eq(product.makerNumber))
                .innerJoin(model)
                .on(model.id.eq(product.modelNumber))
                .innerJoin(detailGrade)
                .on(detailGrade.id.eq(product.detailGradeNumber))
                .where(
                        product.delYn.eq(YN.N),
                        product.postStatus.eq(PostStatus.POST),
                        // 메인 keyword 검색
                        condMainSearchKeyword(condition.getMainKeyword()),
                        // 메인 차량 검색
                        condMainSearchCategory(condition.getMaker(), condition.getModel(), condition.getDetailModel()),
                        // 카테고리 선택시 검색
                        condSearchCategoryId(condition.getCategoryId(), maker, model, detailGrade)
                )
                .orderBy(product.createdAt.desc())
                .fetch();
    }

    @Override
    public List<ProductListDto> searchApiProduct(Map<String, Object> map) {
        System.out.println("map 데이터 확인 : " + map);
        Long categoryId1 = map.get("category1") != null ? Long.valueOf(map.get("category1").toString()) : null;
        Long categoryId2 = map.get("category2") != null ? Long.valueOf(map.get("category2").toString()) : null;
        String engName = map.get("engName") != null ? map.get("engName").toString() : "";
        List<Long> categoryId3 = (List<Long>) map.get("category3");
        Long categoryId = map.get("categoryId") != null ? Long.valueOf(map.get("categoryId").toString()) : 0L;

        // 나머지 코드와 동일
        System.out.println("카테고리 아이디 확인 하기 : " + categoryId);

        List<String> optionList = (List<String>) map.get("selectedOptions");
        List<String> sidoList = (List<String>) map.get("selectedSidos");
        List<String> selectedFuels = (List<String>) map.get("selectedFuels");
        List<String> selectedColors = (List<String>) map.get("selectedColors");
        String orderBy = map.get("sortBy") != null ? map.get("sortBy").toString() : null;
        String carPlateNumber = map.get("carPlateNumber") != null ? map.get("carPlateNumber").toString() : null;
        String startYear = map.containsKey("startYear") ? map.get("startYear").toString() : null;
        String endYear = map.containsKey("endYear") ? map.get("endYear").toString() : null;
        Long startPrice = map.get("startPrice") != null ? Long.valueOf(map.get("startPrice").toString()) : null;
        Long endPrice = map.get("endPrice") != null ? Long.valueOf(map.get("endPrice").toString()) : null;
        Long startKm = map.get("startKm") != null ? Long.valueOf(map.get("startKm").toString()) : null;
        Long endKm = map.get("endKm") != null ? Long.valueOf(map.get("endKm").toString()) : null;
        Long depth = map.get("depth") != null ? Long.valueOf(map.get("depth").toString()) : null;
        String keyword = map.get("keyword") != null ? String.valueOf(map.get("keyword")) : "";

        QLinkCode maker = new QLinkCode("maker");
        QLinkCode model = new QLinkCode("model");
        QLinkCode detailGrade = new QLinkCode("detailGrade");

        System.out.println("넘어온 categoryId : " + categoryId);

        List<String> transmissions = (List<String>) map.get("transmissions");

        BooleanBuilder builder = new BooleanBuilder();

        builder.and(product.delYn.eq(YN.N))
                .and(product.postStatus.eq(PostStatus.POST))
                .and(condApiSearchProductOption(optionList))
                .and(condSearchCategoryInfo(categoryId1, categoryId2, categoryId3, maker, model, detailGrade, engName))
                .and(condApiSearchSido(sidoList))
                .and(condApiSearchFuels(selectedFuels))
                .and(condApiSearchColors(selectedColors))
                .and(condApiSearchDate(startYear, endYear))
                .and(condApiSearchCarPlateNumber(carPlateNumber))
                .and(condApiSearchIstTrans(transmissions))
                .and(condApiSearchProductPrice(startPrice, endPrice))
                .and(condApiSearchProductCarUseKm(startKm, endKm))
                .and(condApiSearchProductNameKeyword(keyword, maker));

        return queryFactory.select(Projections.fields(ProductListDto.class,
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
                        product.city,
                        product.createdAt,
                        product.recommendYn,
                        pick.id.as("pickId"),
                        pick.delYn.as("pickDelYn"),
                        product.recommendYn
                ))
                .from(product)
                .leftJoin(pick).on(pick.productId.eq(product.id))
                .innerJoin(maker).on(maker.id.eq(product.makerNumber))
                .innerJoin(model).on(model.id.eq(product.modelNumber))
                .innerJoin(detailGrade).on(detailGrade.id.eq(product.detailGradeNumber))
                .where(builder)
                .orderBy(condApiSearchOrderBy(orderBy))
                .fetch();
    }

    private Predicate condApiSearchProductNameKeyword(String keyword, QLinkCode maker) {
        BooleanBuilder builder = new BooleanBuilder();

        if (hasText(keyword)) {
            builder.and(product.carPlateNumber.contains(keyword)).or(maker.linkDataNm.contains(keyword));
        }
        return builder;
    }

    private Predicate condSearchCategoryInfo(Long categoryId1, Long categoryId2, List<Long> categoryId3, QLinkCode maker, QLinkCode model, QLinkCode detailGrade, String engName) {
        System.out.println("검색 직전 engName : " + engName);
        System.out.println("categoryId2 : " + categoryId2);
        BooleanBuilder builder = new BooleanBuilder();

        // 1차 카테고리 조건
        if (engName != null && !engName.isEmpty()) {
            builder.and(product.extShape.eq(ExteriorShape.ofEngName(engName)));

            if (categoryId2 != null) {
                builder.and(product.modelNumber.eq(categoryId2));
            }

            // categoryId3 조건
            if (categoryId3 != null && !categoryId3.isEmpty()) {
                builder.and(product.detailGradeNumber.in(categoryId3));
            }
        } else {
            // 1차 카테고리 조건
            if (categoryId1 != null) {
                builder.and(maker.id.eq(categoryId1));
            }

            // 2차 카테고리 조건
            if (categoryId2 != null) {
                builder.and(maker.id.eq(categoryId1)
                        .and(model.id.eq(categoryId2)));
            }

            // 3차 카테고리 조건
            if (categoryId3 != null && !categoryId3.isEmpty()) {
                builder.and(maker.id.eq(categoryId1)
                        .and(model.id.eq(categoryId2))
                        .and(detailGrade.id.in(categoryId3)));
            }
        }

        return builder;
    }


    private Predicate condApiSearchProductPrice(Long startPrice, Long endPrice) {
        BooleanBuilder builder = new BooleanBuilder();

        if (startPrice != null && endPrice != null) {
            builder.and(product.carAmountSale.between(startPrice, endPrice));
        }

        return builder;
    }

    private Predicate condApiSearchProductCarUseKm(Long startUseKm, Long endUseKm) {
        BooleanBuilder builder = new BooleanBuilder();

        if (startUseKm != null && endUseKm != null) {
            builder.and(product.carUseKm.between(startUseKm, endUseKm));
        }

        return builder;
    }


    //     검색 페이지 orderBy 조건
    private OrderSpecifier<?>[] condApiSearchOrderBy(String orderBy) {
        List<OrderSpecifier<?>> orderSpecifiers = new ArrayList<>();

        // 기본값 설정
        if (orderBy == null || orderBy.isEmpty()) {
            orderBy = "recent";
        }

        switch (orderBy) {
            case "pick":
                orderSpecifiers.add(product.hits.desc());
                break;
            case "recent":
                orderSpecifiers.add(product.createdAt.desc());
                break; // 이곳에 break 추가
            case "downPrice":
                orderSpecifiers.add(product.carAmountSale.asc());
                break;
            case "upPrice":
                orderSpecifiers.add(product.carAmountSale.desc());
                break;
            default:
                orderSpecifiers.add(product.createdAt.desc());
                break;
        }

        return orderSpecifiers.toArray(new OrderSpecifier[0]);
    }

    private Predicate condApiSearchCarPlateNumber(String carPlateNumber) {
        BooleanBuilder builder = new BooleanBuilder();

        if (carPlateNumber != null) {
            builder.and(product.carPlateNumber.contains(carPlateNumber));
        }
        return builder;
    }

    private Predicate condApiSearchDate(String startDate, String endDate) {
        BooleanBuilder builder = new BooleanBuilder();

        // 문자열을 연도로 변환할 수 있는 부분을 정의
        StringPath carRegYear = product.carRegYear;

        // 연도 부분만 추출하여 정수로 변환하는 쿼리 템플릿 정의
        NumberTemplate<Integer> yearTemplate = Expressions.numberTemplate(Integer.class, "CAST({0} AS INTEGER)", carRegYear);

        try {
            if (startDate != null && !startDate.isEmpty()) {
                int startYear = Integer.parseInt(startDate);
                builder.and(yearTemplate.goe(startYear));
            }

            if (endDate != null && !endDate.isEmpty()) {
                int endYear = Integer.parseInt(endDate);
                builder.and(yearTemplate.loe(endYear));
            }

        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return builder;
    }


    private Predicate condApiSearchIstTrans(List<String> transmissions) {
        BooleanBuilder builder = new BooleanBuilder();
        if (transmissions != null && !transmissions.isEmpty()) {
            builder.and(product.carMission.in(transmissions));
        }
        return builder;
    }

    // 왼쪼 메뉴바 클릭
    private Predicate condApiSearchCategoryId(Long categoryId, QLinkCode maker, QLinkCode model, QLinkCode detailGrade, Long depth) {
        BooleanBuilder builder = new BooleanBuilder();

        // 카테고리 ID가 0일 경우 전체 검색을 위한 조건 추가
        if (categoryId != null && categoryId == 0) {
            return null;
        }

        if (categoryId != null) {
            if (depth == 1) {
                builder.and(maker.id.eq(categoryId));
            } else if (depth == 2) {
                builder.and(maker.id.eq(categoryId).and(model.id.eq(categoryId)));
            } else if (depth == 3) {
                builder.and(maker.id.eq(categoryId)
                        .and(model.id.eq(categoryId))
                        .and(detailGrade.id.eq(categoryId)));
            }
        }

        return builder;
    }

    // 메인에서 검색하고 들어올때
    private Predicate condSearchCategoryId(Long categoryId, QLinkCode maker, QLinkCode model, QLinkCode detailGrade) {
        BooleanBuilder builder = new BooleanBuilder();

        if (categoryId != null) {
            builder.and(maker.id.eq(categoryId)).and(model.id.eq(categoryId).and(detailGrade.id.eq(categoryId)));
        }
        return builder;
    }

    private Predicate condApiSearchSido(List<String> sidoList) {
        BooleanBuilder builder = new BooleanBuilder();
        if (sidoList != null && !sidoList.isEmpty()) {
            builder.and(product.city.in(sidoList));
        }
        return builder;
    }

    private Predicate condApiSearchFuels(List<String> selectedFuels) {
        BooleanBuilder builder = new BooleanBuilder();
        if (selectedFuels != null && !selectedFuels.isEmpty()) {
            builder.and(product.carFuel.in(selectedFuels));
        }
        return builder;
    }

    private Predicate condApiSearchProductOption(List<String> optionList) {
        BooleanBuilder builder = new BooleanBuilder();
        if (optionList != null && !optionList.isEmpty()) {
            builder.and(product.vehicleIdentificationNumber.in(optionList));
        }
        return builder;
    }

    private Predicate condApiSearchColors(List<String> selectedColors) {
        BooleanBuilder builder = new BooleanBuilder();
        if (selectedColors != null && !selectedColors.isEmpty()) {
            builder.and(product.carColor.in(selectedColors));
        }
        return builder;
    }


    private Predicate condPostStatusEqPost() {
        return product.postStatus.eq(PostStatus.POST);
    }

    private Predicate condProductIdOfPickEqProductId(Long id) {
        return pick.productId.eq(id);
    }

    // 메인 차량 검색 조건
    private Predicate condMainSearchCategory(Long maker, Long model, Long detailModel) {
        BooleanBuilder builder = new BooleanBuilder();

        if (maker != null) {
            builder.and(product.makerNumber.eq(maker));
        }

        if (model != null) {
            builder.and(product.modelNumber.eq(model));
        }

        if (detailModel != null) {
            builder.and(product.detailModelNumber.eq(detailModel));
        }

        return builder;
    }

    private Predicate condMainSearchKeyword(String keyword) {
        BooleanBuilder builder = new BooleanBuilder();

        if (hasText(keyword)) {
            builder.and(product.carPlateNumber.contains(keyword)
                    .or(product.shopName.contains(keyword))
                    .or(product.city.contains(keyword))
                    .or(product.productUniqueNumber.contains(keyword))
                    .or(product.carRegYear.contains(keyword))
                    .or(product.carFuel.contains(keyword))
                    .or(product.carColor.contains(keyword))
                    .or(product.carMission.contains(keyword)));
        }
        return builder;
    }

}
