package com.cuv.domain.linkcode;


import com.cuv.domain.linkcode.entity.LinkCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

public interface LinkCodeRepository extends
        JpaRepository<LinkCode, Long>,
        QuerydslPredicateExecutor<LinkCode>,
        LinkCodeRepositoryCustom {

}
