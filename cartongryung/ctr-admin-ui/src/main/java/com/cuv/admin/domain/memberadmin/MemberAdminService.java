package com.cuv.admin.domain.memberadmin;

import com.cuv.admin.common.YN;
import com.cuv.admin.domain.attachment.AttachmentService;
import com.cuv.admin.domain.authoritymenu.AuthorityMenuRepository;
import com.cuv.admin.domain.authoritymenu.entity.AuthorityMenu;
import com.cuv.admin.domain.authoritymenuemployee.AuthorityMenuEmployeeRepository;
import com.cuv.admin.domain.authoritymenuemployee.entity.AuthorityMenuEmployee;
import com.cuv.admin.domain.member.enumset.MemberRole;
import com.cuv.admin.domain.memberadmin.dto.MemberAdminDetailDto;
import com.cuv.admin.domain.memberadmin.dto.MemberAdminListDto;
import com.cuv.admin.domain.memberadmin.dto.MemberAdminSaveDto;
import com.cuv.admin.domain.memberadmin.dto.MemberAdminSearchDto;
import com.cuv.admin.domain.memberadmin.entity.MemberAdmin;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.util.StringUtils.hasText;

/**
 * 관리자 계열 직군 관리(관리자 등록, 조회, 삭제, 수정 등)
 */
@Service
@RequiredArgsConstructor
public class MemberAdminService {

    private final MemberAdminRepository memberAdminRepository;

    private final AuthorityMenuRepository authorityMenuRepository;

    private final AuthorityMenuEmployeeRepository authorityMenuEmployeeRepository;

    private final AttachmentService attachmentService;

    /**
     * 관리자 | 기타 > 딜러 관리, 상담사 관리, 운영자 관리 > 등록
     *
     * @param requestDto 등록할 글 정보를 담은 DTO
     * @author SungHa
     */
    @Transactional
    public boolean adminEtcManagementWriteProc(MemberAdminSaveDto requestDto) {
        if (!hasText(requestDto.getLoginId()))
            throw new IllegalArgumentException("아이디를 입력해주세요.");

        memberAdminRepository.findByLoginId(requestDto.getLoginId())
                .ifPresent(loginId -> {
                    throw new IllegalArgumentException("이미 등록된 아이디입니다.");
                });

        if (!hasText(requestDto.getPassword()))
            throw new IllegalArgumentException("비밀번호를 입력해주세요.");
        if (!hasText(requestDto.getConfirmPassword()))
            throw new IllegalArgumentException("비밀번호 확인을 입력해주세요.");
        if (!ObjectUtils.nullSafeEquals(requestDto.getPassword(), requestDto.getConfirmPassword()))
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        if (!hasText(requestDto.getRealName()))
            throw new IllegalArgumentException("이름을 입력해주세요.");
        if (!hasText(requestDto.getMobileNumber()))
            throw new IllegalArgumentException("휴대폰 번호를 입력해주세요.");
        if (!requestDto.getMobileNumber().matches("\\d+"))
            throw new IllegalArgumentException("휴대폰 번호 형식이 올바르지 않습니다.");
        if (!hasText(requestDto.getUseYn()))
            throw new IllegalArgumentException("사용 여부를 선택해주세요.");
        if (!requestDto.getUseYn().matches("^Y|N$"))
            throw new IllegalArgumentException("사용 여부 선택이 잘못되었습니다.");

        if (MemberRole.DEALER.getRole().equals(requestDto.getRole())) {
            if (!hasText(requestDto.getEmployeeNumber()))
                throw new IllegalArgumentException("사원증 번호를 입력해주세요.");
            if (!hasText(requestDto.getFileUUID()))
                throw new IllegalArgumentException("프로필 이미지를 첨부해주세요.");
            if (!hasText(requestDto.getIntro()))
                throw new IllegalArgumentException("한줄 소개를 입력해주세요.");
        }

        // 딜러 회원이거나 상담사 회원일 때만 사용 권한 여부 존재
        List<AuthorityMenuEmployee> authorityMenuEmployees = new ArrayList<>();
        if (!MemberRole.ADMIN.getRole().equals(requestDto.getRole())) {
            if (requestDto.getAuthorityMenus() == null || requestDto.getAuthorityMenus().isEmpty())
                throw new IllegalArgumentException("1개 이상의 메뉴 권한을 선택해주세요.");

            List<AuthorityMenu> authorityMenus = authorityMenuRepository.findAllAuthorityMenuByManageYn();
            authorityMenuEmployees = authorityMenus.stream()
                    .filter(authorityMenu -> requestDto.getAuthorityMenus().contains(authorityMenu.getId()))
                    .map(authorityMenu -> AuthorityMenuEmployee.builder()
                            .loginId(requestDto.getLoginId())
                            .authorityMenuId(authorityMenu.getId())
                            .build())
                    .collect(Collectors.toList());

            if (authorityMenuEmployees.isEmpty())
                throw new IllegalArgumentException("메뉴 권한을 찾을 수 없습니다.");
        }

        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encodePassword = passwordEncoder.encode(requestDto.getPassword());

        MemberAdmin memberAdmin = MemberAdmin.builder()
                .loginId(requestDto.getLoginId())
                .password(encodePassword)
                .role(MemberRole.ofRole(requestDto.getRole()))
                .realName(requestDto.getRealName())
                .mobileNumber(requestDto.getMobileNumber())
                .employeeNumber(requestDto.getEmployeeNumber())
                .profileImageJson(attachmentService.findUploadFileDtoByUUID(requestDto.getFileUUID()))
                .intro(requestDto.getIntro())
                .useYn(YN.ofYn(requestDto.getUseYn()))
                .build();

        memberAdminRepository.save(memberAdmin);

        // 해당하는 이용권한 있는 회원 있을 때
        if (!authorityMenuEmployees.isEmpty()) {
            authorityMenuEmployeeRepository.deleteAllByLoginId(requestDto.getLoginId());
            authorityMenuEmployeeRepository.saveAll(authorityMenuEmployees);
        }

        return true;
    }

