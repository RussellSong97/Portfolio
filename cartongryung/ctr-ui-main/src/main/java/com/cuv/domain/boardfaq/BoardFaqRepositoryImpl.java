package com.cuv.domain.boardfaq;

import com.cuv.common.YN;
import com.cuv.domain.boardfaq.dto.BoardFaqListDto;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.cuv.domain.boardfaq.entity.QBoardFaq.boardFaq;

@Repository
public class BoardFaqRepositoryImpl implements BoardFaqRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public BoardFaqRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public List<BoardFaqListDto> searchAllBoardFaq() {
        return queryFactory
                .select(Projections.fields(BoardFaqListDto.class,
                    boardFaq.title,
                    boardFaq.content
                ))
                .from(boardFaq)
                .where(
                        condViewYnEqY(),
                        condDelYnEqN()
                )
                .fetch();

    }

    private Predicate condViewYnEqY() {
        return new BooleanBuilder().and(boardFaq.viewYn.eq(YN.Y));
    }

    private Predicate condDelYnEqN() {
        return new BooleanBuilder().and(boardFaq.delYn.eq(YN.N));
    }
}
