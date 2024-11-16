package com.cuv.admin.domain.popularkeyword;

import com.cuv.admin.common.YN;
import com.cuv.admin.domain.popularkeyword.dto.PopularKeywordListDto;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.cuv.admin.domain.popularkeyword.entity.QPopularKeyword.popularKeyword;

@Repository
public class PopularKeywordRepositoryImpl implements PopularKeywordRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public PopularKeywordRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public List<PopularKeywordListDto> searchAllPopularKeyword() {
        return queryFactory
                .select(Projections.fields(PopularKeywordListDto.class,
                        popularKeyword.id,
                        popularKeyword.makerCodeId,
                        popularKeyword.modelCodeId
                ))
                .from(popularKeyword)
                .orderBy(popularKeyword.id.asc())
                .fetch();
    }

}
