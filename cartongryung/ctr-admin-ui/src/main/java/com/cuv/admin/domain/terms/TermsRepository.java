package com.cuv.admin.domain.terms;

import com.cuv.admin.domain.terms.entity.Terms;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

public interface TermsRepository extends
        JpaRepository<Terms, Long>,
        QuerydslPredicateExecutor<Terms>,
        TermsRepositoryCustom {
}
