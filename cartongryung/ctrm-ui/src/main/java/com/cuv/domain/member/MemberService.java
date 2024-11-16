package com.cuv.domain.member;

import com.cuv.common.JSONResponse;
import com.cuv.common.YN;
import com.cuv.domain.bizgo.AlimTalkService;
import com.cuv.domain.email.EmailService;
import com.cuv.domain.member.dto.MemberGoogleInfoDto;
import com.cuv.domain.member.dto.MemberInfoDto;
import com.cuv.domain.member.dto.MemberSaveDto;
import com.cuv.domain.member.entity.Member;
import com.cuv.domain.member.enumset.MemberRole;
import com.cuv.domain.member.enumset.MemberStatus;
import com.cuv.domain.member.enumset.RegType;
import com.cuv.domain.membercredentials.MemberCredentialsRepository;
import com.cuv.domain.membercredentials.entity.MemberCredentials;
import com.cuv.domain.membercredentials.enumset.CredentialsType;
import com.cuv.domain.notification.NotificationRepository;
import com.cuv.domain.notification.entity.Notification;
import com.cuv.domain.notificationMember.NotificationMemberRepository;
import com.cuv.domain.notificationMember.entity.NotificationMember;
import com.cuv.util.JwtUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.regex.Pattern;

import static org.springframework.util.StringUtils.hasText;

/**
 * 사용자 회원 서비스 (회원 조회, 수정 ,삭제)
 */
