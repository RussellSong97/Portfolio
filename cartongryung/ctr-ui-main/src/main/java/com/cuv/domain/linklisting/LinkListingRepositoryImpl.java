package com.cuv.domain.linklisting;

import com.cuv.common.YN;
import com.cuv.domain.linklisting.dto.LinkListingDetailDto;
import com.cuv.domain.linklisting.dto.LinkListingListDto;
import com.cuv.domain.linklisting.entity.LinkListing;
import com.cuv.domain.product.enumset.PostStatus;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.StringTemplate;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.cuv.domain.linklisting.entity.QLinkListing.linkListing;
import static com.cuv.domain.product.entity.QProduct.product;

@Repository
public class LinkListingRepositoryImpl implements LinkListingRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    public LinkListingRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public List<LinkListingListDto> searchApiProductFuel() {
        StringTemplate trimmedCarFuel = Expressions.stringTemplate("TRIM({0})", linkListing.carFuel);

        List<LinkListingListDto> result = queryFactory
                .select(Projections.fields(LinkListingListDto.class,
                        trimmedCarFuel.as("carFuel"),
                        linkListing.carMission,
                        Expressions.as(JPAExpressions.select(product.id.count())
                                .from(product)
                                .where(trimmedCarFuel.eq(product.carFuel)
                                        .and(product.delYn.eq(YN.N))
                                        .and(product.postStatus.eq(PostStatus.POST))), "productCount"),
                        linkListing.carFrameNo
                ))
                .from(linkListing)
                .where(trimmedCarFuel.ne("").and(trimmedCarFuel.ne("선택")))
                .groupBy(trimmedCarFuel)
                .fetch();

        return result;
    }

    @Override
    public List<LinkListingListDto> searchApiProductSido() {
        List<LinkListingListDto> result = queryFactory
                .select(Projections.fields(LinkListingListDto.class,
                        linkListing.id,
                        linkListing.sido,
                        linkListing.city,
                        Expressions.as(JPAExpressions.select(product.id.count())
                                .from(product)
                                .where(product.delYn.eq(YN.N)
                                        .and(product.city.eq(linkListing.sido))
                                        .and(product.postStatus.eq(PostStatus.POST))), "count"),
                        linkListing.carFrameNo
                ))
                .from(linkListing)
                .groupBy(linkListing.sido)
                .fetch();

        return result;
    }

    @Override
    public List<LinkListingListDto> searchApiProductIstdTrans() {
        List<LinkListingListDto> result = queryFactory
                .select(Projections.fields(LinkListingListDto.class,
                        linkListing.carMission,
                        linkListing.carFrameNo,
                        Expressions.as(
                                JPAExpressions.select(product.id.count())
                                        .from(product)
                                        .where(product.carMission.eq(linkListing.carMission)
                                                .and(product.delYn.eq(YN.N))
                                                .and(product.postStatus.eq(PostStatus.POST))), "count")
                ))
                .from(linkListing)
                .where(linkListing.carMission.ne(""))
                .groupBy(linkListing.carMission)
                .fetch();

        return result;
    }

    @Override
    public List<LinkListingListDto> searchMobileFuelList() {
        StringTemplate trimmedCarFuel = Expressions.stringTemplate("TRIM({0})", linkListing.carFuel);

        List<LinkListingListDto> result = queryFactory
                .select(Projections.fields(LinkListingListDto.class,
                        linkListing.id,
                        trimmedCarFuel.as("carFuel"),
                        linkListing.carMission,
                        Expressions.as(JPAExpressions.select(product.id.count())
                                .from(product)
                                .where(trimmedCarFuel.eq(product.carFuel)
                                        .and(product.delYn.eq(YN.N))
                                        .and(product.postStatus.eq(PostStatus.POST))), "productCount"),
                        linkListing.carFrameNo
                ))
                .from(linkListing)
                .where(trimmedCarFuel.ne("").and(trimmedCarFuel.ne("선택")))
                .groupBy(trimmedCarFuel)
                .fetch();

        return result;
    }

    @Override
    public List<LinkListingListDto> searchMobileIstdTrans() {
        List<LinkListingListDto> result = queryFactory
                .select(Projections.fields(LinkListingListDto.class,
                        linkListing.carMission,
                        linkListing.carFrameNo,
                        Expressions.as(
                                JPAExpressions.select(product.id.count())
                                        .from(product)
                                        .where(product.carMission.eq(linkListing.carMission)
                                                .and(product.delYn.eq(YN.N))
                                                .and(product.postStatus.eq(PostStatus.POST))), "count")
                ))
                .from(linkListing)
                .where(linkListing.carMission.ne(""))
                .groupBy(linkListing.carMission)
                .fetch();

        return result;
    }

    @Override
    public List<LinkListingListDto> searchMobileSidoList() {
        List<LinkListingListDto> result = queryFactory
                .select(Projections.fields(LinkListingListDto.class,
                        linkListing.id,
                        linkListing.sido,
                        linkListing.city,
                        Expressions.as(JPAExpressions.select(product.id.count())
                                .from(product)
                                .where(product.delYn.eq(YN.N)
                                        .and(product.city.eq(linkListing.sido))
                                        .and(product.postStatus.eq(PostStatus.POST))), "count"),
                        linkListing.carFrameNo
                ))
                .from(linkListing)
                .groupBy(linkListing.sido)
                .fetch();

        return result;
    }

}
