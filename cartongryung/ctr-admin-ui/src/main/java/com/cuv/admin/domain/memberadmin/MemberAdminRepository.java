package com.cuv.admin.domain.memberadmin;

import com.cuv.admin.domain.memberadmin.entity.MemberAdmin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface MemberAdminRepository extends
        JpaRepository<MemberAdmin, Long>,
        QuerydslPredicateExecutor<MemberAdmin>,
        MemberAdminRepositoryCustom {

    @Query("SELECT ma FROM MemberAdmin ma " +
            "WHERE ma.loginId = :loginId " +
            "and ma.delYn = 'N'")
    Optional<MemberAdmin> findByLoginId(@Param("loginId") String loginId);

}
