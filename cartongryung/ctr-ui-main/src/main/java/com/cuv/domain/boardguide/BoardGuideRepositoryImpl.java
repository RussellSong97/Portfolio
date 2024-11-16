package com.cuv.domain.boardguide;

import com.cuv.common.YN;
import com.cuv.domain.boardguide.dto.BoardGuideDetailDto;
import com.cuv.domain.boardguide.dto.BoardGuideListDto;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.cuv.domain.boardguide.entity.QBoardGuide.boardGuide;

@Repository
public class BoardGuideRepositoryImpl implements BoardGuideRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public BoardGuideRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public List<BoardGuideListDto> searchAllBoardGuide() {
        return queryFactory
                .select(Projections.fields(BoardGuideListDto.class,
                        boardGuide.id,
                        boardGuide.boardGuideType,
                        boardGuide.title,
                        boardGuide.content,
                        boardGuide.attachment
                        ))
                .from(boardGuide)
                .where(
                        condViewYnEqY(),
                        condDelYnEqN()
                )
                .orderBy(boardGuide.id.desc())
                .fetch();
    }

    @Override
    public BoardGuideDetailDto searchBoardGuideById(Long id) {
        return queryFactory
                .select(Projections.fields(BoardGuideDetailDto.class,
                        boardGuide.id,
                        boardGuide.boardGuideType,
                        boardGuide.title,
                        boardGuide.createdAt,
                        boardGuide.attachment,
                        boardGuide.content
                        ))
                .from(boardGuide)
                .where(
                        condIdEq(id),
                        condViewYnEqY(),
                        condDelYnEqN()
                )
                .fetchFirst();
    }

    private Predicate condIdEq(Long id) {
        return id != null ? boardGuide.id.eq(id) : null;
    }

    private Predicate condViewYnEqY() {
        return new BooleanBuilder().and(boardGuide.viewYn.eq(YN.Y));
    }

    private Predicate condDelYnEqN() {
        return new BooleanBuilder().and(boardGuide.delYn.eq(YN.N));
    }

}
