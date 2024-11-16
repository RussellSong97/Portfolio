package com.cuv.admin.domain.memberadminmemo;

import com.cuv.admin.domain.memberadminmemo.entity.MemberAdminMemo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

public interface MemberAdminMemoRepository extends
        JpaRepository<MemberAdminMemo, Long>,
        MemberAdminMemoRepositoryCustom,
        QuerydslPredicateExecutor<MemberAdminMemo> {
}
