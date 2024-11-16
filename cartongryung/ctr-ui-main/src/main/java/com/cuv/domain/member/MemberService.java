package com.cuv.domain.member;

import com.cuv.common.JSONResponse;
import com.cuv.common.YN;
import com.cuv.domain.bizgo.AlimTalkService;
import com.cuv.domain.member.dto.MemberInfoDto;
import com.cuv.domain.member.dto.MemberSaveDto;
import com.cuv.domain.member.entity.Member;
import com.cuv.domain.member.enumset.MemberRole;
import com.cuv.domain.member.enumset.MemberStatus;
import com.cuv.domain.member.enumset.RegType;
import com.cuv.domain.membercredentials.MemberCredentialsRepository;
import com.cuv.domain.membercredentials.entity.MemberCredentials;
import com.cuv.domain.membercredentials.enumset.CredentialsType;
import com.cuv.util.JwtUtils;
import com.cuv.domain.email.EmailService;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.regex.Pattern;

import static org.springframework.util.StringUtils.hasText;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class MemberService {
    private final MemberRepository memberRepository;
    private final EmailService emailService;
    private final JwtUtils jwtUtils;
    private final AlimTalkService alimTalkService;
    private final MemberCredentialsRepository memberCredentialsRepository;

    @Value("${server.host}")
    private String serverHost;
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$");
    private static final Pattern PASSWORD_PATTERN = Pattern.compile("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)[A-Za-z\\d]{8,12}$");

    /**
     * 회원가입 | 회원 가입 디비 저장
     * @param dto
     * @return
     * @author 송다운
     */
    public JSONResponse<?> saveJoin(MemberSaveDto dto) {

        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String dbPassword = passwordEncoder.encode(dto.getPassword());

        String dbEmailId = dto.getEmailId() + "@" + dto.getDomainSelect();

        // 이메일 중복 체크
        Member dbMember = memberRepository.findByEmail(dbEmailId);


        if (!hasText(dto.getRealName())) {
            return new JSONResponse<>(400, "이름을 입력해주세요.");
        }
        if (!hasText(dto.getEmailId())) {
            return new JSONResponse<>(400, "이메일을 입력해주세요.");
        }
        if (!isValidEmail(dbEmailId)) {
            return new JSONResponse<>(400, "이메일 형식이 올바르지 않습니다.");
        }
        if (!hasText(dto.getPhoneNumber())) {
            return new JSONResponse<>(400, "휴대폰 번호를 입력해주세요.");
        }
        if (!hasText(dto.getPassword())) {
            return new JSONResponse<>(400, "비밀번호를 입력해주세요.");
        }
        if (!isValidPassword(dto.getPassword())) {
            return new JSONResponse<>(400, "비밀번호는 8~12자리로 대문자, 소문자, 숫자를 포함해야 합니다.");
        }
        if (dbMember != null) {
            return new JSONResponse<>(400, "이미 가입된 이메일입니다." + dbMember.getEmail());
        }


        Member member = Member.builder()
                .regCode(RegType.EMAIL)
                .email(dto.getEmailId() + "@" + dto.getDomainSelect())
                .password(dbPassword)
                .statusCode(MemberStatus.NORMAL)
                .role(MemberRole.USER)
                .realName(dto.getRealName())
                .mobileNumber(dto.getPhoneNumber())
                .yearsOlderYn(YN.Y)
                .agreeTermsYn(YN.Y)
                .agreePrivacyYn(YN.Y)
                .agreeMarketingYn(YN.ofYn(dto.getMarketingYn()))
                .build();

        try {
            memberRepository.save(member);

            // 회원가입 성공 시 알림톡 전송
            Map<String, String> placeholders = new HashMap<>();
            placeholders.put("#{day}", member.getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))); // 가입 날짜를 현재 날짜로 설정

            alimTalkService.sendAlimTalk("code01", placeholders, member.getMobileNumber());
        } catch (Exception e) {
            e.printStackTrace();
            return new JSONResponse<>(500, "회원가입에 실패하였습니다.");
        }

        return new JSONResponse<>(200, "회원가입에 성공하였습니다.");
    }

    private boolean hasText(String text) {
        return text != null && !text.trim().isEmpty();
    }

    private boolean isValidEmail(String email) {
        return EMAIL_PATTERN.matcher(email).matches();
    }

    private boolean isValidPassword(String password) {
        return PASSWORD_PATTERN.matcher(password).matches();
    }

    public MemberInfoDto searchMemberInfo(String username) {
        return memberRepository.searchMemberInfo(username);
    }

    public JSONResponse<?> saveMemberWithdrawal(String username, Map<String, Object> map) {
        String withdrawalReason = map.get("reason").toString();

        Member member = memberRepository.findByEmail(username);

        member.setWithdrawAt(LocalDateTime.now());
        member.setWithdrawReason(withdrawalReason);
        member.setStatusCode(MemberStatus.SECESSION);

        try {
            memberRepository.save(member);

            // 알림톡 전송
            Map<String, String> placeholders = new HashMap<>();
            placeholders.put("#{ID}", member.getEmail());

            // LocalDateTime을 yyyy-MM-dd 형식으로 포맷팅
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            String formattedWithdrawAt = member.getWithdrawAt().format(formatter);
            placeholders.put("#{withdrawAt}", formattedWithdrawAt);

            alimTalkService.sendAlimTalk("code12", placeholders, member.getMobileNumber());

        } catch (Exception e) {
            return new JSONResponse<>(500, "회원탈퇴에 실패하였습니다.");
        }

        return new JSONResponse<>(200, "회원탈퇴에 성공하였습니다.");
    }

    public JSONResponse<?> searchFindPassword(Map<String, Object> map) throws JsonProcessingException {
        String email = map.get("email").toString();
        String memberName = map.get("memberName").toString();
        Member member = memberRepository.findByEmail(email);

        if (member == null)
            throw new IllegalArgumentException("존재하지 않는 계정입니다.");

        if (!member.getRealName().equals(memberName) || !member.getEmail().equals(email))
            throw new IllegalArgumentException("정보가 일치하지 않습니다.");

        String jwtToken = jwtUtils.builder()
                .claim("email", email)
                .claim("memberName", memberName)
                .claim("checkData", false)
                .build();

        Map<String, Object> payload = jwtUtils.getPayload(jwtToken);

        // URL 생성
        String returnUrl = serverHost + "/find/password/reset?token=" + jwtToken;

        emailService.sendEmail(email, memberName, returnUrl);

        return new JSONResponse<>(200, "SUCCESS");
    }

    public JSONResponse<?> searchFindPasswordReset(Map<String, Object> map) {
        String jwtToken = null;
        try {
            jwtToken = map.get("jwtToken").toString();
        } catch (NullPointerException e) {
            return new JSONResponse<>(400, "JWT 토큰이 없습니다.");
        }

        Map<String, Object> beforeData;
        try {
            beforeData = jwtUtils.getPayload(jwtToken);
        } catch (Exception e) {
            return new JSONResponse<>(400, "유효하지 않은 JWT 토큰입니다.");
        }

        String newJwtToken = jwtUtils.builder()
                .claim("email", beforeData.get("email").toString())
                .claim("memberName", beforeData.get("memberName").toString())
                .claim("checkData", true)
                .build();

        Map<String, Object> realData = jwtUtils.getPayload(newJwtToken);

        String password = null;
        String confirmPassword = null;
        try {
            password = map.get("newPassword").toString();
            confirmPassword = map.get("confirmPassword").toString();
        } catch (NullPointerException e) {
            return new JSONResponse<>(400, "비밀번호 데이터가 없습니다.");
        }

        if (!password.equals(confirmPassword)) {
            return new JSONResponse<>(400, "비밀번호가 일치하지 않습니다.");
        }

        String email = realData.get("email").toString();
        Member member = memberRepository.findByEmail(email);
        if (member == null) {
            return new JSONResponse<>(404, "회원 정보를 찾을 수 없습니다.");
        }

        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        member.setPassword(passwordEncoder.encode(password));
        try {
            memberRepository.save(member);
        } catch (Exception e) {
            return new JSONResponse<>(500, "비밀번호 재설정에 실패하였습니다.");
        }

        return new JSONResponse<>(200, "비밀번호 재설정에 성공하였습니다.");
    }

    public JSONResponse<?> searchMypageFindPassword(Map<String, Object> map, UserDetails userDetails) {
        String email = userDetails.getUsername();
        Member member = memberRepository.findByEmail(email);
        String password = map.get("newPassword").toString();
        String confirmPassword = map.get("confirmPassword").toString();

        if (!isValidPassword(password)) {
            return new JSONResponse<>(400, "비밀번호는 8~12자리로 대문자, 소문자, 숫자를 포함해야 합니다.");
        }

        if(!password.equals(confirmPassword)) {
            return new JSONResponse<>(400, "비밀번호가 일치하지 않습니다.");
        }

        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String dbPassword = passwordEncoder.encode(password);

        member.setPassword(dbPassword);
        try {
            memberRepository.save(member);
        } catch (Exception e) {
            return new JSONResponse<>(500, "비밀번호 재설정에 실패하였습니다.");
        }

        return new JSONResponse<>(200, "SUCCESS");
    }

    public JSONResponse<?> saveJoinSns(Map<String, Object> map, UserDetails userDetails) {
        String phoneNumber = map.get("phoneNumber").toString();

        Member member = memberRepository.findByEmail(userDetails.getUsername());
        member.setMobileNumber(phoneNumber);
        member.setAuthYn(YN.Y);

        try {
            memberRepository.save(member);
        } catch (Exception e) {
            return new JSONResponse<>(500, "회원가입에 실패하였습니다.");
        }

        return new JSONResponse<>(200, "회원가입에 성공하였습니다.");
    }

    /**
     * 회원가입 -> 인증번호 전송 / 인증 테이블 저장
     * @param map
     * @return
     */
    public JSONResponse<?> checkPhone(Map<String, Object> map, UserDetails userDetails) {
        String phoneNumber = map.get("phoneNumber").toString();
        String type = map.get("type").toString();
        String authNumber = generateRandomNumber();
        UUID uuidAuthNumber = generateUUIDFromAuthNumber(authNumber);
        List<MemberCredentials> dbMeberCredentials = memberCredentialsRepository.findByPhoneNumber(phoneNumber);

        // userDetails가 null인 경우 member를 null로 설정
        Member member = null;
        Long memberId = null;
        if (userDetails != null) {
            member = memberRepository.findByEmail(userDetails.getUsername());
            if (member != null) {
                memberId = member.getId();
            }
        }

        // 재전송 확인을 위한 값 변경
        if (dbMeberCredentials != null) {
            for (MemberCredentials memberCredentials : dbMeberCredentials) {
                memberCredentials.setType(CredentialsType.ofCode(type));
                memberCredentials.setUseYn(YN.Y);
                memberCredentialsRepository.save(memberCredentials);
            }
        }

        Map<String, String> bizData = new HashMap<>();
        bizData.put("#{secretCode}", authNumber);

        MemberCredentials memberCredentials = MemberCredentials.builder()
                .memberId(memberId)  // memberId를 설정
                .phoneNumber(phoneNumber)
                .type(CredentialsType.ofCode(type))
                .authToken(uuidAuthNumber.toString())
                .authNumber(authNumber)
                .expiredAt(LocalDateTime.now().plusMinutes(3))
                .build();

        try {
            memberCredentialsRepository.save(memberCredentials);
        } catch (Exception e) {
            log.error("인증번호 저장에 실패하였습니다.", e.getMessage());
            return new JSONResponse<>(500, "인증번호 저장에 실패하였습니다.");
        }

        try {
            alimTalkService.sendAlimTalk("code02", bizData, phoneNumber);
        } catch (Exception e) {
            log.error("인증번호 전송에 실패하였습니다.", e.getMessage());
            return new JSONResponse<>(500, "인증번호 전송에 실패하였습니다.");
        }

        return new JSONResponse<>(200, "SUCCESS");
    }

    /**
     * 랜덤 6자리 uuid (확인용)
     * @param authNumber
     * @return
     */
    public static UUID generateUUIDFromAuthNumber(String authNumber) {
        // UUID의 처음 부분을 임의의 숫자로 대체
        String uuidString = authNumber + UUID.randomUUID().toString().substring(6);
        // UUID 형식으로 변환
        return UUID.fromString(uuidString);
    }

    // 랜덤 숫자 6자리 생성
    public static String generateRandomNumber() {
        Random random = new Random();
        StringBuilder authNumber = new StringBuilder();
        for (int i = 0; i < 6; i++) {
            authNumber.append(random.nextInt(10));
        }
        // UUID 생성 후, 기존 숫자 문자열에 결합
        return authNumber.toString();
    }

    /**
     * 인증 번호 확인
     * @param map
     * @return
     */
    public JSONResponse<?> checkNumber(Map<String, Object> map) {
        String numberCheck = map.get("numberCheck").toString();
        System.out.println("numberCheck : " + numberCheck);
        String phoneNumber = map.get("phoneNumber").toString();
        System.out.println("phoneNumber : " + phoneNumber);

        MemberCredentials memberCredentials = memberCredentialsRepository.findByAuthNumberAndPhoneNumberOrderByCreatedAtDesc(numberCheck, phoneNumber);

        if (memberCredentials == null || memberCredentials.getExpiredAt().isBefore(LocalDateTime.now()) || "Y".equals(memberCredentials.getUseYn().getYn())) {
            return new JSONResponse<>(400,  memberCredentials == null ? "인증번호가 유효하지 않습니다." : "인증번호가 만료되었습니다.");
        }

        return new JSONResponse<>(200, "인증이 완료되었습니다.");
    }

    /**
     * 회원정보 인증번호 확인 로직 (회원 휴대폰 업데이트 로직 추가)
     * @param map
     * @param userDetails
     * @return
     */
    public JSONResponse<?> checkNumberInfo(Map<String, Object> map, UserDetails userDetails) {
        Member member = memberRepository.findByEmail(userDetails.getUsername());
        String numberCheck = map.get("numberCheck").toString();
        String phoneNumber = map.get("phoneNumber").toString();

        MemberCredentials memberCredentials = memberCredentialsRepository.findByAuthNumberAndPhoneNumberOrderByCreatedAtDesc(numberCheck, phoneNumber);

        if (memberCredentials == null || memberCredentials.getExpiredAt().isBefore(LocalDateTime.now()) || "Y".equals(memberCredentials.getUseYn().getYn())) {
            return new JSONResponse<>(400,  memberCredentials == null ? "인증번호가 유효하지 않습니다." : "인증번호가 만료되었습니다.");
        } else {
            member.setMobileNumber(map.get("phoneNumber").toString());
            try {
                memberRepository.save(member);
            } catch (Exception e) {
                return new JSONResponse<>(500, "회원정보 변경을 실패하였습니다.");
            }

            return new JSONResponse<>(200, "회원 정보를 변경했습니다.");
        }
    }
}
