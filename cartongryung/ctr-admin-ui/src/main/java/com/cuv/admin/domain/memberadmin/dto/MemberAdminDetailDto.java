package com.cuv.admin.domain.memberadmin.dto;

import com.cuv.admin.common.YN;
import com.cuv.admin.domain.attachment.dto.AttachmentDto;
import com.cuv.admin.domain.member.enumset.MemberRole;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Convert;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Schema(
        name = "MemberAdminDetailDto",
        description = "관리자 회원 테이블 상세 출력"
)
public class MemberAdminDetailDto {

    private Long id;

    private MemberRole role;

    private String loginId;

    private String realName;

    private String mobileNumber;

    private String employeeNumber;

    @Convert(converter = AttachmentDto.AttachmentDtoConverter.class)
    private AttachmentDto profileImageJson;

    private String intro;

    private YN useYn;

    private LocalDateTime lastLoginAt;
}
