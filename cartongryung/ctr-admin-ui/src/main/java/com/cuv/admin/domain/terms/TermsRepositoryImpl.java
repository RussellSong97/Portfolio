package com.cuv.admin.domain.terms;

import com.cuv.admin.domain.terms.dto.TermsSaveDto;
import com.cuv.admin.domain.terms.enumset.TermsType;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;

import static com.cuv.admin.domain.terms.entity.QTerms.terms;
import static org.springframework.util.StringUtils.hasText;

@Repository
public class TermsRepositoryImpl implements TermsRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public TermsRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public TermsSaveDto searchAllTerms(TermsSaveDto condition) {
        return queryFactory
                .select(Projections.fields(TermsSaveDto.class,
                        terms.id,
                        terms.termsType,
                        terms.content
                        ))
                .from(terms)
                .where(
                        condTermsTypeEq(condition.getType())
                )
                .orderBy(terms.id.desc())
                .fetchFirst();
    }

    private Predicate condTermsTypeEq(String termsType) {
        return hasText(termsType) ? terms.termsType.eq(TermsType.ofCode(termsType)) : null;
    }

}
