package com.cuv.domain.review;


import com.cuv.common.YN;
import com.cuv.domain.linkcode.entity.QLinkCode;
import com.cuv.domain.pick.PickRepositoryCustom;
import com.cuv.domain.pick.dto.PickListDto;
import com.cuv.domain.review.dto.ReviewDetailDto;
import com.cuv.domain.review.dto.ReviewListDto;
import com.cuv.domain.review.dto.ReviewSearchDto;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Wildcard;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;

import java.util.List;

import static com.cuv.domain.memberadmin.entity.QMemberAdmin.memberAdmin;
import static com.cuv.domain.pick.entity.QPick.pick;
import static com.cuv.domain.product.entity.QProduct.product;
import static com.cuv.domain.review.entity.QReview.review;

public class ReviewRepositoryImpl implements ReviewRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public ReviewRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    /**
     *  리뷰 리스트 가져오기:컨텐츠를 가져오는 쿼리와 카운트 쿼리 분리
     * @author 박성민
     */
    @Override
    public Page<ReviewListDto> searchAllReviewList(ReviewSearchDto condition, Pageable request) {
        // content를 가져오는 쿼리
        List<ReviewListDto> content = queryFactory
                .select(Projections.fields(ReviewListDto.class,
                        review.id.as("id"),
                        review.memberAdminId,
                        review.productId,
                        review.title,
                        review.content,
                        review.attachment,
                        review.createdAt.as("createdAt"),

                        //  memberAdmin에서 가져오는 것
                        memberAdmin.profileImageJson
                ))
                .from(review)
                .where(condViewYnEqY(), condDelYnEqN())
                .innerJoin(memberAdmin)
                .on(review.memberAdminId.eq(memberAdmin.id))
                .offset(request.getOffset())
                .limit(request.getPageSize())
                .orderBy(review.id.desc())
                .fetch();

        // count를 가져오는 쿼리
        JPQLQuery<Long> countQuery = queryFactory
                .select(Wildcard.count)
                .from(review)
                .where(condViewYnEqY(), condDelYnEqN())
                .innerJoin(memberAdmin)
                .on(review.memberAdminId.eq(memberAdmin.id));

        return PageableExecutionUtils.getPage(content, request, countQuery::fetchOne);
    }

    /**
     *  메인에 뿌릴 리뷰 리스트 가져오기
     * @author 박성민
     */
    @Override
    public List<ReviewListDto> searchAllReviewNopageList() {
        List<ReviewListDto> content = queryFactory
                .select(Projections.fields(ReviewListDto.class,
                        review.id.as("id"),
                        review.memberAdminId,
                        review.productId,
                        review.title,
                        review.content,
                        review.attachment,
                        review.createdAt.as("createdAt"),

                        //  memberAdmin에서 가져오는 것
                        memberAdmin.profileImageJson
                ))
                .from(review)
                .where(condViewYnEqY(), condDelYnEqN())
                .innerJoin(memberAdmin)
                .on(review.memberAdminId.eq(memberAdmin.id))
                .orderBy(review.id.desc())
                .fetch();
        return content;
    }

    /**
     *  리뷰 디테일 가져오기
     * @author 박성민
     */
    @Override
    public ReviewDetailDto searchReviewDetailByReviewId(Long id) {
        return queryFactory.select(Projections.fields(ReviewDetailDto.class,
                        review.id,
                        review.title,
                        review.content,
                        review.attachment,
                        review.createdAt
                ))
                .from(review)
                .where(review.id.eq(id))
                .fetchOne();
    }


    private Predicate condViewYnEqY() {
        return new BooleanBuilder().and(review.viewYn.eq(YN.Y));
    }

    private Predicate condDelYnEqN() {
        return new BooleanBuilder().and(review.delYn.eq(YN.N));
    }
}
