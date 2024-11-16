package com.cuv.domain.member;

import com.cuv.common.YN;
import com.cuv.domain.member.entity.Member;
import com.cuv.domain.member.enumset.MemberStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;

public interface MemberRepository extends JpaRepository<Member, Long>,
    MemberRepositoryCustom ,
    QuerydslPredicateExecutor<Member> {
    Member findByLoginIdAndStatusCode(String loginId, MemberStatus memberStatus);

    Member findByEmailAndProviderIdIsNotNullAndStatusCode(String email, MemberStatus memberStatus);

    Member findByLoginIdAndStatusCodeAndProviderIdIsNull(String email, MemberStatus memberStatus);

    @Modifying
    @Query("UPDATE Member mem set mem.agreeMarketingYn = :agreeMarketingYn where mem.loginId = :loginId")
    void updateAgreeMarketingYnByLoginId(@Param("loginId") String loginId, @Param("agreeMarketingYn") YN agreeMarketingYn);
}