    /**
     * 관리자 | 기타 > 딜러 관리, 상담사 관리, 운영자 관리 > 목록
     *
     * @param condition 회원 권한을 담은 DTO
     * @author SungHa
     */
    public Page<MemberAdminListDto> searchAllMemberAdmin(MemberAdminSearchDto condition, Pageable pageable) {
        return memberAdminRepository.searchAllMemberAdmin(condition, pageable);
    }

    /**
     * 관리자 | 기타 > 딜러 관리, 상담사 관리, 운영자 관리 > 상세
     *
     * @param id 회원 시퀀스
     * @author SungHa
     */
    public MemberAdminDetailDto searchMemberAdminById(Long id) {
        return memberAdminRepository.searchMemberAdminById(id);
    }

    /**
     * 관리자 | 기타 > 딜러 관리, 상담사 관리, 운영자 관리 > 수정
     *
     * @param requestDto 등록할 글 정보를 담은 DTO
     * @author SungHa
     */
    @Transactional
    public boolean adminEtcManagementEditProc(MemberAdminSaveDto requestDto) {
        MemberAdmin memberAdmin = memberAdminRepository
                .findById(requestDto.getId())
                .orElseThrow(() -> new IllegalArgumentException("회원을 찾을 수가 없습니다."));

        if (!hasText(requestDto.getPassword()))
            throw new IllegalArgumentException("비밀번호를 입력해주세요.");
        if (!hasText(requestDto.getConfirmPassword()))
            throw new IllegalArgumentException("비밀번호 확인을 입력해주세요.");
        if (!ObjectUtils.nullSafeEquals(requestDto.getPassword(), requestDto.getConfirmPassword()))
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        if (!hasText(requestDto.getRealName()))
            throw new IllegalArgumentException("이름을 입력해주세요.");
        if (!hasText(requestDto.getMobileNumber()))
            throw new IllegalArgumentException("휴대폰 번호를 입력해주세요.");
        if (!requestDto.getMobileNumber().matches("\\d+"))
            throw new IllegalArgumentException("휴대폰 번호 형식이 올바르지 않습니다.");
        if (!hasText(requestDto.getUseYn()))
            throw new IllegalArgumentException("사용 여부를 선택해주세요.");
        if (!requestDto.getUseYn().matches("^Y|N$"))
            throw new IllegalArgumentException("사용 여부 선택이 잘못되었습니다.");

        if (MemberRole.DEALER.getRole().equals(requestDto.getRole())) {
            if (!hasText(requestDto.getEmployeeNumber()))
                throw new IllegalArgumentException("사원증 번호를 입력해주세요.");
            if (!hasText(requestDto.getFileUUID()))
                throw new IllegalArgumentException("프로필 이미지를 첨부해주세요.");
            if (!hasText(requestDto.getIntro()))
                throw new IllegalArgumentException("한줄 소개를 입력해주세요.");
        }

        // 딜러 회원이거나 상담사 회원일 때만 사용 권한 여부 존재
        List<AuthorityMenuEmployee> authorityMenuEmployees = new ArrayList<>();
        if (!MemberRole.ADMIN.getRole().equals(requestDto.getRole())) {
            if (requestDto.getAuthorityMenus() == null || requestDto.getAuthorityMenus().isEmpty())
                throw new IllegalArgumentException("1개 이상의 메뉴 권한을 선택해주세요.");

            List<AuthorityMenu> authorityMenus = authorityMenuRepository.findAllAuthorityMenuByManageYn();
            authorityMenuEmployees = authorityMenus.stream()
                    .filter(authorityMenu -> requestDto.getAuthorityMenus().contains(authorityMenu.getId()))
                    .map(authorityMenu -> AuthorityMenuEmployee.builder()
                            .loginId(memberAdmin.getLoginId())
                            .authorityMenuId(authorityMenu.getId())
                            .build())
                    .collect(Collectors.toList());

            if (authorityMenuEmployees.isEmpty())
                throw new IllegalArgumentException("메뉴 권한을 찾을 수 없습니다.");
        }

        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encodePassword = passwordEncoder.encode(requestDto.getPassword());

        memberAdmin.setPassword(encodePassword);
        memberAdmin.setRealName(requestDto.getRealName());
        memberAdmin.setMobileNumber(requestDto.getMobileNumber());
        memberAdmin.setEmployeeNumber(requestDto.getEmployeeNumber());
        memberAdmin.setProfileImageJson(attachmentService.findUploadFileDtoByUUID(requestDto.getFileUUID()));
        memberAdmin.setIntro(requestDto.getIntro());
        memberAdmin.setUseYn(YN.ofYn(requestDto.getUseYn()));

        // 해당하는 이용권한 있는 회원 있을 때
        if (!authorityMenuEmployees.isEmpty()) {
            authorityMenuEmployeeRepository.deleteAllByLoginId(memberAdmin.getLoginId());
            authorityMenuEmployeeRepository.saveAll(authorityMenuEmployees);
        }

        return true;
    }

