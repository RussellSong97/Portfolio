package com.cuv.domain.terms;

import com.cuv.domain.terms.entity.Terms;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

public interface TermsRepository extends
        JpaRepository<Terms, Long>,
        QuerydslPredicateExecutor<Terms>,
        TermsRepositoryCustom {
}
