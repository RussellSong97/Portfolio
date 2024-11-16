package com.cuv.admin.domain.boardnotice;

import com.cuv.admin.common.YN;
import com.cuv.admin.domain.boardnotice.dto.BoardNoticeDetailDto;
import com.cuv.admin.domain.boardnotice.dto.BoardNoticeListDto;
import com.cuv.admin.domain.boardnotice.dto.BoardNoticeSearchDto;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Wildcard;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

import static com.cuv.admin.domain.boardnotice.entity.QBoardNotice.boardNotice;
import static com.cuv.admin.domain.memberadmin.entity.QMemberAdmin.memberAdmin;
import static org.springframework.util.StringUtils.hasText;

@Repository
public class BoardNoticeRepositoryImpl implements BoardNoticeRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    public BoardNoticeRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public Long searchAllNoticeCount() {
        return queryFactory
                .select(Wildcard.count)
                .from(boardNotice)
                .where(
                        condNoticeYnEqY(),
                        condDelYnEqN()
                )
                .fetchFirst();
    }

    @Override
    public Page<BoardNoticeListDto> searchAllBoardNotice(BoardNoticeSearchDto condition, Pageable request) {
        List<BoardNoticeListDto> content;

        content = queryFactory
                .select(Projections.fields(BoardNoticeListDto.class,
                        boardNotice.id,
                        boardNotice.title,
                        boardNotice.attachment,
                        boardNotice.hits,
                        memberAdmin.id.as("memberAdminId"),
                        memberAdmin.loginId,
                        memberAdmin.role,
                        boardNotice.viewYn,
                        boardNotice.noticeYn,
                        boardNotice.createdAt
                ))
                .from(boardNotice)
                .leftJoin(memberAdmin)
                .on(memberAdmin.id.eq(boardNotice.memberAdminId))
                .where(
                        condDelYnEqN(),
                        condMultiFieldLike(condition.getType(), condition.getS())
                )
                .offset(request.getOffset())
                .limit(request.getPageSize())
                .orderBy(getSort(request))
                .fetch();

        JPQLQuery<Long> contentQuery = queryFactory
                .select(Wildcard.count)
                .from(boardNotice)
                .leftJoin(memberAdmin)
                .on(memberAdmin.id.eq(boardNotice.memberAdminId))
                .where(
                        condDelYnEqN(),
                        condMultiFieldLike(condition.getType(), condition.getS())
                );

        return PageableExecutionUtils.getPage(content, request, contentQuery::fetchCount);
    }

    @Override
    public BoardNoticeDetailDto searchBoardNoticeById(Long id) {
        return queryFactory.select(Projections.fields(BoardNoticeDetailDto.class,
                boardNotice.id,
                boardNotice.title,
                boardNotice.content,
                boardNotice.attachment,
                boardNotice.createdId,
                boardNotice.viewYn,
                boardNotice.noticeYn,
                boardNotice.createdAt
        ))
                .from(boardNotice)
                .where(boardNotice.id.eq(id))
                .fetchOne();
    }

    @Override
    public List<BoardNoticeListDto> searchAllBoardNoticeByMain() {
        return queryFactory
                .select(Projections.fields(BoardNoticeListDto.class,
                        boardNotice.id,
                        boardNotice.title,
                        boardNotice.createdAt
                ))
                .from(boardNotice)
                .where(
                        condDelYnEqN()
                )
                .limit(5)
                .fetch();
    }

    private Predicate condNoticeYnEqY() {
        return new BooleanBuilder().and(boardNotice.noticeYn.eq(YN.Y));
    }

    private Predicate condDelYnEqN() {
        return new BooleanBuilder().and(boardNotice.delYn.eq(YN.N));
    }

    private Predicate condMultiFieldLike(String field, String s) {
        BooleanBuilder builder = new BooleanBuilder();
        if (!hasText(field) || !hasText(s)) return builder;

        if (field.equals("title")) {
            return condTitleLike(s);
        }else if (field.equals("content")) {
            return condContentLike(s);
        } else {
            builder.or(condTitleLike(s));
            builder.or(condContentLike(s));
        }

        return builder;
    }

    private Predicate condTitleLike(String s) {
        BooleanBuilder builder = new BooleanBuilder();
        builder.and(boardNotice.title.like("%" + s + "%"));
        return builder;
    }

    private Predicate condContentLike(String s) {
        BooleanBuilder builder = new BooleanBuilder();
        builder.and(boardNotice.content.like("%" + s + "%"));
        return builder;
    }
    private OrderSpecifier<?>[] getSort(Pageable pageable) {
        List<OrderSpecifier<?>> orderSpecifiers = new ArrayList<>();

        if (pageable.getSort().isEmpty())
            return new OrderSpecifier[]{boardNotice.id.desc()};

        for (Sort.Order order : pageable.getSort()) {
            Order direction = order.isAscending() ? Order.ASC : Order.DESC;


            if (order.getProperty().equals("id")) {
                orderSpecifiers.add(new OrderSpecifier<>(Order.DESC, boardNotice.noticeYn));
                orderSpecifiers.add(new OrderSpecifier<>(direction, boardNotice.id));
            }
        }

        return orderSpecifiers.toArray(OrderSpecifier[]::new);
    }
}
