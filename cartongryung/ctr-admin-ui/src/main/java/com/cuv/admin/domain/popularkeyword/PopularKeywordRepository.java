package com.cuv.admin.domain.popularkeyword;

import com.cuv.admin.domain.popularkeyword.entity.PopularKeyword;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

public interface PopularKeywordRepository extends
        JpaRepository<PopularKeyword, Long>,
        QuerydslPredicateExecutor<PopularKeyword>,
        PopularKeywordRepositoryCustom {

}