@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class MemberService {
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$");
    private static final Pattern PASSWORD_PATTERN = Pattern.compile("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@#$%^&+=!])[A-Za-z\\d@#$%^&+=!]{8,12}$");
    private final MemberRepository memberRepository;
    private final EmailService emailService;
    private final JwtUtils jwtUtils;
    private final AlimTalkService alimTalkService;
    private final MemberCredentialsRepository memberCredentialsRepository;
    private final NotificationRepository notificationRepository;
    private final NotificationMemberRepository notificationMemberRepository;
    @Value("${cuv.service-url}")
    private String serverHost;

    /**
     * 랜덤 6자리 uuid (확인용)
     * @param authNumber 인증 번호
     * @return
     */
    public static UUID generateUUIDFromAuthNumber(String authNumber) {
        String uuidString = authNumber + UUID.randomUUID().toString().substring(6);
        return UUID.fromString(uuidString);
    }

    /**
     * 인증번호 (랜덤 6자리 번호)
     * @return
     */
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
     * 회원가입 | 회원 가입 디비 저장
     * @param dto 회원 저장 정보
     * @return
     * @author 송다운
     */
    public JSONResponse<?> saveJoin(MemberSaveDto dto) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String dbPassword = passwordEncoder.encode(dto.getPassword());

        String domainSelect = dto.getDomainSelect().replace(",", "");
        String dbEmailId = dto.getEmailId() + "@" + domainSelect;

        // 이메일 중복 체크
        Member dbMember = memberRepository.findByLoginIdAndStatusCode(dbEmailId, MemberStatus.NORMAL);

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
            return new JSONResponse<>(400, "비밀번호는 8~12자리로 대문자, 소문자, 숫자, 특수문자를 포함해야 합니다.");
        }
        if (dbMember != null) {
            return new JSONResponse<>(400, "이미 가입된 이메일입니다." + dbMember.getEmail());
        }

        String agreeNoticeYn = null;
        String agreePushYn = null;
        if (YN.Y.getYn().equals(dto.getMarketingYn())) {
            agreeNoticeYn = "Y";
            agreePushYn = "Y";
        }

        Member member = Member.builder()
                .regCode(RegType.EMAIL)
                .loginId(dbEmailId)
                .email(dbEmailId)
                .password(dbPassword)
                .statusCode(MemberStatus.NORMAL)
                .role(MemberRole.USER)
                .realName(dto.getRealName())
                .mobileNumber(dto.getPhoneNumber())
                .yearsOlderYn(YN.Y)
                .agreeTermsYn(YN.Y)
                .agreePrivacyYn(YN.Y)
                .authYn(YN.Y)
                .agreeMarketingYn(YN.ofYn(dto.getMarketingYn()))
                .agreeNoticeYn(YN.ofYn(agreeNoticeYn))
                .agreePushYn(YN.ofYn(agreePushYn))
                .build();

        try {
            memberRepository.save(member);

            String formattedDate = member.getMemberCreatedAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));

            Notification notification = Notification.builder()
                    .memberId(member.getId())
                    .target("personal")
                    .pushStatus("send")
                    .sendStatus("right")
                    .title("회원가입")
                    .content("안녕하세요. 카통령입니다. \n" +
                            "카통령에 가입해주셔서 감사합니다. \n" +
                            "\n" +
                            "신뢰하실 수 있는 서비스를 약속드립니다.\n" +
                            "\n" +
                            "가입날짜 : " + formattedDate)
                    .readYn(YN.N)
                    .sendAt(LocalDateTime.now())
                    .build();

            notificationRepository.save(notification);

            NotificationMember notificationMember = NotificationMember.builder()
                    .memberId(member.getId())
                    .notificationId(notification.getId())
                    .readYn(YN.N)
                    .build();

            notificationMemberRepository.save(notificationMember);

            if (YN.Y.equals(member.getAgreeMarketingYn())) {
                // 회원가입 성공 시 알림톡 전송
                Map<String, String> placeholders = new HashMap<>();
                placeholders.put("#{day}", formattedDate); // 가입 날짜를 현재 날짜로 설정

                alimTalkService.sendAlimTalk("code-01", placeholders, member.getMobileNumber());
            }

        } catch (Exception e) {
            e.printStackTrace();
            return new JSONResponse<>(500, "회원가입에 실패하였습니다.");
        }

        return new JSONResponse<>(200, "회원가입에 성공하였습니다.");
    }

    /**
     * 이메일 pattern 체크
     * @param email 이메일
     * @return
     */
    private boolean isValidEmail(String email) {
        return EMAIL_PATTERN.matcher(email).matches();
    }

    /**
     * 비밀번호 pattern 체크
     * @param password 비밀번호
     * @return
     */
    private boolean isValidPassword(String password) {
        return PASSWORD_PATTERN.matcher(password).matches();
    }

    /**
     * 회원 정보 조회
     * @param loginId 로그인 아이디
     * @return
     */
    public MemberInfoDto searchMemberInfo(String loginId) {
        return memberRepository.searchMemberInfo(loginId);
    }

    /**
     * 회원 탈퇴 (디비 저장)
     * @param username 로그인 아이디
     * @param map 탈퇴 사유
     * @return
     */
    public JSONResponse<?> saveMemberWithdrawal(String username, Map<String, Object> map) {
        String withdrawalReason = map.get("reason").toString();

        Member member = memberRepository.findByLoginIdAndStatusCode(username, MemberStatus.NORMAL);

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

            if (YN.Y.equals(member.getAgreeNoticeYn())) {
                alimTalkService.sendAlimTalk("code12", placeholders, member.getMobileNumber());
            }

        } catch (Exception e) {
            return new JSONResponse<>(500, "회원탈퇴에 실패하였습니다.");
        }

        return new JSONResponse<>(200, "회원탈퇴에 성공하였습니다.");
    }

    /**
     * 비밀번호 찾기 이메일 전송
     * @param map 비밀번호 찾기 정보
     * @throws JsonProcessingException
     */
    public JSONResponse<?> searchFindPasswordEmail(Map<String, Object> map) throws JsonProcessingException {
        String email = map.get("email").toString();
        String memberName = map.get("memberName").toString();
        Member member = memberRepository.findByLoginIdAndStatusCodeAndProviderIdIsNull(email, MemberStatus.NORMAL);

        if (member == null)
            throw new IllegalArgumentException("존재하지 않는 계정입니다.");

        if (!member.getRealName().equals(memberName) || !member.getEmail().equals(email))
            throw new IllegalArgumentException("정보가 일치하지 않습니다.");

        if(!RegType.EMAIL.equals(member.getRegCode())) {
            throw new IllegalArgumentException("이메일 회원가입 회원만 조회가 가능합니다.");
        }

        String jwtToken = jwtUtils.builder()
                .claim("email", email)
                .claim("memberName", memberName)
                .claim("checkData", false)
                .build();

        // URL 생성
        String returnUrl = serverHost + "/find/password/reset?token=" + jwtToken;

        emailService.sendEmail(email, memberName, returnUrl);

        return new JSONResponse<>(200, "SUCCESS");
    }

    /**
     * 비밀번호 재설정
     * @param map 재설정을 위한 정보
     * @return
     */
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

        if(!isValidPassword(password)) {
            return new JSONResponse<>(400, "비밀번호는 8~12자리로 대문자, 소문자, 숫자, 특수문자를 포함해야 합니다.");
        }

        String email = realData.get("email").toString();
        Member member = memberRepository.findByLoginIdAndStatusCode(email, MemberStatus.NORMAL);
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

    /**
     * 마이페이지 비밀번호 변경
     * @param map 마이페이지 비밀번호 변경 정보
     * @param userDetails 로그인 정보
     * @return
     */
    public JSONResponse<?> searchMypageFindPassword(Map<String, Object> map, UserDetails userDetails) {
        String loginId = userDetails.getUsername();
        Member member = memberRepository.findByLoginIdAndStatusCode(loginId, MemberStatus.NORMAL);
        String password = map.get("newPassword").toString();
        String confirmPassword = map.get("confirmPassword").toString();

        if (!isValidPassword(password)) {
            return new JSONResponse<>(400, "비밀번호는 8~12자리로 대문자, 소문자, 숫자, 특수문자를 포함해야 합니다.");
        }

        if (!password.equals(confirmPassword)) {
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

    /**
     * sns 회원 가입 저장
     * @param map sns 회원가입 정보 / redirectUrl
     * @return
     */
    public JSONResponse<?> saveJoinSns(Map<String, Object> map) {
        String marketingYn = map.get("marketingYn").toString();
        String phoneNumber = map.get("phoneNumber").toString();
        String redirectUrl = map.get("redirectUrl").toString() != null ? map.get("redirectUrl").toString() : "/";

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Member member = memberRepository.findByLoginIdAndStatusCode(authentication.getName(), MemberStatus.NORMAL);
        member.setMobileNumber(phoneNumber);
        member.setAgreeMarketingYn(YN.ofYn(marketingYn));
        if ("Y".equals(marketingYn)) {
            member.setAgreeNoticeYn(YN.Y);
            member.setAgreePushYn(YN.Y);
        }
        member.setAuthYn(YN.Y);

        // 인증 정보 갱신
        Authentication loginAuthentication = new UsernamePasswordAuthenticationToken(member, member.getPassword(), member.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(loginAuthentication);

        try {
            memberRepository.save(member);
        } catch (Exception e) {
            return new JSONResponse<>(500, "전화번호 인증에 실패하였습니다.");
        }

        return new JSONResponse<>(200, "전화번호 인증에 성공하였습니다.", redirectUrl);
    }

    /**
     * 회원가입 -> 인증번호 전송 / 인증 테이블 저장
     * @param map 인증번호 전송 정보
     * @return
     */
    public JSONResponse<?> checkPhone(Map<String, Object> map, Authentication authentication) {
        String phoneNumber = map.get("phoneNumber").toString();
        String type = map.get("type").toString();
        String authNumber = generateRandomNumber();
        UUID uuidAuthNumber = generateUUIDFromAuthNumber(authNumber);

        // userDetails가 null인 경우 member를 null로 설정
        Member member = null;
        Long memberId = null;
        if (authentication != null) {
            member = memberRepository.findByLoginIdAndStatusCode(authentication.getName(), MemberStatus.NORMAL);
            if (member != null) {
                memberId = member.getId();
            }
        }

        LocalDateTime now = LocalDateTime.now();

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
            log.error("인증번호 저장에 실패하였습니다.", e);
            return new JSONResponse<>(500, "인증번호 저장에 실패하였습니다.");
        }

        try {
            alimTalkService.sendAlimTalk("code02", bizData, phoneNumber);
        } catch (Exception e) {
            log.error("인증번호 전송에 실패하였습니다.", e);
            return new JSONResponse<>(500, "인증번호 전송에 실패하였습니다.");
        }

        return new JSONResponse<>(200, "SUCCESS");
    }


    /**
     * 인증 번호 확인
     * @param map 인증번호 확인 정보
     * @return
     */
    public JSONResponse<?> checkNumber(Map<String, Object> map) {
        String numberCheck = map.get("numberCheck").toString();
        String phoneNumber = map.get("phoneNumber").toString();

        MemberCredentials memberCredentials = memberCredentialsRepository.findByAuthNumberAndPhoneNumberOrderByCreatedAtDesc(numberCheck, phoneNumber);

        if (memberCredentials == null || memberCredentials.getExpiredAt().isBefore(LocalDateTime.now()) || "Y".equals(memberCredentials.getUseYn().getYn())) {
            return new JSONResponse<>(400, memberCredentials == null ? "인증번호가 유효하지 않습니다." : "인증번호가 만료되었습니다.");
        }

        memberCredentials.setUseYn(YN.Y);

        try {
            memberCredentialsRepository.save(memberCredentials);
        } catch (Exception e) {
            return new JSONResponse<>(500, "FALSE 인증번호 업데이트 실패₩");
        }

        return new JSONResponse<>(200, "인증이 완료되었습니다.");
    }

    /**
     * 회원정보 인증번호 확인 로직 (회원 휴대폰 업데이트 로직 추가)
     * @param map 회원정보 인증번호 확인 정보
     * @param authentication 로그인 정보
     * @return
     */
    public JSONResponse<?> checkNumberInfo(Map<String, Object> map, Authentication authentication) {
        Member member = memberRepository.findByLoginIdAndStatusCode(authentication.getName(), MemberStatus.NORMAL);
        String numberCheck = map.get("numberCheck").toString();
        String phoneNumber = map.get("phoneNumber").toString();

        MemberCredentials memberCredentials = memberCredentialsRepository.findByAuthNumberAndPhoneNumberOrderByCreatedAtDesc(numberCheck, phoneNumber);

        if (memberCredentials == null || memberCredentials.getExpiredAt().isBefore(LocalDateTime.now()) || "Y".equals(memberCredentials.getUseYn().getYn())) {
            return new JSONResponse<>(400, memberCredentials == null ? "인증번호가 유효하지 않습니다." : "인증번호가 만료되었습니다.");
        } else {
            try {
                memberCredentials.setUseYn(YN.Y);
                memberCredentialsRepository.save(memberCredentials);
            } catch (Exception e) {
                return new JSONResponse<>(500, "인증 번호 사용 여부 업데이트 실패");
            }
            try {
                member.setMobileNumber(map.get("phoneNumber").toString());
                memberRepository.save(member);
            } catch (Exception e) {
                return new JSONResponse<>(500, "회원정보 변경을 실패하였습니다.");
            }

            return new JSONResponse<>(200, "회원 정보를 변경했습니다.");
        }
    }

    /**
     * 토큰 db에 저장하기
     * @param map: 토큰 :토큰값
     */
    public JSONResponse<?> saveToken(Map<String, Object> map) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        Member member = memberRepository.findByLoginIdAndStatusCode(email, MemberStatus.NORMAL); // 이메일로 맴버 객체 가져옴
        String token = map.get("token").toString();

        member.setFcmToken(token);

        try {
            memberRepository.save(member);
        } catch (Exception e) {
            return new JSONResponse<>(500, "토큰 저장 실패하였습니다.");
        }
        return new JSONResponse<>(200, "토큰 저장 성공하였습니다.");
    }

    /**
     * 로그인 여부 체크
     * @return
     */
    public String checkLogin() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Member member = memberRepository.findByLoginIdAndStatusCode(authentication.getName(), MemberStatus.NORMAL);

        if (RegType.EMAIL != member.getRegCode()) {
            return null;
        } else {
            return member.getRegCode().getDetail();
        }
    }

    /**
     * 알림 설정 > 설정 저장
     * @param map 설정 변경 정보
     * @return 광고성 정보 변경 여부 (true: 변경함, false: 변경 안함)
     * @author TAEROK HWANG
     */
    public boolean updateMemberNotification(Map<String, Object> map) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Member member = memberRepository.findByLoginIdAndStatusCode(authentication.getName(), MemberStatus.NORMAL);

        YN beforeMarketingYn = member.getAgreeMarketingYn();
        boolean changeMarketingYn = false;

        member.setAgreeMarketingYn(YN.ofYn(map.get("agreeMarketingYn").toString()));
        member.setAgreeNoticeYn(YN.ofYn(map.get("agreeNoticeYn").toString()));
        member.setAgreePushYn(YN.ofYn(map.get("agreePushYn").toString()));

        if (beforeMarketingYn != member.getAgreeMarketingYn())
            changeMarketingYn = true;

        // 인증 정보 갱신
        authentication = new UsernamePasswordAuthenticationToken(member, member.getPassword(), member.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        return changeMarketingYn;
    }

    /**
     * 모바일 앱 구글 로그인
     * @param googleInfoDto 앱 구글 로그인 이후 떨어진 정보
     * @param request 로그인 정보를 저장한 요청
     * @author SungHa
     */
    @Transactional
    public void loginAppGoogle(MemberGoogleInfoDto googleInfoDto, HttpServletRequest request) {
        if (googleInfoDto == null) {
            throw new IllegalArgumentException("잘못된 호출입니다");
        }

        String email = googleInfoDto.getEmail();
        if (!hasText(googleInfoDto.getIdToken()))
            throw new IllegalArgumentException("아이디 정보가 없습니다.");

        if (!hasText(email))
            throw new IllegalArgumentException("이메일 정보가 없습니다.");

        String[] tokenArray = googleInfoDto.getIdToken().split("\\.");
        byte[] decodedToken = Base64.getDecoder().decode(tokenArray[1]);
        ObjectMapper objectMapper = new ObjectMapper();
        String providerId = null;
        String realName = null;
        try {
            JsonNode jsonNode = objectMapper.readTree(decodedToken);
            providerId = jsonNode.get("sub").asText();
            realName = jsonNode.get("name").asText();
        } catch (IOException e) {
            throw new RuntimeException("토큰 읽기 오류: " + e.getMessage());
        }

        String loginId = "google_" + providerId;
        String uuid = UUID.randomUUID().toString().substring(0, 6);
        String password = new BCryptPasswordEncoder().encode(uuid);

        MemberInfoDto memberNotNormalCount = memberRepository.searchEmailAndProviderIdAndMemberStatus(email, providerId);

        if (memberNotNormalCount != null) {
            if (Objects.requireNonNull(memberNotNormalCount.getStatusCode()) == MemberStatus.PAUSE) {
                throw new LockedException("일시" + MemberStatus.PAUSE.getDetail() + "된 계정입니다. 관리자에게 문의하세요.");
            }
        }

        Member dbMember = memberRepository.findByEmailAndProviderIdIsNotNullAndStatusCode(email, MemberStatus.NORMAL);

        if (dbMember != null && dbMember.getRegCode() != RegType.GOOGLE) {
            throw new BadCredentialsException(dbMember.getRegCode().getDetail());
        }

        Member member = memberRepository.findByLoginIdAndStatusCode(loginId, MemberStatus.NORMAL);

        if (member == null) {
            member = Member.builder()
                    .providerId(providerId)
                    .loginId(loginId)
                    .password(password)
                    .realName(realName)
                    .email(email)
                    .mobileNumber("01000000000")
                    .regCode(RegType.GOOGLE)
                    .statusCode(MemberStatus.NORMAL)
                    .role(MemberRole.USER)
                    .authYn(YN.N)
                    .lastLoginAt(LocalDateTime.now())
                    .build();

            memberRepository.save(member);

            String formattedDate = LocalDate.now().toString();

            Notification notification = Notification.builder()
                    .memberId(member.getId())
                    .target("personal")
                    .pushStatus("send")
                    .sendStatus("right")
                    .title("회원가입")
                    .content("안녕하세요. 카통령입니다. \n" +
                            "카통령에 가입해주셔서 감사합니다. \n" +
                            "\n" +
                            "신뢰하실 수 있는 서비스를 약속드립니다.\n" +
                            "\n" +
                            "가입날짜 : " + formattedDate)
                    .readYn(YN.N)
                    .sendAt(LocalDateTime.now())
                    .build();

            notificationRepository.save(notification);

            NotificationMember notificationMember = NotificationMember.builder()
                    .memberId(member.getId())
                    .notificationId(notification.getId())
                    .readYn(YN.N)
                    .build();

            notificationMemberRepository.save(notificationMember);

            // 회원가입 성공 시 알림톡 전송
            Map<String, String> placeholders = new HashMap<>();
            placeholders.put("#{day}", formattedDate); // 가입 날짜를 현재 날짜로 설정

            alimTalkService.sendAlimTalk("code01", placeholders, member.getMobileNumber());

            try {
                memberRepository.save(member);

            } catch (Exception e) {
                log.error("member save error : {}", e.getMessage());
                throw new RuntimeException();
            }
        }

        Authentication loginAuthentication = new UsernamePasswordAuthenticationToken(member, null, member.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(loginAuthentication);
        HttpSession session = request.getSession(true);
        session.setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY, SecurityContextHolder.getContext());

        member.setLastLoginAt(LocalDateTime.now());
        memberRepository.save(member);

    }

}
