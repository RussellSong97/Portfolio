package com.cuv.domain.linkoptioninfo;

import com.cuv.common.YN;
import com.cuv.domain.linkoptioninfo.dto.LinkOptionInfoListDto;
import com.cuv.domain.linkoptioninfo.entity.LinkOptionInfo;
import com.cuv.domain.product.dto.ProductLinkOptionDto;
import com.cuv.domain.product.dto.SpecGroupDto;
import com.cuv.domain.product.enumset.PostStatus;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.cuv.domain.linkoptioninfo.entity.QLinkOptionInfo.linkOptionInfo;
import static com.cuv.domain.product.entity.QProduct.product;

public class LinkOptionInfoRepositoryImpl implements LinkOptionInfoRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    public LinkOptionInfoRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public List<LinkOptionInfoListDto> searchApiProductOptionList() {

        List<LinkOptionInfoListDto> list = queryFactory.select(Projections.fields(LinkOptionInfoListDto.class,
                        linkOptionInfo.id,
                        linkOptionInfo.optNm.as("optionName"),
                        linkOptionInfo.optPickType.as("optPickType"),
                        Expressions.as(JPAExpressions.select(product.id.count())
                                .from(product).where(linkOptionInfo.carGradeNbr.eq(product.detailGradeNumber)
                                        .and(product.delYn.eq(YN.N))
                                        .and(product.postStatus.eq(PostStatus.POST))), "count"),
                        linkOptionInfo.carGradeNbr
                ))
                .from(linkOptionInfo)
                .leftJoin(product).on(linkOptionInfo.carGradeNbr.eq(product.detailGradeNumber))
                .where(linkOptionInfo.optPickType.isNotNull())
                .limit(12)
                .fetch();
        return list;
    }

    @Override
    public List<SpecGroupDto> searchApiProductMoreOptionList() {
        List<ProductLinkOptionDto> result = queryFactory.select(Projections.fields(ProductLinkOptionDto.class,
                        linkOptionInfo.carGradeNbr,
                        linkOptionInfo.optCtgry,
                        linkOptionInfo.optNm
                ))
                .from(linkOptionInfo)
                .where(linkOptionInfo.optPickType.isNotNull())
                .groupBy(linkOptionInfo.optNm)
                .fetch();

        Map<String, List<ProductLinkOptionDto>> categoryToOptionsMap = result.stream()
                .collect(Collectors.groupingBy(dto -> {
                    String optCtgry = dto.getOptCtgry();
                    return optCtgry != null ? optCtgry : "";
                }));


        return categoryToOptionsMap.entrySet().stream()
                .map(entry -> {
                    SpecGroupDto specGroupDto = new SpecGroupDto();
                    specGroupDto.setSpecCtgry(entry.getKey());
                    specGroupDto.setOptionList(entry.getValue());
                    return specGroupDto;
                }).collect(Collectors.toList());
    }

    @Override
    public List<LinkOptionInfoListDto> searchMobileOptionList() {
        return queryFactory.select(Projections.fields(LinkOptionInfoListDto.class,
                        linkOptionInfo.id.max().as("id"),
                        linkOptionInfo.optNm.max().as("optionName"),
                        linkOptionInfo.optPickType.max().as("optPickType"),
                        product.id.count().as("count"),
                        linkOptionInfo.carGradeNbr.max().as("carGradeNbr")
                ))
                .from(linkOptionInfo)
                .leftJoin(product).on(linkOptionInfo.carGradeNbr.eq(product.detailGradeNumber))
                .where(linkOptionInfo.optPickType.isNotNull()
                        .and(product.delYn.eq(YN.N))
                        .and(product.postStatus.eq(PostStatus.POST)))
                .groupBy(linkOptionInfo.optNm)
                .fetch();
    }
}
