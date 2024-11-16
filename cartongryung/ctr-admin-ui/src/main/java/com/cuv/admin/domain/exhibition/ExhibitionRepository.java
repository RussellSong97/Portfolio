package com.cuv.admin.domain.exhibition;

import com.cuv.admin.domain.exhibition.entity.Exhibition;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

public interface ExhibitionRepository
        extends JpaRepository<Exhibition, Long>,
        ExhibitionRepositoryCustom,
        QuerydslPredicateExecutor<Exhibition> {

}
