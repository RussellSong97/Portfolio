package com.cuv.admin.domain.exhibition;

import com.cuv.admin.common.YN;
import com.cuv.admin.domain.exhibition.dto.ExhibitionDetailDto;
import com.cuv.admin.domain.exhibition.dto.ExhibitionListDto;
import com.cuv.admin.domain.exhibition.dto.ExhibitionSearchDto;
import com.cuv.admin.domain.exhibition.enumset.ExhibitionType;
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

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;

import static com.cuv.admin.domain.exhibition.entity.QExhibition.exhibition;
import static org.springframework.util.StringUtils.hasText;

@Repository
public class ExhibitionRepositoryImpl implements ExhibitionRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    public ExhibitionRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public Long searchAllExhibitionCount(String type) {
        return queryFactory
                .select(Wildcard.count)
                .from(exhibition)
                .where(
                        condExhibitionTypeEq(type),
                        condViewYnEqY(),
                        condDelYnEqN()
                )
                .fetchFirst();
    }

    @Override
    public Page<ExhibitionListDto> searchAllExhibition(ExhibitionSearchDto condition, Pageable pageable) {
        List<ExhibitionListDto> content = queryFactory
                .select(Projections.fields(ExhibitionListDto.class,
                        exhibition.id,
                        exhibition.exhibitionType,
                        exhibition.title,
                        exhibition.content,
                        exhibition.pcAttachment,
                        exhibition.exhibitionStartDate,
                        exhibition.exhibitionEndDate,
                        exhibition.createdAt,
                        exhibition.viewYn,
                        exhibition.hits
                ))
                .from(exhibition)
                .where(
                        condExhibitionTypeEq(condition.getType()),
                        condKeywordLike(condition.getType(), condition.getS()),
                        condExhibitionDateBetween(condition.getSdate(), condition.getEdate()),
                        condViewYnEq(condition.getViewYn()),
                        condEventStatusEq(condition.getEventYn()),
                        condDelYnEqN()
                )
                .orderBy(exhibition.position.asc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        JPQLQuery<Long> countQuery = queryFactory
                .select(Wildcard.count)
                .from(exhibition)
                .where(
                        condExhibitionTypeEq(condition.getType()),
                        condKeywordLike(condition.getType(), condition.getS()),
                        condExhibitionDateBetween(condition.getSdate(), condition.getEdate()),
                        condViewYnEq(condition.getViewYn()),
                        condEventStatusEq(condition.getEventYn()),
                        condDelYnEqN()
                );

        return PageableExecutionUtils.getPage(content, pageable, countQuery::fetchCount);
    }

    @Override
    public ExhibitionDetailDto searchExhibitionById(Long id) {
        return queryFactory
                .select(Projections.fields(ExhibitionDetailDto.class,
                        exhibition.id,
                        exhibition.exhibitionType,
                        exhibition.title,
                        exhibition.content,
                        exhibition.linkUrl,
                        exhibition.linkType,
                        exhibition.pcAttachment,
                        exhibition.mobileAttachment,
                        exhibition.exhibitionStartDate,
                        exhibition.exhibitionEndDate,
                        exhibition.backgroundColorType,
                        exhibition.viewYn,
                        exhibition.position
                        ))
                .from(exhibition)
                .where(
                        condIdEq(id)
                )
                .fetchFirst();
    }

    private Predicate condIdEq(Long id) {
        return id != null ? exhibition.id.eq(id) : null;
    }

    private Predicate condExhibitionTypeEq(String exhibitionType) {
        return hasText(exhibitionType) ? exhibition.exhibitionType.eq(ExhibitionType.ofCode(exhibitionType)) : null;
    }

    private Predicate condKeywordLike(String type, String s) {
        BooleanBuilder builder = new BooleanBuilder();
        if (!hasText(type) || !hasText(s)) return builder;

        if (ExhibitionType.TOP_STRIP.getCode().equals(type)) {
            return builder.and(exhibition.content.contains(s));
        } else {
            return builder.and(exhibition.title.contains(s));
        }
    }

    private Predicate condExhibitionDateBetween(String sdate, String edate) {
        BooleanBuilder builder = new BooleanBuilder();

        if (hasText(sdate))
            try {
                builder.and(exhibition.exhibitionStartDate.goe(LocalDate.parse(sdate)));
            } catch (DateTimeParseException ignored) {}

        if (hasText(edate))
            try {
                builder.and(exhibition.exhibitionEndDate.lt(LocalDate.parse(edate).plusDays(1L)));
            } catch (DateTimeParseException ignored) {}

        return builder;
    }

    private Predicate condViewYnEq(String viewYn) {
        return hasText(viewYn) ? exhibition.viewYn.eq(YN.ofYn(viewYn)) : null;
    }

    private Predicate condEventStatusEq(String status) {
        BooleanBuilder builder = new BooleanBuilder();

        if (!hasText(status)) return builder;

        LocalDate now = LocalDate.now();
        switch (status) {
            case "W":
                builder.and(exhibition.exhibitionStartDate.gt(now));
                break;

            case "Y":
                builder.and(exhibition.exhibitionStartDate.loe(now).and(exhibition.exhibitionEndDate.goe(now)));
                break;

            case "N":
                builder.and(exhibition.exhibitionEndDate.lt(now));
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
