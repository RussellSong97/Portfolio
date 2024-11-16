package com.cuv.domain.review;

import com.cuv.domain.pick.PickRepositoryCustom;
import com.cuv.domain.pick.entity.Pick;
import com.cuv.domain.review.dto.ReviewListDto;
import com.cuv.domain.review.dto.ReviewSearchDto;
import com.cuv.domain.review.entity.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import java.awt.print.Pageable;

public interface ReviewRepository extends
        JpaRepository<Review, Long>,
        ReviewRepositoryCustom,
        QuerydslPredicateExecutor<Review> {

}
