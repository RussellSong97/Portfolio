package com.cuv.admin.domain.boardnotice.dto;

import com.cuv.admin.common.YN;
import com.cuv.admin.domain.attachment.dto.AttachmentDto;
import com.cuv.admin.domain.member.enumset.MemberRole;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Convert;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Schema(
        name = "BoardNoticeListDto",
        description = "공지사항 테이블 목록 출력"
)
public class BoardNoticeListDto {
    private Long id;
    private String title;
    @Convert(converter = AttachmentDto.AttachmentDtoListConverter.class)
    private List<AttachmentDto> attachment;
    private Long hits;
    private Long memberAdminId;
    private String loginId;
    @Convert(converter = MemberRole.MemberRoleConverter.class)
    private MemberRole role;
    private YN viewYn;
    private YN noticeYn;
    private LocalDateTime createdAt;
}
