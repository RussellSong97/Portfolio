package com.cuv.admin.domain.memberadminmemo;

import com.cuv.admin.domain.member.dto.MemberAdminMemoListDto;

import java.util.List;

public interface MemberAdminMemoRepositoryCustom {
    List<MemberAdminMemoListDto> searchMemberAdminMemoList(Long id);
}
