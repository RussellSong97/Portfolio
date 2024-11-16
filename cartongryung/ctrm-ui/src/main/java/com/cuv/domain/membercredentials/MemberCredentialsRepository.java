package com.cuv.domain.membercredentials;

import com.cuv.domain.membercredentials.entity.MemberCredentials;
import com.cuv.domain.membercredentials.enumset.CredentialsType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import java.time.LocalDateTime;
import java.util.List;

public interface MemberCredentialsRepository extends JpaRepository<MemberCredentials, Long>, MemberCredentialsRepositoryCustom, QuerydslPredicateExecutor<MemberCredentials> {

    MemberCredentials findByAuthNumberAndPhoneNumberOrderByCreatedAtDesc(String numberCheck, String phoneNumber);

    MemberCredentials findTopByPhoneNumberOrderByCreatedAtDesc(String phoneNumber);
}
