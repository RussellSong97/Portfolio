package com.cuv.domain.member;

import com.cuv.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

public interface MemberRepository extends JpaRepository<Member, Long>,
    MemberRepositoryCustom ,
    QuerydslPredicateExecutor<Member> {
    Member findByEmail(String email);
}
