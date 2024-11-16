package com.cuv.domain.linkcode;

import com.cuv.common.YN;
import com.cuv.domain.linkcode.dto.LinkCodeListDto;
import com.cuv.domain.product.enumset.PostStatus;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.Wildcard;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.aspectj.weaver.ast.Expr;
import org.springframework.stereotype.Repository;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.cuv.domain.linkcode.entity.QLinkCode.linkCode;
import static com.cuv.domain.product.entity.QProduct.product;

@Repository
public class LinkCodeRepositoryImpl implements LinkCodeRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public LinkCodeRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public Integer searchLastOrderSeq(Long parentLinkNbrId, Integer depth) {
        return queryFactory
                .select(
                        linkCode.viewOrder
                )
                .from(linkCode)
                .where(
                        condParentLinkNbrId(parentLinkNbrId),
                        condDepthEq(depth),
                        condDelYnEqN(), condViewYnEqY()
                )
                .orderBy(linkCode.viewOrder.desc())
                .fetchFirst();
    }


    @Override
    public List<LinkCodeListDto> searchFirstCategoryList() {
        return queryFactory.select(Projections.fields(LinkCodeListDto.class,
                        linkCode.id,
                        linkCode.linkDataNm,
                        linkCode.depth,
                        linkCode.attachment.as("carImage"),
                        Expressions.as(JPAExpressions.select(product.id.count()).from(product).where(linkCode.id.eq(product.makerNumber)
                                .and(product.delYn.eq(YN.N))
                                .and(product.postStatus.eq(PostStatus.POST))), "count")
                ))
                .from(linkCode)
                .where(linkCode.depth.eq(0), condDelYnEqN(), condViewYnEqY())
                .orderBy(linkCode.viewOrder.asc())
                .fetch();
    }
    @Override
    public List<LinkCodeListDto> searchApiCategoryChildrenList(Long categoryId, Integer depth) {
        Long searchCategoryId = categoryId == null ? 0L : categoryId;
        List<Tuple> result;
        if (depth == 0) {
            result = queryFactory
                    .select(linkCode.id,
                            linkCode.id.count())
                    .from(product)
                    .join(linkCode).on(product.makerNumber.eq(linkCode.id))
                    .where(
                            condDelYnEqN(),
                            condViewYnEqY(),
                            product.delYn.eq(YN.N),
                            product.postStatus.eq(PostStatus.POST))
                    .groupBy(product.makerNumber)
                    .fetch();

        } else if (depth == 1) {
            result = queryFactory
                    .select(product.modelNumber,
                            product.modelNumber.count())
                    .from(product)
                    .join(linkCode).on(product.modelNumber.eq(linkCode.id))
                    .where(linkCode.parentLinkNbrId.eq(searchCategoryId),
                            condDelYnEqN(),
                            condViewYnEqY(),
                            product.delYn.eq(YN.N),
                            product.postStatus.eq(PostStatus.POST))
                    .groupBy(product.modelNumber)
                    .fetch();
        } else if (depth == 2) {
            result = queryFactory
                    .select(product.detailModelNumber,
                            product.detailModelNumber.count())
                    .from(product)
                    .join(linkCode).on(product.detailModelNumber.eq(linkCode.id))
                    .where(linkCode.parentLinkNbrId.eq(searchCategoryId),
                            condDelYnEqN(),
                            condViewYnEqY(),
                            product.delYn.eq(YN.N),
                            product.postStatus.eq(PostStatus.POST))
                    .groupBy(product.detailModelNumber)
                    .fetch();
        } else {
            result = Collections.emptyList();
        }

        Map<Long, Long> countsMap = result.stream()
                .collect(Collectors.toMap(
                        tuple -> {
                            if (depth == 0) {
                                return tuple.get(linkCode.id);
                            } else if (depth == 1) {
                                return tuple.get(product.modelNumber);
                            } else {
                                return tuple.get(product.detailModelNumber);
                            }
                        },
                        tuple -> tuple.get(1, Long.class) // Count is always the second element in tuple
                ));

        List<LinkCodeListDto> linkCodes = queryFactory.select(Projections.fields(LinkCodeListDto.class,
                        linkCode.id,
                        linkCode.parentLinkNbrId.as("parentId"),
                        linkCode.linkDataNm,
                        linkCode.depth,
                        linkCode.attachment.as("carImage")
                ))
                .from(linkCode)
                .where(linkCode.parentLinkNbrId.eq(searchCategoryId),
                        linkCode.depth.eq(depth),
                        condDelYnEqN(), condViewYnEqY())
                .orderBy(linkCode.viewOrder.asc())
                .fetch();

        for (LinkCodeListDto dto : linkCodes) {
            Long count = countsMap.getOrDefault(dto.getId(), 0L);
            dto.setCount(count);
        }

        return linkCodes;
    }



    @Override
    public List<LinkCodeListDto> searchCategory2ListName(List<Long> modelIds) {
        return queryFactory.select(Projections.fields(LinkCodeListDto.class,
                        linkCode.id,
                        linkCode.linkDataNm,
                        linkCode.depth,
                        Expressions.as(JPAExpressions.select(Wildcard.count).from(product).where(linkCode.id.eq(product.modelNumber)
                                .and(product.delYn.eq(YN.N))
                                .and(product.postStatus.eq(PostStatus.POST))), "count")
                ))
                .from(linkCode)
                .where(linkCode.id.in(modelIds), condDelYnEqN(), condViewYnEqY())
                .orderBy(linkCode.viewOrder.asc())
                .fetch();
    }

    @Override
    public List<LinkCodeListDto> searchApiMainCategory(Map<String, Object> map) {
        Long category1 = map.get("category1") != null ? Long.valueOf(map.get("category1").toString()) : null;
        Long category2 = map.get("category2") != null ? Long.valueOf(map.get("category2").toString()) : null;
        Long category3 = map.get("category3") != null ? Long.valueOf(map.get("category3").toString()) : null;

        Integer depth = (Integer) map.get("depth");

        BooleanBuilder whereClause = new BooleanBuilder();
        whereClause.and(linkCode.depth.eq(depth));

        // depth에 따라 적절한 카테고리 ID 선택
        switch (depth) {
            case 0:
                // 1차 카테고리의 경우 parentLinkNbrId가 null이거나 0인 경우를 찾습니다.
                whereClause.and(linkCode.parentLinkNbrId.isNull().or(linkCode.parentLinkNbrId.eq(0L)));
                break;
            case 1:
                // 2차 카테고리의 경우 category1을 parentLinkNbrId로 사용
                whereClause.and(linkCode.parentLinkNbrId.eq(category1));
                break;
            case 2:
                // 3차 카테고리의 경우 category2를 parentLinkNbrId로 사용
                whereClause.and(linkCode.parentLinkNbrId.eq(category2));
                break;
            case 3:
                // 4차 카테고리의 경우 category3를 parentLinkNbrId로 사용
                whereClause.and(linkCode.parentLinkNbrId.eq(category3));
                break;
        }

        // 기본 조건 (삭제 여부, 보기 여부)
        whereClause.and(condDelYnEqN()).and(condViewYnEqY());

        return queryFactory.select(Projections.fields(LinkCodeListDto.class,
                        linkCode.id,
                        linkCode.parentLinkNbrId.as("parentId"),
                        linkCode.linkDataNm,
                        linkCode.depth
                ))
                .from(linkCode)
                .where(whereClause)
                .fetch();
    }


    private Predicate condParentLinkNbrId(Long upperLinkNbrId) {
        return upperLinkNbrId != null ? linkCode.parentLinkNbrId.eq(upperLinkNbrId) : null;
    }

    private Predicate condDepthEq(Integer depth) {
        return depth != null ? linkCode.depth.eq(depth) : null;
    }

    private Predicate condDelYnEqN() {
        return new BooleanBuilder().and(linkCode.delYn.eq(YN.N));
    }

    private Predicate condViewYnEqY() {
        return new BooleanBuilder().and(linkCode.viewYn.eq(YN.Y));
    }


}
