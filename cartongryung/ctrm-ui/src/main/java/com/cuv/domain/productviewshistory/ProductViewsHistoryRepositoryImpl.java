package com.cuv.domain.productviewshistory;

import com.cuv.common.YN;
import com.cuv.domain.linkcode.entity.QLinkCode;
import com.cuv.domain.product.enumset.PostStatus;
import com.cuv.domain.productviewshistory.dto.ProductViewsHistoryMyPageRecentCarListDto;
import com.cuv.domain.productviewshistory.dto.ProductViewsHistoryRecentCarListDto;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Wildcard;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.cuv.domain.product.entity.QProduct.product;
import static com.cuv.domain.productviewshistory.entity.QProductViewsHistory.productViewsHistory;

@Repository
public class ProductViewsHistoryRepositoryImpl implements ProductViewsHistoryRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public ProductViewsHistoryRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    /**
     * aside > 로그인 상태 최근본차량
     *
     * @author Sangbin
     */
    @Override
    public List<ProductViewsHistoryRecentCarListDto> searchProductViewsHistoryRecentCarList(Long memberId) {
        QLinkCode maker = new QLinkCode("maker");
        QLinkCode model = new QLinkCode("model");
        QLinkCode detailGrade = new QLinkCode("detailGrade");

        return queryFactory
                .select(Projections.fields(ProductViewsHistoryRecentCarListDto.class,
                        productViewsHistory.id,
                        productViewsHistory.productId,
                        product.carImageUrl,
                        maker.linkDataNm.as("makerName"),
                        model.linkDataNm.as("modelName"),
                        detailGrade.linkDataNm.as("detailGradeName")
                ))
                .from(productViewsHistory)
                .leftJoin(product).on(productViewsHistory.productId.eq(product.id))
                .innerJoin(maker)
                .on(maker.id.eq(product.makerNumber))
                .innerJoin(model)
                .on(model.id.eq(product.modelNumber))
                .innerJoin(detailGrade)
                .on(detailGrade.id.eq(product.detailGradeNumber))
                .where(condMemberIdEq(memberId))
                .orderBy(productViewsHistory.createdAt.desc())
                .where(productViewsHistory.delYn.eq(YN.N))
                .limit(10)
                .fetch();
    }

    /**
     * aside > 미로그인 상태 최근본차량
     *
     * @author Sangbin
     */
    @Override
    public List<ProductViewsHistoryRecentCarListDto> searchProductViewsHistoryRecentCarListByIds(List<Long> ids) {
        QLinkCode maker = new QLinkCode("maker");
        QLinkCode model = new QLinkCode("model");
        QLinkCode detailGrade = new QLinkCode("detailGrade");

        return queryFactory
                .select(Projections.fields(ProductViewsHistoryRecentCarListDto.class,
                        product.id.as("productId"),
                        product.carImageUrl,
                        maker.linkDataNm.as("makerName"),
                        model.linkDataNm.as("modelName"),
                        detailGrade.linkDataNm.as("detailGradeName")
                ))
                .from(product)
                .leftJoin(maker).on(maker.id.eq(product.makerNumber))
                .leftJoin(model).on(model.id.eq(product.modelNumber))
                .leftJoin(detailGrade).on(detailGrade.id.eq(product.detailGradeNumber))
                .where(product.id.in(ids).and(product.delYn.eq(YN.N)))
                .limit(10)
                .fetch();
    }

    /**
     * 더보기 > 최근본차량 목록
     *
     * @author Sangbin
     */
    @Override
    public List<ProductViewsHistoryMyPageRecentCarListDto> searchProductViewsHistoryMyPageRecentCarListByMemberId(Long memberId) {
        QLinkCode maker = new QLinkCode("maker");
        QLinkCode model = new QLinkCode("model");
        QLinkCode detailGrade = new QLinkCode("detailGrade");

        return queryFactory
                .select(Projections.fields(ProductViewsHistoryMyPageRecentCarListDto.class,
                        productViewsHistory.id.as("product_views_history_id"),
                        productViewsHistory.productId,
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
                .from(productViewsHistory)
                .leftJoin(product).on(productViewsHistory.productId.eq(product.id))
                .innerJoin(maker)
                .on(maker.id.eq(product.makerNumber))
                .innerJoin(model)
                .on(model.id.eq(product.modelNumber))
                .innerJoin(detailGrade)
                .on(detailGrade.id.eq(product.detailGradeNumber))
                .where(product.postStatus.eq(PostStatus.POST),
                        productViewsHistory.delYn.eq(YN.N),
                        condMemberIdEq(memberId))
                .orderBy(productViewsHistory.createdAt.desc())
                .fetch();
    }

    /**
     * 더보기 > 최근본차량 카운트
     *
     * @author Sangbin
     */
    @Override
    public Long searchProductViewsHistoryMyPageRecentCarCountByMemberId(Long memberId) {
        return queryFactory
                .select(Wildcard.count)
                .from(productViewsHistory)
                .where(productViewsHistory.delYn.eq(YN.N),
                        condMemberIdEq(memberId))
                .fetchOne();
    }

    private Predicate condMemberIdEq(Long id) {
        return id != null ? productViewsHistory.memberId.eq(id) : null;
    }
}
