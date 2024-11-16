package com.cuv.domain.product;

import com.cuv.common.YN;
import com.cuv.domain.linkcode.entity.QLinkCode;
import com.cuv.domain.member.enumset.MemberRole;
import com.cuv.domain.product.dto.*;
import com.cuv.domain.product.enumset.CarSize;
import com.cuv.domain.product.enumset.ExteriorShape;
import com.cuv.domain.product.enumset.PostStatus;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.StringTemplate;
import com.querydsl.core.types.dsl.*;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

import java.util.*;
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
                        Expressions.stringTemplate(
                                "JSON_UNQUOTE(JSON_EXTRACT({0}, '$[0].realUrl'))",
                                product.carImageUrl).as("carImageUrl"),
                        product.shopName,
                        maker.linkDataNm.as("makerName"),
                        model.linkDataNm.as("modelName"),
                        detailGrade.linkDataNm.as("detailGradeName"),
                        product.carRegYear,
                        product.carUseKm,
                        product.carAmountSale,
                        product.postStatus,
                        product.city,
                        product.createdAt
                ))
                .from(product)
                .innerJoin(maker).on(maker.id.eq(product.makerNumber))
                .innerJoin(model).on(model.id.eq(product.modelNumber))
                .innerJoin(detailGrade).on(detailGrade.id.eq(product.detailGradeNumber))
                .where(product.postStatus.eq(PostStatus.POST).and(product.shopName.startsWith("주식회사 디에스 오토")).and(product.delYn.eq(YN.N)))
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
                .where(product.postStatus.eq(PostStatus.POST).and(product.shopName.startsWith("주식회사 디에스 오토")).and(product.delYn.eq(YN.N)));

        return PageableExecutionUtils.getPage(content, request, contentQuery::fetchCount);
    }


    @Override
    public ProductDetailDto searchProductDetail(Long id) {
        QLinkCode maker = new QLinkCode("maker");
        QLinkCode model = new QLinkCode("model");
        QLinkCode detailGrade = new QLinkCode("detailGrade");
        QLinkCode detailModel = new QLinkCode("detailModel");

        return queryFactory
                .select(Projections.fields(ProductDetailDto.class,
                        product.id.as("productId"),
                        product.productUniqueNumber.as("productUniqueNumber"),
                        product.carPlateNumber.as("carPlateNumber"),
                        product.hits.as("hits"),
                        product.carImageUrl,
                        product.detailGradeNumber.as("productDetailGradeNumber"),
                        product.shopName.as("shopName"),
                        maker.linkDataNm.as("makerName"),
                        model.linkDataNm.as("modelName"),
                        detailGrade.linkDataNm.as("detailGradeName"),
                        detailModel.linkDataNm.as("detailModelName"),
                        product.carRegYear.as("carRegYear"),
                        product.carUseKm.as("carUseKm"),
                        product.carAmountSale.as("carAmountSale"),
                        product.postStatus.as("postStatus"),
                        product.city.as("city"),
                        product.carFuel.as("carFuel"),
                        product.carColor.as("carColor"),
                        detailGrade.linkDataNm.as("linkDataNm"),
                        product.carMission.as("carMission"),
                        // 딜러 정보 select
                        memberAdmin.realName.as("dealerName"),
                        memberAdmin.mobileNumber.as("mobileNumber"),
                        memberAdmin.intro.as("intro"),
                        memberAdmin.employeeNumber.as("employeeNumber"),
                        memberAdmin.profileImageJson,
                        // 기본정보 select
                        linkInfo.gradeFuelRate.as("gradeFuelRate"),
                        linkInfo.tireSizeFront.as("tireSizeFront"),
                        linkInfo.tireSizeBack.as("tireSizeBack"),
                        linkInfo.carGradeNm.as("carGradeNm"),
                        linkInfo.carClassNbr.as("carClassNbr"),
                        linkInfo.enginesize.as("enginesize"),
                        linkInfo.istdTrans.as("istdTrans"),
                        linkInfo.extShape.as("extShape"),
                        linkInfo.person.as("person"),
                        linkInfo.engineForm.as("engineForm"),
                        linkInfo.prye.as("prye"),
                        linkInfo.insptValidPdDe.as("insptValidPdDe"),
                        linkInfo.frstRegistDe.as("frstRegistDe"),
                        maker.afterServiceDate.as("afterServiceDate"),
                        linkListing.checkouturl.as("checkouturl"),
                        linkListing.carContent.as("carContent"),
                        Expressions.as(JPAExpressions
                                .select(Wildcard.count)
                                .from(pick)
                                .where(pick.productId.eq(product.id)), "pickCount")
                ))
                .from(product)
                .innerJoin(maker).on(maker.id.eq(product.makerNumber))
                .innerJoin(model).on(model.id.eq(product.modelNumber))
                .innerJoin(detailGrade).on(detailGrade.id.eq(product.detailGradeNumber))
                .innerJoin(detailModel).on(detailModel.id.eq(product.detailModelNumber))
                // 딜러 정보 조인
                .leftJoin(memberAdmin).on(memberAdmin.id.eq(product.memberDealerId).and(memberAdmin.role.eq(MemberRole.DEALER)))
                // 기본정보 조인
                .leftJoin(linkInfo).on(linkInfo.vin.eq(product.vehicleIdentificationNumber))
                // 제원 정보 조인
                .leftJoin(linkSpecInfo).on(linkSpecInfo.carGradeNbr.eq(product.detailGradeNumber))
                .leftJoin(linkListing).on(product.vehicleIdentificationNumber.eq(linkListing.carFrameNo))
                .where(product.id.eq(id))
                .fetchFirst();
//                .fetchOne();
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


        // 랜덤으로 정렬하여 10개 선택
        List<ProductRecommendListDto> content = queryFactory
                .select(Projections.fields(ProductRecommendListDto.class,
                        product.id.as("productId"),
                        product.productUniqueNumber,
                        product.carPlateNumber,
                        Expressions.stringTemplate(
                                "JSON_UNQUOTE(JSON_EXTRACT({0}, '$[0].realUrl'))",
                                product.carImageUrl).as("carImageUrl"),
                        product.shopName,
                        maker.linkDataNm.as("makerName"),
                        model.linkDataNm.as("modelName"),
                        detailGrade.linkDataNm.as("detailGradeName"),
                        product.carRegYear,
                        product.carUseKm,
                        product.carAmountSale,
                        product.postStatus,
                        product.city,
                        product.createdAt
                ))
                .from(product)
                .innerJoin(maker)
                .on(maker.id.eq(product.makerNumber))
                .innerJoin(model)
                .on(model.id.eq(product.modelNumber))
                .innerJoin(detailGrade)
                .on(detailGrade.id.eq(product.detailGradeNumber))
                .where(product.postStatus.eq(PostStatus.POST)
                        .and(product.shopName.startsWith("주식회사 디에스 오토"))
                        .and(product.delYn.eq(YN.N)))
                .orderBy(Expressions.numberTemplate(Double.class, "RAND()").asc()) // 랜덤 정렬
                .limit(10) // 10개만 선택
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

    // 실시간 인기 차량
    @Override
    public List<ProductHitCarListDto> searchProductHitCarList() {
        QLinkCode maker = new QLinkCode("maker");
        QLinkCode model = new QLinkCode("model");
        QLinkCode detailGrade = new QLinkCode("detailGrade");

        return queryFactory.select(Projections.fields(ProductHitCarListDto.class,
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
                .where(product.postStatus.eq(PostStatus.POST)
                        .and(product.delYn.eq(YN.Y)))
                .orderBy(product.hits.desc())
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
    public Page<ProductListDto> searchProductTotalSearchList(ProductTotalSearchDto condition, Pageable request) {
        QLinkCode maker = new QLinkCode("maker");
        QLinkCode model = new QLinkCode("model");
        QLinkCode detailGrade = new QLinkCode("detailGrade");

        List<ProductListDto> content = queryFactory.select(Projections.fields(ProductListDto.class,
                        product.id.as("productId"),
                        product.productUniqueNumber.as("productUniqueNumber"),
                        product.carPlateNumber.as("carPlateNumber"),
                        product.carImageUrl,
                        product.shopName.as("shopName"),
                        maker.linkDataNm.as("makerName"),
                        model.linkDataNm.as("modelName"),
                        detailGrade.linkDataNm.as("detailGradeName"),
                        product.carRegYear.as("carRegYear"),
                        product.carUseKm.as("carUseKm"),
                        product.carAmountSale.as("carAmountSale"),
                        product.postStatus.as("postStatus"),
                        product.city.as("city"),
                        product.createdAt.as("createdAt")
                ))
                .from(product)
                .innerJoin(maker)
                .on(maker.id.eq(product.makerNumber))
                .innerJoin(model)
                .on(model.id.eq(product.modelNumber))
                .innerJoin(detailGrade)
                .on(detailGrade.id.eq(product.detailGradeNumber))
                .where(
                        product.postStatus.eq(PostStatus.POST),
                        product.delYn.eq(YN.N)
                )
                .orderBy(product.createdAt.desc())
                .limit(request.getPageSize())
                .offset(request.getOffset())
                .fetch();

        JPQLQuery<Long> contentQuery = queryFactory
                .select(Wildcard.count)
                .from(product)
                .innerJoin(maker)
                .on(maker.id.eq(product.makerNumber))
                .innerJoin(model)
                .on(model.id.eq(product.modelNumber))
                .innerJoin(detailGrade)
                .on(detailGrade.id.eq(product.detailGradeNumber))
                .where(
                        product.postStatus.eq(PostStatus.POST),
                        product.delYn.eq(YN.N)
                );

        return PageableExecutionUtils.getPage(content, request, contentQuery::fetchCount);
    }

    @Override
    public Page<ProductListDto> searchApiProduct(Map<String, String> map) {
        // 모델 아이디
        Long categoryId1 = getLongFromMap(map, "category1");
        // 메이커 아이디
        Long categoryId2 = getLongFromMap(map, "category2");
        // 제조사?
        String engName = getStringFromMap(map, "engName");
        // 카테고리 ID 목록 (CSV 형식으로 전달된다고 가정)
        List<Long> categoryId3 = parseLongListFromString(getStringFromMap(map, "category3"));
        // 선택한 옵션들 (CSV 형식으로 전달된다고 가정)
        List<Long> optionList = parseLongListFromString(getStringFromMap(map, "option"));
        // 선택한 지역 (CSV 형식으로 전달된다고 가정)
        List<String> sidoList = parseStringListFromString(getStringFromMap(map, "sido"));
        // 선택한 연료 (CSV 형식으로 전달된다고 가정)
        List<String> selectedFuels = parseStringListFromString(getStringFromMap(map, "fuel"));
        // 선택한 색상 (CSV 형식으로 전달된다고 가정)
        List<String> selectedColors = parseStringListFromString(getStringFromMap(map, "color"));

        // orderBy 조건
        String orderBy = getStringFromMap(map, "sortBy");
        // 차량 번호
        String carPlateNumber = getStringFromMap(map, "carPlateNumber");
        // 시작 년도
        String startYear = getStringFromMap(map, "startYear");
        // 끝 년도
        String endYear = getStringFromMap(map, "endYear");
        // 시작 금액
        Long startPrice = getLongFromMap(map, "startPrice");
        // 끝 금액
        Long endPrice = getLongFromMap(map, "endPrice");
        // 시작 주행거리
        Long startKm = getLongFromMap(map, "startKm");
        // 끝 주행거리
        Long endKm = getLongFromMap(map, "endKm");
        // 키워드 (차량 이름, 번호)
        String keyword = getStringFromMap(map, "keyword");

        String shapeType = getStringFromMap(map, "shapeType");
        // 페이지네이션 매개변수
        int page = getIntFromMap(map, "page", 1); // 현재 페이지, 기본값 1
        int size = getIntFromMap(map, "size", 6); // 페이지당 데이터 수, 기본값 6
        int offset = (page - 1) * size; // 데이터 시작 위치

        QLinkCode maker = new QLinkCode("maker");
        QLinkCode model = new QLinkCode("model");
        QLinkCode detailModel = new QLinkCode("detailModel");
        QLinkCode detailGrade = new QLinkCode("detailGrade");

        List<String> transmissions = parseStringListFromString(getStringFromMap(map, "transmissions"));

        BooleanBuilder builder = new BooleanBuilder();

        builder.and(product.delYn.eq(YN.N))
                .and(product.postStatus.eq(PostStatus.POST))
                .and(condApiSearchProductOption(optionList))
                .and(condSearchCategoryInfo(categoryId1, categoryId2, categoryId3, maker, model, detailModel, engName, shapeType))
                .and(condApiSearchSido(sidoList))
                .and(condApiSearchFuels(selectedFuels))
                .and(condApiSearchColors(selectedColors))
                .and(condApiSearchDate(startYear, endYear))
                .and(condApiSearchCarPlateNumber(carPlateNumber))
                .and(condApiSearchIstTrans(transmissions))
                .and(condApiSearchProductPrice(startPrice, endPrice))
                .and(condApiSearchProductCarUseKm(startKm, endKm))
                .and(condApiSearchProductNameKeyword(keyword, maker, model, detailModel, detailGrade));

        // 별도의 조건 처리: condSearchPcKeyword
        String pcKeyword = getStringFromMap(map, "pcKeyword");
        BooleanBuilder pcKeywordBuilder = new BooleanBuilder();
        pcKeywordBuilder.or(condSearchPcKeyword(pcKeyword,maker, model,detailModel, detailGrade));

        // 기본 검색 조건에 pcKeywordBuilder 조건 추가
        BooleanBuilder finalBuilder = new BooleanBuilder();
        finalBuilder.and(builder)
                .and(pcKeywordBuilder); // pcKeyword 조건은 별도로 추가

        List<ProductListDto> content = queryFactory.select(
                Projections.fields(ProductListDto.class,
                        product.id.as("productId"),
                        product.productUniqueNumber.as("productUniqueNumber"),
                        product.carPlateNumber.as("carPlateNumber"),
                        product.carImageUrl,
                        product.shopName.as("shopName"),
                        maker.linkDataNm.as("makerName"),
                        model.linkDataNm.as("modelName"),
                        detailGrade.linkDataNm.as("detailGradeName"),
                        product.carRegYear.as("carRegYear"),
                        product.carUseKm.as("carUseKm"),
                        product.carAmountSale.as("carAmountSale"),
                        product.postStatus.as("postStatus"),
                        product.city.as("city"),
                        product.createdAt.as("createdAt")
                ))
                .from(product)
                .innerJoin(maker).on(maker.id.eq(product.makerNumber))
                .innerJoin(model).on(model.id.eq(product.modelNumber))
                .innerJoin(detailModel).on(detailModel.id.eq(product.detailModelNumber))
                .innerJoin(detailGrade).on(detailGrade.id.eq(product.detailGradeNumber))
                .where(finalBuilder)
                .orderBy(condApiSearchOrderBy(orderBy))
                .offset(offset) // 페이지네이션 오프셋
                .limit(size) // 페이지당 데이터 수
                .fetch();

        JPQLQuery<Long> contentQuery = queryFactory
                .select(Wildcard.count)
                .from(product)
                .innerJoin(maker).on(maker.id.eq(product.makerNumber))
                .innerJoin(model).on(model.id.eq(product.modelNumber))
                .innerJoin(detailModel).on(detailModel.id.eq(product.detailModelNumber))
                .innerJoin(detailGrade).on(detailGrade.id.eq(product.detailGradeNumber))
                .where(finalBuilder);

        return PageableExecutionUtils.getPage(content, PageRequest.of(page - 1, size), contentQuery::fetchCount);
    }

    // Map<String, String>에서 Long 값 추출
    private Long getLongFromMap(Map<String, String> map, String key) {
        String value = map.get(key);
        if (value == null || value.trim().isEmpty()) {
            return null; // 값이 없거나 빈 문자열일 경우 null 반환
        }
        try {
            return Long.valueOf(value);
        } catch (NumberFormatException e) {
            System.err.println("Invalid number format for key: " + key + ", value: " + value);
            return null; // 잘못된 숫자 형식일 경우 null 반환
        }
    }

    // Map<String, String>에서 String 값 추출
    private String getStringFromMap(Map<String, String> map, String key) {
        String value = map.get(key);
        return value != null ? value.trim() : ""; // 값이 없을 경우 빈 문자열 반환
    }

    // Map<String, String>에서 String 값 추출
//    private String getStringFromMap(Map<String, String> map, String key) {
//        return map.get(key);
//    }

    // Map<String, String>에서 int 값 추출
    private int getIntFromMap(Map<String, String> map, String key, int defaultValue) {
        String value = map.get(key);
        return value != null ? Integer.parseInt(value) : defaultValue;
    }

    // CSV 문자열을 Long 리스트로 변환
    private List<Long> parseLongListFromString(String value) {
        if (value == null || value.isEmpty()) {
            return Collections.emptyList();
        }
        return Arrays.stream(value.split(","))
                .map(String::trim)
                .map(Long::valueOf)
                .collect(Collectors.toList());
    }

    // CSV 문자열을 String 리스트로 변환
    private List<String> parseStringListFromString(String value) {
        if (value == null || value.isEmpty()) {
            return Collections.emptyList();
        }
        return Arrays.stream(value.split(","))
                .map(String::trim)
                .collect(Collectors.toList());
    }

    @Override
    public List<ProductRecommendListDto> searchApiRecommendProduct(Map<String, Object> map) {
        // 페이지네이션 매개변수
        int page = map.get("page") != null ? Integer.parseInt(map.get("page").toString()) : 1; // 현재 페이지, 기본값 1
        int size = map.get("size") != null ? Integer.parseInt(map.get("size").toString()) : 20; // 페이지당 데이터 수, 기본값 20

        int offset = (page - 1) * size; // 데이터 시작 위치

        QLinkCode maker = new QLinkCode("maker");
        QLinkCode model = new QLinkCode("model");
        QLinkCode detailGrade = new QLinkCode("detailGrade");

        return queryFactory
                .select(Projections.fields(ProductRecommendListDto.class,
                        product.id.as("productId"),
                        product.productUniqueNumber,
                        product.carPlateNumber,
                        Expressions.stringTemplate(
                                "JSON_UNQUOTE(JSON_EXTRACT({0}, '$[0].realUrl'))",
                                product.carImageUrl).as("carImageUrl"),
                        product.shopName,
                        maker.linkDataNm.as("makerName"),
                        model.linkDataNm.as("modelName"),
                        detailGrade.linkDataNm.as("detailGradeName"),
                        product.carRegYear,
                        product.carUseKm,
                        product.carAmountSale,
                        product.postStatus,
                        product.city,
                        product.createdAt
                ))
                .from(product)
                .innerJoin(maker).on(maker.id.eq(product.makerNumber))
                .innerJoin(model).on(model.id.eq(product.modelNumber))
                .innerJoin(detailGrade).on(detailGrade.id.eq(product.detailGradeNumber))
                .where(product.postStatus.eq(PostStatus.POST)
                        .and(product.shopName.startsWith("주식회사 디에스 오토"))
                        .and(product.delYn.eq(YN.N))
                )
                .orderBy(product.createdAt.desc())
                .offset(offset) // 페이지네이션 오프셋
                .limit(size) // 페이지당 데이터 수
                .fetch();

    }

    @Override
    public List<Long> searchCarSize(String engName) {
        CarSize code = CarSize.ofEngName(engName);
        return queryFactory.select(product.modelNumber)
                .from(product)
                .where(product.carSize.eq(CarSize.ofEngName(engName)))
                .fetch();
    }

    private Predicate condSearchPcKeyword(String pcKeyword, QLinkCode maker, QLinkCode model, QLinkCode detailModel, QLinkCode detailGrade) {
        BooleanBuilder builder = new BooleanBuilder();

        if (hasText(pcKeyword)) {
            StringExpression carPlateNumberNoSpaces = Expressions.stringTemplate("REPLACE({0}, ' ', '')", product.carPlateNumber);
            StringExpression makerModelNoSpaces = Expressions.stringTemplate(
                    "REPLACE(CONCAT({0}, {1}, {2}, {3}), ' ', '')",
                    maker.linkDataNm, model.linkDataNm, detailModel.linkDataNm, detailGrade.linkDataNm
            );

            // 공백을 제거한 키워드
            StringExpression keywordNoSpaces = Expressions.stringTemplate("REPLACE({0}, ' ', '')", pcKeyword);

            // Predicate 구성
            Predicate keywordPredicate = carPlateNumberNoSpaces.contains(keywordNoSpaces)
                    .or(makerModelNoSpaces.contains(keywordNoSpaces));

            builder.and(keywordPredicate);
        }
        return builder;
    }

    private Predicate condApiSearchProductNameKeyword(String keyword, QLinkCode maker, QLinkCode model, QLinkCode detailModel, QLinkCode detailGrade) {
        BooleanBuilder builder = new BooleanBuilder();

        if (hasText(keyword)) {
            StringExpression carPlateNumberNoSpaces = Expressions.stringTemplate("REPLACE({0}, ' ', '')", product.carPlateNumber);
            StringExpression makerModelNoSpaces = Expressions.stringTemplate(
                    "REPLACE(CONCAT({0}, {1}, {2}, {3}), ' ', '')",
                    maker.linkDataNm, model.linkDataNm, detailModel.linkDataNm, detailGrade.linkDataNm
            );

            // 공백을 제거한 키워드
            StringExpression keywordNoSpaces = Expressions.stringTemplate("REPLACE({0}, ' ', '')", keyword);

            // Predicate 구성
            Predicate keywordPredicate = carPlateNumberNoSpaces.contains(keywordNoSpaces)
                    .or(makerModelNoSpaces.contains(keywordNoSpaces));
            builder.and(keywordPredicate);
        }
        return builder;
    }
    private Predicate condSearchCategoryInfo(Long categoryId1, Long categoryId2, List<Long> categoryId3, QLinkCode maker, QLinkCode model, QLinkCode detailGrade, String engName, String shapeType) {
        BooleanBuilder builder = new BooleanBuilder();

        // 1차 카테고리 조건
        if (engName != null && !engName.isEmpty()) {
            if("shape".equals(shapeType)) {
                builder.and(product.extShape.eq(ExteriorShape.ofCode(engName)));
            } else {
                builder.and(product.carSize.eq(CarSize.ofCode(engName)));
            }

            if (categoryId2 != null) {
                builder.and(product.modelNumber.eq(categoryId2));
            }

            // categoryId3 조건
            if (categoryId3 != null && !categoryId3.isEmpty()) {
                builder.and(product.detailModelNumber.in(categoryId3));
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
            // startPrice과 endPrice 모두 존재할 때
            builder.and(product.carAmountSale.between(startPrice, endPrice));
        } else if (startPrice != null) {
            // startPrice만 존재할 때
            builder.and(product.carAmountSale.goe(startPrice));
        } else if (endPrice != null) {
            // endPrice만 존재할 때
            builder.and(product.carAmountSale.loe(endPrice));
        }

        return builder;
    }

    private Predicate condApiSearchProductCarUseKm(Long startUseKm, Long endUseKm) {
        BooleanBuilder builder = new BooleanBuilder();

        if (startUseKm != null && endUseKm != null) {
            // startUseKm과 endUseKm 모두 존재할 때
            builder.and(product.carUseKm.between(startUseKm, endUseKm));
        } else if (startUseKm != null) {
            // startUseKm만 존재할 때
            builder.and(product.carUseKm.goe(startUseKm));
        } else if (endUseKm != null) {
            // endUseKm만 존재할 때
            builder.and(product.carUseKm.loe(endUseKm));
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

    private Predicate condApiSearchProductOption(List<Long> optionList) {
        BooleanBuilder builder = new BooleanBuilder();
        if (optionList != null && !optionList.isEmpty()) {
            builder.and(product.detailGradeNumber.in(optionList));
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
