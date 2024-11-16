package com.cuv.domain.linklisting;

import com.cuv.common.YN;
import com.cuv.domain.linklisting.dto.LinkListingListDto;
import com.cuv.domain.product.enumset.PostStatus;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.StringTemplate;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;

import java.util.List;

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
                        trimmedCarFuel.max().as("carFuel"),
                        linkListing.carMission.max().as("carMission"),
                        Expressions.as(JPAExpressions.select(product.id.count())
                                .from(product)
                                .where(trimmedCarFuel.eq(product.carFuel)
                                        .and(product.delYn.eq(YN.N))
                                        .and(product.postStatus.eq(PostStatus.POST))), "productCount")
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
                        linkListing.id.max().as("id"),
                        linkListing.sido.max().as("sido"),
                        linkListing.city.max().as("city"),
                        Expressions.as(JPAExpressions.select(product.id.count())
                                .from(product)
                                .where(product.delYn.eq(YN.N)
                                        .and(product.city.eq(linkListing.sido))
                                        .and(product.postStatus.eq(PostStatus.POST))), "count")
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
                        linkListing.carMission.max().as("carMission"),
                        linkListing.carFrameNo.max().as("carFrameNo"),
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
                        linkListing.id.max().as("id"),
                        trimmedCarFuel.max().as("carFuel"),
                        linkListing.carMission.max().as("carMission"),
                        Expressions.as(JPAExpressions.select(product.id.count())
                                .from(product)
                                .where(trimmedCarFuel.eq(product.carFuel)
                                        .and(product.delYn.eq(YN.N))
                                        .and(product.postStatus.eq(PostStatus.POST))), "productCount")
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
                        linkListing.id.max().as("id"),
                        linkListing.carMission.max().as("carMission"),
                        linkListing.carFrameNo.max().as("carFrameNo"),
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
                        linkListing.id.max().as("id"),
                        linkListing.sido.max().as("sido"),
                        linkListing.city.max().as("city"),
                        Expressions.as(JPAExpressions.select(product.id.count())
                                .from(product)
                                .where(product.delYn.eq(YN.N)
                                        .and(product.city.eq(linkListing.sido))
                                        .and(product.postStatus.eq(PostStatus.POST))), "count")
                ))
                .from(linkListing)
                .groupBy(linkListing.sido)
                .fetch();

        return result;
    }

    @Override
    public List<LinkListingListDto> searchMobileCarColorList() {

        return
                queryFactory.select(Projections.fields(LinkListingListDto.class,
                        linkListing.id.max().as("id"),
                        linkListing.carColor.max().as("carColor")
                        ))
                        .from(linkListing)
                        .where(linkListing.carColor.ne(""))
                        .groupBy(linkListing.carColor)
                        .fetch();
    }

}
