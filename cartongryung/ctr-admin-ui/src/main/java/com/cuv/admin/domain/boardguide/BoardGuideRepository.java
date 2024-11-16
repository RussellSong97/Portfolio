package com.cuv.admin.domain.boardguide;

import com.cuv.admin.domain.boardguide.entity.BoardGuide;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

public interface BoardGuideRepository extends
        JpaRepository<BoardGuide, Long>,
        QuerydslPredicateExecutor<BoardGuide>,
        BoardGuideRepositoryCustom {
}
