package com.cuv.domain.boardreview;

import com.cuv.domain.boardreview.entity.BoardReview;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

public interface BoardReviewRepository extends
        JpaRepository<BoardReview, Long>,
        BoardReviewRepositoryCustom,
        QuerydslPredicateExecutor<BoardReview> {

}
