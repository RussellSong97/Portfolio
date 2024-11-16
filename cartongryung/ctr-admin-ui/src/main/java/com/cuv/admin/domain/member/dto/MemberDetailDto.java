package com.cuv.admin.domain.member.dto;

import com.cuv.admin.common.YN;
import com.cuv.admin.domain.attachment.dto.AttachmentDto;
import com.cuv.admin.domain.member.enumset.MemberStatus;
import com.cuv.admin.domain.member.enumset.RegType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Convert;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Schema(
        name = "MemberDetailDto",
        description = "회원 상세 정보 출력"
)
public class MemberDetailDto {
    private Long id;
    private String realName;
    private String email;
    private String mobileNumber;
    private String password;
    private YN agreeMarketingYn;
    private LocalDateTime memberCreatedAt;
    private LocalDateTime lastLoginAt;
    private MemberStatus statusCode;
    private RegType regCode;
    @Convert(converter = AttachmentDto.AttachmentDtoConverter.class)
    private AttachmentDto profileImage;
}
