package com.cuv.domain.boardfaq;

import com.cuv.domain.boardfaq.entity.BoardFaq;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

public interface BoardFaqRepository
        extends JpaRepository<BoardFaq, Long>,
        QuerydslPredicateExecutor<BoardFaq>,
        BoardFaqRepositoryCustom {
}
