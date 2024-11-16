package com.cuv.domain.linkcode;

import com.cuv.common.YN;
import com.cuv.domain.linkcode.dto.LinkCodeListDto;
import com.cuv.domain.product.enumset.PostStatus;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.Wildcard;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.aspectj.weaver.ast.Expr;
import org.springframework.stereotype.Repository;

import java.util.List;

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
                        condDelYnEqN()
                )
                .orderBy(linkCode.viewOrder.desc())
                .fetchFirst();
    }

    @Override
    public List<LinkCodeListDto> searchAlinkCodeList() {
        return queryFactory.select(Projections.fields(LinkCodeListDto.class,
                        linkCode.id,
                        linkCode.parentLinkNbrId.as("parentId"),
                        linkCode.linkDataNm,
                        linkCode.depth
                ))
                .from(linkCode)
                .where(condDelYnEqN())
                .orderBy(linkCode.viewOrder.asc())
                .fetch();
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
                        .and(product.postStatus.eq(PostStatus.POST))),"count")
                ))
                .from(linkCode)
                .where(linkCode.depth.eq(0), condDelYnEqN())
                .orderBy(linkCode.viewOrder.asc())
                .fetch();
    }

    @Override
    public List<LinkCodeListDto> searchCategoryListByCategoryId(Long categoryId) {
        return queryFactory.select(Projections.fields(LinkCodeListDto.class,
                        linkCode.id,
                        linkCode.parentLinkNbrId.as("parentId"),
                        linkCode.linkDataNm,
                        linkCode.depth
//                linkCode.attachment.as("carImage")
                ))
                .from(linkCode)
                .where(linkCode.parentLinkNbrId.eq(categoryId), condDelYnEqN())
                .fetch();
    }

    @Override
    public List<LinkCodeListDto> searchApiCategoryChildrenList(Long categoryId, Integer depth) {
        System.out.println("categoryId = " + categoryId);
        System.out.println("depth = " + depth);
        Long searchCategoryId = categoryId == null ? 0L : categoryId;
        return queryFactory.select(Projections.fields(LinkCodeListDto.class,
                        linkCode.id,
                        linkCode.parentLinkNbrId.as("parentId"),
                        linkCode.linkDataNm,
                        linkCode.depth,
                        Expressions.as(JPAExpressions.select(Wildcard.count)
                                .from(product)
                                .where(linkCode.id.eq(searchCategoryId)
                                        .and(product.delYn.eq(YN.N))
                                        .and(product.postStatus.eq(PostStatus.POST))), "count")
                ))
                .from(linkCode)
                .where(linkCode.parentLinkNbrId.eq(searchCategoryId),
                        linkCode.depth.eq(depth),
                        condDelYnEqN())
                .orderBy(linkCode.viewOrder.asc())
                .fetch();
    }

    @Override
    public List<LinkCodeListDto> searchCategory2ListName(List<Long> modelIds) {
        return queryFactory.select(Projections.fields(LinkCodeListDto.class,
                        linkCode.id,
                        linkCode.linkDataNm,
                        linkCode.depth,
                        Expressions.as(JPAExpressions.select(Wildcard.count).from(product).where(linkCode.id.eq(product.modelNumber)
                                .and(product.delYn.eq(YN.N))
                                .and(product.postStatus.eq(PostStatus.POST))),"count")
                ))
                .from(linkCode)
                .where(linkCode.id.in(modelIds), condDelYnEqN())
                .orderBy(linkCode.viewOrder.asc())
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

}
