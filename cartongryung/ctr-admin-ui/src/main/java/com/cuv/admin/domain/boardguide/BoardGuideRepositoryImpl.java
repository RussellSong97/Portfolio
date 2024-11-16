package com.cuv.admin.domain.boardguide;

import com.cuv.admin.common.YN;
import com.cuv.admin.domain.boardguide.dto.BoardGuideDetailDto;
import com.cuv.admin.domain.boardguide.dto.BoardGuideListDto;
import com.cuv.admin.domain.boardguide.dto.BoardGuideSearchDto;
import com.cuv.admin.domain.boardguide.enumset.BoardGuideType;
import com.cuv.admin.domain.member.enumset.MemberRole;
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
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

import static com.cuv.admin.domain.boardguide.entity.QBoardGuide.boardGuide;
import static org.springframework.util.StringUtils.hasText;

@Repository
public class BoardGuideRepositoryImpl implements BoardGuideRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public BoardGuideRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public Page<BoardGuideListDto> searchAllBoardGuide(BoardGuideSearchDto condition, Pageable pageable) {
        List<BoardGuideListDto> content = queryFactory
                .select(Projections.fields(BoardGuideListDto.class,
                        boardGuide.id,
                        boardGuide.title,
                        boardGuide.boardGuideType,
                        boardGuide.createdAt,
                        boardGuide.viewYn
                        ))
                .from(boardGuide)
                .where(
                        condMultiFieldLike(condition.getBoardGuideType(), condition.getS()),
                        condDelYnEqN()
                )
                .orderBy(getSort(pageable))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        JPQLQuery<Long> countQuery = queryFactory
                .select(Wildcard.count)
                .from(boardGuide)
                .where(
                        condMultiFieldLike(condition.getBoardGuideType(), condition.getS()),
                        condDelYnEqN()
                );


        return PageableExecutionUtils.getPage(content, pageable, countQuery::fetchCount);
    }

    @Override
    public BoardGuideDetailDto searchBoardGuideById(Long id) {
        return queryFactory
                .select(Projections.fields(BoardGuideDetailDto.class,
                        boardGuide.id,
                        boardGuide.title,
                        boardGuide.boardGuideType,
                        boardGuide.viewYn,
                        boardGuide.content,
                        boardGuide.attachment,
                        boardGuide.createdAt
                        ))
                .from(boardGuide)
                .where(
                        condIdEq(id),
                        condDelYnEqN()
                )
                .fetchFirst();
    }


    /* === === === === === === === === === === */


    private Predicate condMultiFieldLike(String field, String s) {
        BooleanBuilder builder = new BooleanBuilder();
        if (!hasText(field) || !hasText(s)) return builder;

        if (field.equals("all")) {
            return condTitleLike(s);
        } else {
            builder.and(condBoardGuideTypeEq(field));
            builder.and(condTitleLike(s));
        }

        return builder;
    }


    /* === === === === === === === === === === */


    private Predicate condIdEq(Long id) {
        return id != null ? boardGuide.id.eq(id) : null;
    }

    private Predicate condTitleLike(String s) {
        return hasText(s) ? boardGuide.title.contains(s) : null;
    }

    private Predicate condBoardGuideTypeEq(String boardGuideType) {
        return hasText(boardGuideType) ? boardGuide.boardGuideType.eq(BoardGuideType.ofCode(boardGuideType)) : null;
    }

    private Predicate condDelYnEqN() {
        return new BooleanBuilder().and(boardGuide.delYn.eq(YN.N));
    }


    /* === === === === === === === === === === */


    private OrderSpecifier<?>[] getSort(Pageable pageable) {
        List<OrderSpecifier<?>> orderSpecifiers = new ArrayList<>();

        if (pageable.getSort().isEmpty())
            return new OrderSpecifier[]{boardGuide.id.desc()};

        for (Sort.Order order : pageable.getSort()) {
            Order direction = order.isAscending() ? Order.ASC : Order.DESC;

            if (order.getProperty().equals("id")) {
                orderSpecifiers.add(new OrderSpecifier<>(direction, boardGuide.id));
            }
        }

        return orderSpecifiers.toArray(OrderSpecifier[]::new);
    }

}
