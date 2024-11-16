package com.cuv.admin.domain.memberadmin;

import com.cuv.admin.domain.memberadmin.dto.MemberAdminDetailDto;
import com.cuv.admin.domain.memberadmin.dto.MemberAdminListDto;
import com.cuv.admin.domain.memberadmin.dto.MemberAdminSearchDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface MemberAdminRepositoryCustom {
    Page<MemberAdminListDto> searchAllMemberAdmin(MemberAdminSearchDto condition, Pageable pageable);

    MemberAdminDetailDto searchMemberAdminById(Long id);

    List<MemberAdminListDto> searchAllMemberDealer(String role);

    Long searchMemberDealerOrderByAssignmentAt(String role);

    List<MemberAdminListDto> searchAllMemberDealerAndMemberCounselor(String role);

    List<MemberAdminListDto> searchAllMemberCounselor(String role);
}
