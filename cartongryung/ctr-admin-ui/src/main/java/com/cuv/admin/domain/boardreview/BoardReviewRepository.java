package com.cuv.admin.domain.boardreview;

import com.cuv.admin.domain.boardreview.entity.BoardReview;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

public interface BoardReviewRepository extends JpaRepository<BoardReview, Long>,
        BoardReviewRepositoryCustom,
        QuerydslPredicateExecutor<BoardReview> {
}
