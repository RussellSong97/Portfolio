package com.cuv.domain.linkinfo;

import com.cuv.common.YN;
import com.cuv.domain.linkinfo.dto.LinkInfoListDto;
import com.cuv.domain.product.enumset.PostStatus;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.cuv.domain.linkinfo.entity.QLinkInfo.linkInfo;
import static com.cuv.domain.linklisting.entity.QLinkListing.linkListing;
import static com.cuv.domain.product.entity.QProduct.product;

@Repository
public class LinkInfoRepositoryImpl implements LinkInfoRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public LinkInfoRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public List<LinkInfoListDto> searchApiProductIstdTrans() {
        return queryFactory
                .select(Projections.fields(LinkInfoListDto.class,
                        linkInfo.id,
                        linkInfo.istdTrans,
                        Expressions.as(JPAExpressions.select(linkInfo.id.count())
                                .from(linkInfo)
                                .leftJoin(product).on(linkInfo.vhrno.eq(product.carPlateNumber))
                                .where(product.delYn.eq(YN.N)), "count"),
                        linkInfo.vhrno.as("carPlateNumber")
                ))
                .from(linkInfo)
                .groupBy(linkInfo.istdTrans)
                .fetch();
    }

    @Override
    public List<LinkInfoListDto> searchApiProductColor() {

        List<LinkInfoListDto> result = queryFactory.select(Projections.fields(LinkInfoListDto.class,
                        linkListing.id,
                        linkListing.carColor.as("colorName"),
                        Expressions.as(
                                JPAExpressions.select(product.id.count())
                                        .from(product)
                                        .where(linkListing.carColor.eq(product.carColor)
                                                .and(product.delYn.eq(YN.N))
                                                .and(product.postStatus.eq(PostStatus.POST))),
                                "count")
                        ))
                .from(linkListing)
                .groupBy(linkListing.carColor)
                .fetch();

        return result;
    }
}
