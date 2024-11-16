package com.cuv.domain.member;

import com.cuv.domain.member.dto.MemberInfoDto;

public interface MemberRepositoryCustom {
    MemberInfoDto searchMemberInfo(String username);
}
