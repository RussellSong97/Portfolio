package com.cuv.domain.pick;

import com.cuv.common.YN;
import com.cuv.domain.pick.entity.Pick;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PickRepository extends
        JpaRepository<Pick, Long>,
        PickRepositoryCustom,
        QuerydslPredicateExecutor<Pick> {

    Pick findPickById(Long pickId);

    @Query("SELECT p FROM Pick p WHERE p.memberId = :memberId")
    List<Pick> getPicksById(@Param("memberId") Long memberId);

    Long countByMemberIdAndReadYn(Long memberId, YN yn);

    List<Pick> findByMemberIdAndProductId(Long id, Long productId);

    @Query("SELECT p.productId FROM Pick p WHERE p.memberId = :memberId and p.delYn = 'N'")
    List<Long> findPickedProductIdsByMemberId(@Param("memberId") Long memberId);

    @Query("SELECT p.productId FROM Pick p WHERE p.memberId = :memberId and p.productId = :productId and p.delYn = 'Y'")
    List<Long> findDelYNByMemberId(@Param("memberId") Long memberId, @Param("productId") Long productId);

}
