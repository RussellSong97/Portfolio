package com.cuv.domain.pick;

import com.cuv.common.YN;
import com.cuv.domain.linkcode.entity.QLinkCode;
import com.cuv.domain.pick.dto.PickCountDto;
import com.cuv.domain.pick.dto.PickListDto;
import com.cuv.domain.pick.entity.QPick;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Wildcard;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;

import java.util.List;

import static com.cuv.domain.pick.entity.QPick.pick;
import static com.cuv.domain.product.entity.QProduct.product;


public class PickRepositoryImpl implements PickRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public PickRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }


    /**
     * @param memberId: 맴버 ID
     *  멤버 아이디로 픽 가져오기
     * @author 박성민
     */
    @Override
    public List<PickListDto> searchPickListNoPageList(Long memberId) {
        QLinkCode maker = new QLinkCode("maker");
        QLinkCode model = new QLinkCode("model");
        QLinkCode detailGrade = new QLinkCode("detailGrade");

        List<PickListDto> content;
        content = queryFactory
                .select(Projections.fields(PickListDto.class,
                        pick.id.as("pickId"),
                        pick.memberId,
                        pick.productId,
                        pick.readYn,
                        pick.createdAt.as("createdAt"),
                        // 픽에서 가져오는 거 끝
                        // productId로 조인해서 product의 것 가져오기
                        product.carImageUrl,
                        product.shopName,
                        maker.linkDataNm.as("makerName"),
                        model.linkDataNm.as("modelName"),
                        detailGrade.linkDataNm.as("detailGradeName"),
                        product.carRegYear,
                        product.carFuel,
                        product.carUseKm,
                        product.carAmountSale,
                        product.postStatus,
                        product.city,
                        product.createdAt
                ))
                .from(pick)
                .where(pick.memberId.eq(memberId))
                .where(pick.delYn.eq(YN.N))
                .innerJoin(product)
                .on(pick.productId.eq(product.id))
                .innerJoin(maker)
                .on(maker.id.eq(product.makerNumber))
                .innerJoin(model)
                .on(model.id.eq(product.modelNumber))
                .innerJoin(detailGrade)
                .on(detailGrade.id.eq(product.detailGradeNumber))
                .orderBy(pick.id.desc())
                .fetch();

        return content;
    }

    /**
     * 헤더 > 찜 불들어오는거
     * @param id
     * @return
     */
    @Override
    public Long searchPicksMemberIdReadYn(Long id) {
        return queryFactory
                .select(pick.id.max().as("id"))
                .from(pick)
                .where(pick.memberId.eq(id)
                        .and(pick.readYn.eq(YN.N))
                        .and(pick.delYn.eq(YN.N)))
                .fetchFirst();
    }

    /**
     * memberId로 픽Id 가져오기
     * aisde에 픽 개수 찍기위함
     * @author 박성민
     */
    @Override
    public Long searchPicksMemberIdCount(Long memberId) {
        return queryFactory
                .select(pick.id.count())
                .from(pick)
                .where(pick.memberId.eq(memberId))
                .where(pick.delYn.eq(YN.N))
                .fetchFirst();
    }

    @Override
    public Long searchPickCountByProductId(Long productId) {
        return queryFactory
                .select(pick.count())
                .from(pick)
                .where(pick.productId.eq(productId))
                .where(pick.delYn.eq(YN.N))
                .fetchFirst();
    }

    @Override
    public PickCountDto searchPickSummary(Long memberId) {
        QPick subPick = new QPick("subPick");

        List<Long> pickProductIds = queryFactory.select(pick.productId)
                .from(pick)
                .where(pick.memberId.eq(memberId))
                .where(pick.delYn.eq(YN.N))
                .fetch();

        Tuple tuple = queryFactory
                .select(pick.count().as("pickCount"),
                        ExpressionUtils.as(
                                JPAExpressions.select(Wildcard.count.coalesce(0L))
                                        .from(subPick)
                                        .where(
                                                subPick.memberId.eq(pick.memberId),
                                                subPick.readYn.eq(YN.N),
                                                subPick.delYn.eq(YN.N)
                                        ), "unreadCount")
                )
                .from(pick)
                .where(
                        pick.memberId.eq(memberId),
                        pick.delYn.eq(YN.N)
                )
                .fetchFirst();

        return new PickCountDto(pickProductIds, tuple.get(0, Long.class), tuple.get(1, Long.class));
    }



}
