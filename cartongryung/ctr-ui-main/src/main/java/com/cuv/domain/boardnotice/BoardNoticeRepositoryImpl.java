package com.cuv.domain.boardnotice;

import com.cuv.common.YN;
import com.cuv.domain.boardnotice.dto.BoardNoticeDetailDto;
import com.cuv.domain.boardnotice.dto.BoardNoticeListDto;
import com.cuv.domain.boardnotice.dto.BoardNoticeSearchDto;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.core.types.dsl.Wildcard;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static com.cuv.domain.boardnotice.entity.QBoardNotice.boardNotice;
import static org.springframework.util.StringUtils.hasText;

@Repository
public class BoardNoticeRepositoryImpl implements BoardNoticeRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    public BoardNoticeRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public Page<BoardNoticeListDto> searchAllBoardNotice(BoardNoticeSearchDto condition, Pageable request) {
        // 등록된 지 7일 이후인지 여부
        BooleanExpression newActive = getNewActiveExpression();

        List<BoardNoticeListDto> content = queryFactory
                .select(Projections.fields(BoardNoticeListDto.class,
                        boardNotice.id,
                        boardNotice.noticeYn,
                        boardNotice.title,
                        newActive.as("newActive"),
                        boardNotice.createdAt,
                        boardNotice.hits
                ))
                .from(boardNotice)
                .where(
                        condTitleLike(condition.getS()),
                        condViewYnEqY(),
                        condDelYnEqN()
                )
                .offset(request.getOffset())
                .limit(request.getPageSize())
                .orderBy(getSort(request))
                .fetch();

        JPQLQuery<Long> contentQuery = queryFactory
                .select(Wildcard.count)
                .from(boardNotice)
                .where(
                        condTitleLike(condition.getS()),
                        condViewYnEqY(),
                        condDelYnEqN()
                );

        return PageableExecutionUtils.getPage(content, request, contentQuery::fetchCount);
    }

    @Override
    public BoardNoticeDetailDto searchBoardNoticeById(Long id) {
        // 등록된 지 7일 이후인지 여부
        BooleanExpression newActive = getNewActiveExpression();

        return queryFactory
                .select(Projections.fields(BoardNoticeDetailDto.class,
                    boardNotice.id,
                    boardNotice.noticeYn,
                    boardNotice.title,
                    newActive.as("newActive"),
                    boardNotice.createdAt,
                    boardNotice.hits,
                    boardNotice.content,
                    boardNotice.attachment
                ))
                .from(boardNotice)
                .where(boardNotice.id.eq(id))
                .fetchOne();
    }

    @Override
    public BoardNoticeDetailDto searchBoardNoticeByMain() {
        return queryFactory
                .select(Projections.fields(BoardNoticeDetailDto.class,
                        boardNotice.id,
                        boardNotice.title,
                        boardNotice.createdAt
                ))
                .from(boardNotice)
                .where(
                        condViewYnEqY(),
                        condDelYnEqN()
                )
                .orderBy(boardNotice.id.desc())
                .limit(1)
                .fetchOne();
    }

    /**
     * 새로운 글 판별 여부 표현식
     *
     * @author SungHa
     */
    private static BooleanExpression getNewActiveExpression() {
        return new CaseBuilder()
                .when(
                        boardNotice.createdAt.goe(LocalDateTime.now().minusDays(6))
                                .and(boardNotice.createdAt.loe(LocalDateTime.now()))
                )
                .then(true)
                .otherwise(false);
    }

    private Predicate condViewYnEqY() {
        return new BooleanBuilder().and(boardNotice.viewYn.eq(YN.Y));
    }

    private Predicate condDelYnEqN() {
        return new BooleanBuilder().and(boardNotice.delYn.eq(YN.N));
    }

    private Predicate condTitleLike(String s) {
        return hasText(s) ? boardNotice.title.contains(s) : null;
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
