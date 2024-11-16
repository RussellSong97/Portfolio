package com.cuv.domain.boardreview;


import com.cuv.common.YN;
import com.cuv.domain.boardreview.dto.BoardReviewDetailDto;
import com.cuv.domain.boardreview.dto.BoardReviewListDto;
import com.cuv.domain.boardreview.dto.BoardReviewSearchDto;
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

import static com.cuv.domain.boardreview.entity.QBoardReview.boardReview;
import static com.cuv.domain.memberadmin.entity.QMemberAdmin.memberAdmin;

public class BoardReviewRepositoryImpl implements BoardReviewRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public BoardReviewRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    /**
     *  리뷰 리스트 가져오기:컨텐츠를 가져오는 쿼리와 카운트 쿼리 분리
     * @author 박성민
     */
    @Override
    public Page<BoardReviewListDto> searchAllReviewList(BoardReviewSearchDto condition, Pageable request) {
        // content를 가져오는 쿼리
        List<BoardReviewListDto> content = queryFactory
                .select(Projections.fields(BoardReviewListDto.class,
                        boardReview.id,
                        boardReview.memberAdminId,
                        boardReview.productId,
                        boardReview.title,
                        boardReview.content,
                        boardReview.attachment,
                        boardReview.createdAt.as("createdAt"),

                        //  memberAdmin에서 가져오는 것
                        memberAdmin.profileImageJson
                ))
                .from(boardReview)
                .innerJoin(memberAdmin)
                .on(boardReview.memberAdminId.eq(memberAdmin.id))
                .where(condViewYnEqY(), condDelYnEqN())
                .offset(request.getOffset())
                .limit(request.getPageSize())
                .orderBy(boardReview.id.desc())
                .fetch();

        // count를 가져오는 쿼리
        JPQLQuery<Long> countQuery = queryFactory
                .select(Wildcard.count)
                .from(boardReview)
                .where(condViewYnEqY(), condDelYnEqN())
                .innerJoin(memberAdmin)
                .on(boardReview.memberAdminId.eq(memberAdmin.id));

        return PageableExecutionUtils.getPage(content, request, countQuery::fetchOne);
    }

    /**
     *  메인에 뿌릴 리뷰 리스트 가져오기
     * @author 박성민
     */
    @Override
    public List<BoardReviewListDto> searchAllReviewNopageList() {
        return queryFactory
                .select(Projections.fields(BoardReviewListDto.class,
                        boardReview.id,
                        boardReview.memberAdminId,
                        boardReview.productId,
                        boardReview.title,
                        boardReview.content,
                        boardReview.attachment,
                        boardReview.createdAt.as("createdAt"),

                        //  memberAdmin에서 가져오는 것
                        memberAdmin.profileImageJson
                ))
                .from(boardReview)
                .where(condViewYnEqY(), condDelYnEqN())
                .innerJoin(memberAdmin)
                .on(boardReview.memberAdminId.eq(memberAdmin.id))
                .orderBy(boardReview.id.desc())
                .fetch();
    }

    /**
     *  리뷰 디테일 가져오기
     * @author 박성민
     */
    @Override
    public BoardReviewDetailDto searchReviewDetailByReviewId(Long id) {
        return queryFactory
                .select(Projections.fields(BoardReviewDetailDto.class,
                        boardReview.id,
                        boardReview.title,
                        boardReview.content,
                        boardReview.attachment,
                        boardReview.createdAt
                ))
                .from(boardReview)
                .where(boardReview.id.eq(id))
                .fetchOne();
    }


    private Predicate condViewYnEqY() {
        return new BooleanBuilder().and(boardReview.viewYn.eq(YN.Y));
    }

    private Predicate condDelYnEqN() {
        return new BooleanBuilder().and(boardReview.delYn.eq(YN.N));
    }
}
