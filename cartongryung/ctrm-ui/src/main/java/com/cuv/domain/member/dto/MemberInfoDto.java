package com.cuv.domain.member.dto;

import com.cuv.common.YN;
import com.cuv.domain.attachment.dto.AttachmentDto;
import com.cuv.domain.member.enumset.MemberStatus;
import com.cuv.domain.member.enumset.RegType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Convert;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(
        name="MemberInfoDto",
        description = "회원 정보 출력"
)
public class MemberInfoDto {
    private String email;
    private String mobileNumber;
    private String realName;
    private RegType regCode;
    @Convert(converter = AttachmentDto.AttachmentDtoConverter.class)
    private AttachmentDto profileImage;
    private MemberStatus statusCode;
    private Long id;
    private YN agreeMarketingYn;
    private YN agreeNoticeYn;
    private YN agreePushYn;
}
