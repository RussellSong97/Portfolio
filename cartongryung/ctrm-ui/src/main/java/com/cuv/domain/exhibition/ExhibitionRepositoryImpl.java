package com.cuv.domain.exhibition;

import com.cuv.common.YN;
import com.cuv.domain.exhibition.dto.ExhibitionDetailDto;
import com.cuv.domain.exhibition.dto.ExhibitionListDto;
import com.cuv.domain.exhibition.dto.ExhibitionSearchDto;
import com.cuv.domain.exhibition.enumset.ExhibitionType;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

import static com.cuv.domain.exhibition.entity.QExhibition.exhibition;
import static org.springframework.util.StringUtils.hasText;

@Repository
public class ExhibitionRepositoryImpl implements ExhibitionRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public ExhibitionRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public List<ExhibitionListDto> searchAllExhibition(String exhibitionType) {
        return queryFactory
                .select(Projections.fields(ExhibitionListDto.class,
                        exhibition.id,
                        exhibition.backgroundColorType,
                        exhibition.content,
                        exhibition.linkUrl,
                        exhibition.linkType,
                        exhibition.pcAttachment,
                        exhibition.mobileAttachment
                ))
                .from(exhibition)
                .where(
                        condExhibitionTypeEq(exhibitionType),
                        condExhibitionEqOngoing(exhibitionType),
                        condViewYnEqY(),
                        condDelYnEqN()
                )
                .orderBy(exhibition.position.asc())
                .fetch();
    }

    @Override
    public List<ExhibitionListDto> searchAllEvent(ExhibitionSearchDto condition) {
        // 이벤트 상태별 정렬 조건
        BooleanExpression exhibitionDateOrder = getExhibitionDateExpression();

        return queryFactory
                .select(Projections.fields(ExhibitionListDto.class,
                        exhibition.id,
                        exhibition.title,
                        exhibition.pcAttachment,
                        exhibition.mobileAttachment,
                        exhibition.exhibitionStartDate,
                        exhibition.exhibitionEndDate,
                        exhibition.hits
                ))
                .from(exhibition)
                .where(
                        condExhibitionTypeEq(ExhibitionType.EVENT.getCode()),
                        condEventStatusEq(condition.getStatus()),
                        condViewYnEqY(),
                        condDelYnEqN()
                )
                .orderBy(exhibitionDateOrder.asc(), exhibition.exhibitionEndDate.desc(), exhibition.id.desc())
                .fetch();
    }

    @Override
    public ExhibitionDetailDto searchExhibitionById(Long id) {
        return queryFactory
                .select(Projections.fields(ExhibitionDetailDto.class,
                        exhibition.id,
                        exhibition.title,
                        exhibition.content,
                        exhibition.exhibitionStartDate,
                        exhibition.exhibitionEndDate,
                        exhibition.hits
                        ))
                .from(exhibition)
                .where(
                        condIdEq(id)
                )
                .fetchFirst();
    }

    /**
     * 이벤트 상태별 정렬식
     *
     * @author SungHa
     */
    private static BooleanExpression getExhibitionDateExpression() {
        return new CaseBuilder()
                .when(exhibition.exhibitionStartDate.lt(LocalDate.now()))
                .then(true)
                .otherwise(false);
    }

    private Predicate condIdEq(Long id) {
        return id != null ? exhibition.id.eq(id) : null;
    }

    private Predicate condExhibitionTypeEq(String exhibitionType) {
        return hasText(exhibitionType) ? exhibition.exhibitionType.eq(ExhibitionType.ofCode(exhibitionType)) : null;
    }

    private Predicate condExhibitionEqOngoing(String exhibitionType) {
        BooleanBuilder builder = new BooleanBuilder();

        return switch (exhibitionType) {
            case "EXH001", "EXH002", "EXH003" ->
                    builder.and(exhibition.exhibitionStartDate.loe(LocalDate.now()).and(exhibition.exhibitionEndDate.goe(LocalDate.now())));
            default -> builder;
        };
    }

    private Predicate condEventStatusEq(String status) {
        BooleanBuilder builder = new BooleanBuilder();

        if (!hasText(status))
            return builder.and(exhibition.exhibitionStartDate.loe(LocalDate.now())
                    .and(exhibition.exhibitionEndDate.goe(LocalDate.now())))
                    .or(exhibition.exhibitionEndDate.lt(LocalDate.now()));

        LocalDate now = LocalDate.now();
        switch (status) {
            case "ongoing":
                builder.and(exhibition.exhibitionStartDate.loe(now).and(exhibition.exhibitionEndDate.goe(now)));
                break;

            case "ended":
                builder.and(exhibition.exhibitionEndDate.lt(now));
                break;

            default:
                break;
        }

        return builder;
    }

    private Predicate condViewYnEqY() {
        return new BooleanBuilder().and(exhibition.viewYn.eq(YN.Y));
    }

    private Predicate condDelYnEqN() {
        return new BooleanBuilder().and(exhibition.delYn.eq(YN.N));
    }
}
