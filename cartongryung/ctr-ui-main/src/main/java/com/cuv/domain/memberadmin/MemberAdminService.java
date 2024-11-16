package com.cuv.domain.memberadmin;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@RequiredArgsConstructor
public class MemberAdminService {

    private final MemberAdminRepository memberAdminRepository;

    /**
     * 관리자 | 내 차 구입 > 연락 가능 문의 > 검색 조건 - 딜러
     * 내 차 팔기 > 검색 조건 - 딜러
     * 내 차 팔기 > 상세 - 문의 내용
     *
     * @param role 회원 권한
     * @author SungHa
     */
    public List<Long> searchAllMemberDealer(String role) {
    return memberAdminRepository.searchAllMemberDealer(role);
    }

}
