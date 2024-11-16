package com.cuv.domain.memberadmin;


import com.cuv.domain.memberadmin.entity.MemberAdmin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

public interface MemberAdminRepository extends
        JpaRepository<MemberAdmin, Long>,
        QuerydslPredicateExecutor<MemberAdmin>,
        MemberAdminRepositoryCustom {

}
