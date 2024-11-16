package com.cuv.admin.domain.boardfaq;

import com.cuv.admin.common.YN;
import com.cuv.admin.domain.boardfaq.dto.BoardFaqDetailDto;
import com.cuv.admin.domain.boardfaq.dto.BoardFaqListDto;
import com.cuv.admin.domain.boardfaq.dto.BoardFaqSearchDto;
import com.cuv.admin.domain.boardfaq.entity.BoardFaq;
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
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.cuv.admin.domain.boardfaq.entity.QBoardFaq.boardFaq;
import static com.cuv.admin.domain.boardguide.entity.QBoardGuide.boardGuide;
import static com.cuv.admin.domain.boardnotice.entity.QBoardNotice.boardNotice;
import static org.springframework.util.StringUtils.hasText;

@Repository
public class BoardFaqRepositoryImpl implements BoardFaqRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public BoardFaqRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public Page<BoardFaqListDto> searchAllBoardFaq(BoardFaqSearchDto condition, Pageable request) {
        List<BoardFaqListDto> content;
        content = queryFactory.select(Projections.fields(BoardFaqListDto.class,
                boardFaq.id,
                boardFaq.title,
                boardFaq.content,
                boardFaq.createdAt,
                boardFaq.viewYn
                ))
                .from(boardFaq)
                .where(
                        condKeyword(condition.getType(), condition.getS()),
                        condDelYnEqN()
                )
                .offset(request.getOffset())
                .limit(request.getPageSize())
                .orderBy(boardFaq.id.desc())
                .fetch();

        JPQLQuery<Long> countQuery = queryFactory
                .select(Wildcard.count)
                .where(
                        condKeyword(condition.getType(), condition.getS()),
                        condDelYnEqN()
                )
                .from(boardFaq);

        return PageableExecutionUtils.getPage(content, request, countQuery::fetchCount);
    }

    @Override
    public BoardFaqDetailDto searchBoardFaqById(Long id) {
        return queryFactory
                .select(Projections.fields(BoardFaqDetailDto.class,
                        boardFaq.id,
                        boardFaq.title,
                        boardFaq.content,
                        boardFaq.createdAt,
                        boardFaq.viewYn
                ))
                .from(boardFaq)
                .where(boardFaq.id.eq(id))
                .fetchOne();
    }

    private Predicate condKeyword(String type, String s) {
        BooleanBuilder builder = new BooleanBuilder();

        if(!hasText(type) || !hasText(s)) {
            return builder;
        }

        switch(type) {
            case "title":
                builder.and(boardFaq.title.contains(s));
                break;

            case "content" :
                builder.and(boardFaq.content.contains(s));
                break;

            case "all" :
                builder.and(boardFaq.title.contains(s)
                        .or(boardFaq.content.contains(s)));
                break;
        }

        return builder;
    }

    private Predicate condDelYnEqN() {
        return new BooleanBuilder().and(boardFaq.delYn.eq(YN.N));
    }
}
