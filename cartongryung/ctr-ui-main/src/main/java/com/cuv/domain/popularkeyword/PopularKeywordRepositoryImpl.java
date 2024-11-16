package com.cuv.domain.popularkeyword;

import com.cuv.domain.linkcode.entity.QLinkCode;
import com.cuv.domain.popularkeyword.dto.PopularKeywordListDto;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.cuv.domain.popularkeyword.entity.QPopularKeyword.popularKeyword;


@Repository
public class PopularKeywordRepositoryImpl implements PopularKeywordRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public PopularKeywordRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public List<PopularKeywordListDto> searchAllPopularKeyword() {
        QLinkCode maker = new QLinkCode("maker");
        QLinkCode model = new QLinkCode("model");
        QLinkCode detailModel = new QLinkCode("detailModel");

        return queryFactory
                .select(Projections.fields(PopularKeywordListDto.class,
                        popularKeyword.makerCodeId,
                        popularKeyword.modelCodeId,
                        popularKeyword.detailModelCodeId,
                        maker.linkDataNm.as("makerName"),
                        model.linkDataNm.as("modelName"),
                        detailModel.linkDataNm.as("detailModelName"),
                        popularKeyword.createdAt
                ))
                .from(popularKeyword)
                .leftJoin(maker)
                .on(maker.id.eq(popularKeyword.makerCodeId))
                .leftJoin(model)
                .on(model.id.eq(popularKeyword.modelCodeId))
                .leftJoin(detailModel)
                .on(detailModel.id.eq(popularKeyword.detailModelCodeId))
                .where(popularKeyword.makerCodeId.isNotNull())
                .orderBy(popularKeyword.id.asc())
                .fetch();
    }

}
