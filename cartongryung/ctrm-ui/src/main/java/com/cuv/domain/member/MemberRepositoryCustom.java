package com.cuv.domain.member;

import com.cuv.domain.member.dto.MemberInfoDto;
import com.cuv.domain.member.entity.Member;

public interface MemberRepositoryCustom {
    MemberInfoDto searchMemberInfo(String loginId);

    MemberInfoDto searchEmailAndProviderIdAndMemberStatus(String email, String providerId);
}
