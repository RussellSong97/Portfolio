package com.cuv.domain.exhibition.dto;

import com.cuv.domain.attachment.dto.AttachmentDto;
import com.cuv.domain.exhibition.enumset.LinkType;
import jakarta.persistence.Convert;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class ExhibitionListDto {

    private Long id;

    private String title;

    private String content;

    private String linkUrl;

    @Convert(converter = LinkType.LinkTypeConverter.class)
    private LinkType linkType;

    @Convert(converter = AttachmentDto.AttachmentDtoConverter.class)
    private AttachmentDto pcAttachment;

    @Convert(converter = AttachmentDto.AttachmentDtoConverter.class)
    private AttachmentDto mobileAttachment;

    private LocalDate exhibitionStartDate;

    private LocalDate exhibitionEndDate;

    private Long hits;

    /**
     * 이벤트
     *
     * @param edate 노출 종료 날짜
     * @author SungHa
     */
    public String getEventStatusClass(LocalDate edate) {
        LocalDate now = LocalDate.now();

        if (now.isAfter(edate)) {
            return "is-end";
        } else {
            return "";
        }
    }

}
