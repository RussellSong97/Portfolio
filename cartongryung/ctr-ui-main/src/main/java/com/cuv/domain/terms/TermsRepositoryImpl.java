package com.cuv.domain.terms;

import com.cuv.domain.terms.enumset.TermsType;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;

import static com.cuv.domain.terms.entity.QTerms.terms;

@Repository
public class TermsRepositoryImpl implements TermsRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public TermsRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public String searchAllTerms(TermsType termsType) {
        return queryFactory
                .select(terms.content)
                .from(terms)
                .where(terms.termsType.eq(termsType))
                .orderBy(terms.id.desc())
                .fetchFirst();
    }

}
