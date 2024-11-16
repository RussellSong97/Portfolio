package com.cuv.admin.domain.member;

import com.cuv.admin.domain.member.entity.Member;
import com.cuv.admin.domain.notificatonMember.dto.NotificatonMemberMemberIdAndTokenDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface MemberRepository extends
        JpaRepository<Member, Long>,
        MemberRepositoryCustom ,
        QuerydslPredicateExecutor<Member> {

    @Query("SELECT m FROM Member m WHERE m.mobileNumber = :mobileNumber AND m.delYn = 'N'")
    Optional<Member> findByMobileNumber(@Param("mobileNumber") String mobileNumber);

    @Query("SELECT m.fcmToken FROM Member m WHERE m.id = :memberId AND m.delYn = 'N'")
    String findFcmTokenById(@Param("memberId") Long memberId);

    @Query("SELECT m.realName FROM Member m WHERE m.id = :memberId AND m.delYn = 'N'")
    String findRealNameById(@Param("memberId") Long memberId);

}
