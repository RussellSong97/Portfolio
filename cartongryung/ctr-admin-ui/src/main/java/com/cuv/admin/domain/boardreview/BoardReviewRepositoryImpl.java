package com.cuv.admin.domain.boardreview;

import com.cuv.admin.common.YN;
import com.cuv.admin.domain.linkcode.entity.QLinkCode;
import com.cuv.admin.domain.boardreview.dto.BoardReviewDetailDto;
import com.cuv.admin.domain.boardreview.dto.BoardReviewListDto;
import com.cuv.admin.domain.boardreview.dto.BoardReviewSearchDto;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Wildcard;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;

import java.util.List;

import static com.cuv.admin.domain.boardreview.entity.QBoardReview.boardReview;
import static com.cuv.admin.domain.product.entity.QProduct.product;
import static org.springframework.util.StringUtils.hasText;

public class BoardReviewRepositoryImpl implements BoardReviewRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    public BoardReviewRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    /**
     *
     * @author 박성민
     * 리스트
     */
    @Override
    public Page<BoardReviewListDto> searchAllReviewList(BoardReviewSearchDto condition, Pageable request) {
        QLinkCode maker = new QLinkCode("maker");
        QLinkCode model = new QLinkCode("model");
        QLinkCode detailGrade = new QLinkCode("detailGrade");

        // content를 가져오는 쿼리
        List<BoardReviewListDto> content = queryFactory
                .select(Projections.fields(BoardReviewListDto.class,
                        boardReview.id,
                        boardReview.productId,
                        boardReview.title,
                        boardReview.viewYn,
                        boardReview.createdAt,

                        //  memberAdmin에서 가져오는 것
                        maker.linkDataNm.as("makerName"),
                        model.linkDataNm.as("modelName"),
                        detailGrade.linkDataNm.as("detailGradeName")
                ))
                .from(boardReview)
                .leftJoin(product)
                .on(boardReview.productId.eq(product.id))
                .leftJoin(maker)
                .on(maker.id.eq(product.makerNumber))
                .leftJoin(model)
                .on(model.id.eq(product.modelNumber))
                .leftJoin(detailGrade)
                .on(detailGrade.id.eq(product.detailGradeNumber))
                .where(
                        condTitleLike(condition.getKeyWord()),
                        condDelYnEqN()
                )
                .orderBy(boardReview.id.desc())
                .offset(request.getOffset())
                .limit(request.getPageSize())
                .fetch();

        // count를 가져오는 쿼리
        JPQLQuery<Long> countQuery = queryFactory
                .select(Wildcard.count)
                .from(boardReview)
                .leftJoin(product)
                .on(boardReview.productId.eq(product.id))
                .leftJoin(maker)
                .on(maker.id.eq(product.makerNumber))
                .leftJoin(model)
                .on(model.id.eq(product.modelNumber))
                .leftJoin(detailGrade)
                .on(detailGrade.id.eq(product.detailGradeNumber))
                .where(
                        condDelYnEqN(),
                        condTitleLike(condition.getKeyWord()
                        )
                );

        return PageableExecutionUtils.getPage(content, request, countQuery::fetchOne);
    }

    /**
     *
     * @author 박성민
     * 디테일
     */
    @Override
    public BoardReviewDetailDto searchReviewDetailByReviewId(Long reviewId) {
        return queryFactory.select(Projections.fields(BoardReviewDetailDto.class,
                        boardReview.id,
                        boardReview.productId,
                        boardReview.title,
                        boardReview.content,
                        boardReview.attachment.as("reviewAttachment"),
                        boardReview.viewYn,
                        boardReview.createdAt,
                        product.carPlateNumber
                ))
                .from(boardReview)
                .leftJoin(product).on(boardReview.productId.eq(product.id))
                .where(boardReview.id.eq(reviewId))
                .fetchOne();
    }

    private Predicate condDelYnEqN() {
        return new BooleanBuilder().and(boardReview.delYn.eq(YN.N));
    }

    private Predicate condTitleLike(String keyWord) {
        return hasText(keyWord) ? boardReview.title.contains(keyWord) : null;
    }
}
