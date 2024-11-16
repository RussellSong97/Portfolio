package com.cuv.domain.linkhistory;

import com.cuv.domain.linkhistory.entity.LinkHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import java.time.LocalDate;
import java.time.LocalDateTime;

public interface LinkHistoryRepository extends JpaRepository<LinkHistory, Long>, QuerydslPredicateExecutor<LinkHistory>, LinkHistoryRepositoryCustom {
    LinkHistory findByMemberIdAndProductId(Long dbMemberId, Long productId);

    Long countByMemberIdAndProductIdAndCreatedAt(Long dbMemberId, Long productId, LocalDateTime today);
}
