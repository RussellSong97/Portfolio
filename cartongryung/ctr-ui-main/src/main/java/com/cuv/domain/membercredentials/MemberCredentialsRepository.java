package com.cuv.domain.membercredentials;

import com.cuv.domain.membercredentials.entity.MemberCredentials;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import java.util.List;

public interface MemberCredentialsRepository extends JpaRepository<MemberCredentials, Long>, MemberCredentialsRepositoryCustom, QuerydslPredicateExecutor<MemberCredentials> {


    List<MemberCredentials> findByPhoneNumber(String phoneNumber);

    MemberCredentials findByAuthNumberAndPhoneNumberOrderByCreatedAtDesc(String numberCheck, String phoneNumber);
}
