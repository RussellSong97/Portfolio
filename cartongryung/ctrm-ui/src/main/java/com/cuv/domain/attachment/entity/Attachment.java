package com.cuv.domain.attachment.entity;

import com.cuv.common.BaseEntity;
import com.cuv.common.YN;
import com.cuv.domain.attachment.dto.AttachmentDto;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.Comment;

/**
 * 첨부파일
 */
@Entity
@SuperBuilder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
@Table(name = "attachment")
public class Attachment extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "attachment_id")
    @Comment("파일 일련번호")
    private Long id;

    @Comment("파일 UUID")
    private String uuid;

    @Comment("파일 저장된 이름")
    private String uploadName;

    @Comment("파일 원본 이름")
    private String originalName;

    @Comment("파일 확장자 (. 제외)")
    private String extension;

    @Comment("파일 경로")
    private String path;

    @Comment("실제 URL")
    private String realUrl;

    @Column(name = "file_size")
    @Comment("파일 크기")
    private Long size;

    @Comment("업로드 파일의 출처")
    private String source;

    @Comment("표시 여부")
    private YN viewYn;


    public void setViewYn(YN viewYn) {
        this.viewYn = viewYn;
    }

    public AttachmentDto toDto() {
        return new AttachmentDto(
                this.id,
                this.uuid,
                this.uploadName,
                this.originalName,
                this.extension,
                this.path,
                this.realUrl,
                this.size,
                this.createdAt
        );
    }

}