    /**
     * 관리자 | 기타 > 딜러 관리, 상담사 관리, 운영자 관리 > 삭제
     *
     * @param id 임원 시퀀스
     * @author SungHa
     */
    @Transactional
    public boolean adminEtcManagementDeleteProc(Long id) {
        MemberAdmin memberAdmin = memberAdminRepository
                .findById(id)
                .orElseThrow(() -> new IllegalArgumentException("회원을 찾을 수 없습니다."));

        memberAdmin.setDelYn(YN.Y);

        // 사용 권한 삭제
        List<AuthorityMenuEmployee> authorityMenuEmployees = authorityMenuEmployeeRepository.findAuthorityMenuEmployeeByLoginId(memberAdmin.getLoginId());
        authorityMenuEmployeeRepository.deleteAllByLoginId(memberAdmin.getLoginId());
        authorityMenuEmployeeRepository.saveAll(authorityMenuEmployees);

        return true;
    }

    /**
     * 관리자 | 내 차 구입 > 연락 가능 문의 > 검색 조건 - 딜러
     * 내 차 팔기 > 검색 조건 - 딜러
     * 내 차 팔기 > 상세 - 문의 내용
     *
     * @param role 회원 권한
     * @author SungHa
     */
    public List<MemberAdminListDto> searchAllMemberDealer(String role) {
        return memberAdminRepository.searchAllMemberDealer(role);
    }

    /**
     *  관리자 | 차량관리 > 매물 연동 관리 > 차량등록 > 딜러 배정
     *
     * @param role 회원 권한
     * @author SungHa
     */
    public Long searchMemberDealerOrderByAssignmentAt(String role) {
        return memberAdminRepository.searchMemberDealerOrderByAssignmentAt(role);
    }

    /**
     * 관리자 | 내 차 구입 > 연락 가능 문의 > 검색 조건 - 상담원 (딜러, 상담원)
     *
     * @param role 회원 권한
     * @author SungHa
     */
    public List<MemberAdminListDto> searchAllMemberDealerAndMemberCounselor(String role) {
        return memberAdminRepository.searchAllMemberDealerAndMemberCounselor(role);
    }

    /**
     * 관리자 | 통계 > 상담원 통계 > 상담원 선택
     *
     * @param role 회원 권한
     * @author 박성민
     */
    public List<MemberAdminListDto> searchAllMemberCounselor(String role) {
        return memberAdminRepository.searchAllMemberCounselor(role);
    }

}
