package com.cuv.admin.domain.boardnotice;

import com.cuv.admin.domain.boardnotice.entity.BoardNotice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

public interface BoardNoticeRepository extends
    JpaRepository<BoardNotice, Long>,
    QuerydslPredicateExecutor<BoardNotice>,
    BoardNoticeRepositoryCustom {

}
