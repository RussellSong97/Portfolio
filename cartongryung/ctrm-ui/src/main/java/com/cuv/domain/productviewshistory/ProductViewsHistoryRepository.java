package com.cuv.domain.productviewshistory;


import com.cuv.domain.productviewshistory.entity.ProductViewsHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;

public interface ProductViewsHistoryRepository extends
        JpaRepository<ProductViewsHistory, Long>,
        QuerydslPredicateExecutor<ProductViewsHistory>,
        ProductViewsHistoryRepositoryCustom {
    boolean existsByProductIdAndMemberId(Long productId, Long memberId);

    /**
     * 생성된 지 createdAt 기준 14일 된 데이터 삭제
     * @author 이상빈
     */
    @Modifying
    @Query("DELETE FROM ProductViewsHistory pvh WHERE pvh.createdAt <= :dateToDelete")
    void deleteOldRecords(@Param("dateToDelete") LocalDateTime dateToDelete);
}
