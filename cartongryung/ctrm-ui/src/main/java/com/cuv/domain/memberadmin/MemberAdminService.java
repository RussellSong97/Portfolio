package com.cuv.domain.memberadmin;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * 관리자 회원 서비스 (내 차 팔기 등록 시 차량에 딜러 배정)
 */
@Service
@RequiredArgsConstructor
public class MemberAdminService {

    private final MemberAdminRepository memberAdminRepository;

    /**
     * 사용자 | 내 차 팔기 > 딜러 배정
     *
     * @param role 회원 권한
     * @author SungHa
     */
    public Long searchMemberDealerOrderByAssignmentAt(String role) {
        return memberAdminRepository.searchMemberDealerOrderByAssignmentAt(role);
    }

}
