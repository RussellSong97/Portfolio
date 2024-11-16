package com.cuv.admin.domain.exhibition;

import com.cuv.admin.common.YN;
import com.cuv.admin.domain.attachment.AttachmentService;
import com.cuv.admin.domain.exhibition.dto.ExhibitionDetailDto;
import com.cuv.admin.domain.exhibition.dto.ExhibitionListDto;
import com.cuv.admin.domain.exhibition.dto.ExhibitionSaveDto;
import com.cuv.admin.domain.exhibition.dto.ExhibitionSearchDto;
import com.cuv.admin.domain.exhibition.entity.Exhibition;
import com.cuv.admin.domain.exhibition.enumset.BackgroundColorType;
import com.cuv.admin.domain.exhibition.enumset.ExhibitionType;
import com.cuv.admin.domain.exhibition.enumset.LinkType;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

import static org.springframework.util.StringUtils.hasText;

/**
 * 프로모션 관리 (등록, 수정, 삭제, 상세보기)
 */
@Service
@RequiredArgsConstructor
public class ExhibitionService {

    private final ExhibitionRepository exhibitionRepository;

    private final AttachmentService attachmentService;

    /**
     * 관리자 | 프로모션 > 전시 관리 > 등록
     *
     * @param requestDto 등록할 정보를 담은 DTO
     * @author SungHa
     */
    @Transactional
    public void adminPromotionExhibitionWriteProc(ExhibitionSaveDto requestDto) {
        String type = requestDto.getExhibitionType();
        String linkType = requestDto.getLinkType();
        String url = requestDto.getUrl();
        String exhibitionStartDate = requestDto.getExhibitionStartDate();
        String exhibitionEndDate = requestDto.getExhibitionEndDate();

        if (!ExhibitionType.TOP_STRIP.getCode().equals(type)) {
            if (!hasText(requestDto.getTitle()))
                throw new IllegalArgumentException("제목을 입력해주세요.");
            if (!hasText(requestDto.getPcFileUUID()))
                throw new IllegalArgumentException("PC 이미지를 첨부해주세요.");
            if (!hasText(requestDto.getMoFileUUID()))
                throw new IllegalArgumentException("모바일 이미지를 첨부해주세요.");
        }

        if (ExhibitionType.EVENT.getCode().equals(type) || ExhibitionType.TOP_STRIP.getCode().equals(type)) {
            if (!hasText(requestDto.getContent()))
                throw new IllegalArgumentException("내용을 입력해주세요.");
        }

        if (!ExhibitionType.EVENT.getCode().equals(type)) {
            if (!LinkType.EMPTY.getCode().equals(linkType) && !hasText(url)) {
                throw new IllegalArgumentException("링크를 입력해주세요.");
            } else if (LinkType.EMPTY.getCode().equals(linkType) && hasText(url)) {
                requestDto.setUrl(null);
            }
        }

        if (ExhibitionType.EVENT.getCode().equals(type) || ExhibitionType.POPUP.getCode().equals(type)
                || ExhibitionType.TOP_STRIP.getCode().equals(type)) {
            if (!hasText(exhibitionStartDate))
                throw new IllegalArgumentException("시작일을 설정해주세요.");

            if (!hasText(exhibitionEndDate))
                throw new IllegalArgumentException("종료일을 설정해주세요.");
        }

        if (ExhibitionType.TOP_STRIP.getCode().equals(type)) {
            if (YN.Y.getYn().equals(requestDto.getViewYn())) {
                Long topStripBannerCount = exhibitionRepository.searchAllExhibitionCount(type);

                if (topStripBannerCount >= 1)
                    throw new IllegalArgumentException("상단 띠 배너는 최대 1개까지 노출 가능합니다.");
            }
        }

        if (ExhibitionType.MAIN_SUB.getCode().equals(type)) {
            if (YN.Y.getYn().equals(requestDto.getViewYn())) {
                Long mainSubBannerCount = exhibitionRepository.searchAllExhibitionCount(type);

                if (mainSubBannerCount >= 2)
                    throw new IllegalArgumentException("메인 서브 배너는 최대 2개까지 노출 가능합니다.");
            }
        }

        if (ExhibitionType.LOGIN.getCode().equals(type)) {
            if (YN.Y.getYn().equals(requestDto.getViewYn())) {
                Long loginBannerCount = exhibitionRepository.searchAllExhibitionCount(type);

                if (loginBannerCount >= 1)
                    throw new IllegalArgumentException("로그인 배너는 한 개만 노출 가능합니다.");
            }
        }

        LocalDate parsedStartDate = null;
        LocalDate parsedEndDate = null;
        if (exhibitionStartDate != null && exhibitionEndDate != null) {
            parsedStartDate = LocalDate.parse(exhibitionStartDate);
            parsedEndDate = LocalDate.parse(exhibitionEndDate);
        }

        Exhibition exhibition = Exhibition.builder()
                .exhibitionType(ExhibitionType.ofCode(type))
                .title(requestDto.getTitle())
                .content(requestDto.getContent())
                .linkUrl(url)
                .linkType(LinkType.ofCode(linkType))
                .pcAttachment(attachmentService.findUploadFileDtoByUUID(requestDto.getPcFileUUID()))
                .mobileAttachment(attachmentService.findUploadFileDtoByUUID(requestDto.getMoFileUUID()))
                .exhibitionStartDate(parsedStartDate)
                .exhibitionEndDate(parsedEndDate)
                .backgroundColorType(BackgroundColorType.ofCode(requestDto.getBackgroundColorCode()))
                .hits(0L)
                .viewYn(YN.ofYn(requestDto.getViewYn()))
                .build();

        exhibitionRepository.save(exhibition);
    }

    /**
     * 관리자 | 프로모션 > 전시 관리 > 목록
     *
     * @param condition 검색 조건을 담은 DTO
     * @author SungHa
     */
    public Page<ExhibitionListDto> searchAllExhibition(ExhibitionSearchDto condition, Pageable pageable) {
        return exhibitionRepository.searchAllExhibition(condition, pageable);
    }

    /**
     * 관리자 | 프로모션 > 전시 관리 > 상세
     *
     * @param id 전시 시퀀스
     * @author SungHa
     */
    public ExhibitionDetailDto searchExhibitionById(Long id) {
        return exhibitionRepository.searchExhibitionById(id);
    }

    /**
     * 관리자 | 프로모션 > 전시 관리 > 수정
     *
     * @param requestDto 등록할 정보를 담은 DTO
     * @author SungHa
     */
    @Transactional
    public void adminPromotionExhibitionEditProc(ExhibitionSaveDto requestDto) {
        String type = requestDto.getExhibitionType();
        String linkType = requestDto.getLinkType();
        String url = requestDto.getUrl();
        String exhibitionStartDate = requestDto.getExhibitionStartDate();
        String exhibitionEndDate = requestDto.getExhibitionEndDate();

        Exhibition exhibition = exhibitionRepository
                .findById(requestDto.getId())
                .orElseThrow(() -> new IllegalArgumentException("글을 찾을 수가 없습니다."));

        if (!ExhibitionType.TOP_STRIP.getCode().equals(type)) {
            if (!hasText(String.valueOf(requestDto.getPosition())))
                throw new IllegalArgumentException("순서를 입력해주세요.");
            if (!hasText(requestDto.getTitle()))
                throw new IllegalArgumentException("제목을 입력해주세요.");
            if (!hasText(requestDto.getPcFileUUID()))
                throw new IllegalArgumentException("PC 이미지를 첨부해주세요.");
            if (!hasText(requestDto.getMoFileUUID()))
                throw new IllegalArgumentException("모바일 이미지를 첨부해주세요.");
        }

        if (ExhibitionType.EVENT.getCode().equals(type) || ExhibitionType.TOP_STRIP.getCode().equals(type)) {
            if (!hasText(requestDto.getContent()))
                throw new IllegalArgumentException("내용을 입력해주세요.");
        }

        if (!ExhibitionType.EVENT.getCode().equals(type)) {
            if (!LinkType.EMPTY.getCode().equals(linkType) && !hasText(url)) {
                throw new IllegalArgumentException("링크를 입력해주세요.");
            } else if (LinkType.EMPTY.getCode().equals(linkType) && hasText(url)) {
                requestDto.setUrl(null);
            }
        }

        if (ExhibitionType.EVENT.getCode().equals(type) || ExhibitionType.POPUP.getCode().equals(type)
                || ExhibitionType.TOP_STRIP.getCode().equals(type)) {
            if (!hasText(exhibitionStartDate))
                throw new IllegalArgumentException("시작일을 설정해주세요.");

            if (!hasText(exhibitionEndDate))
                throw new IllegalArgumentException("종료일을 설정해주세요.");
        }

        boolean isNewView = YN.Y.getYn().equals(requestDto.getViewYn());
        boolean isCurrentlyView = YN.Y == exhibition.getViewYn();

        if (isNewView && !isCurrentlyView) {
            if (ExhibitionType.TOP_STRIP.getCode().equals(type)) {
                if (YN.Y.getYn().equals(requestDto.getViewYn())) {
                    Long topStripBannerCount = exhibitionRepository.searchAllExhibitionCount(type);

                    if (topStripBannerCount >= 1)
                        throw new IllegalArgumentException("상단 띠 배너는 최대 1개까지 노출 가능합니다.");
                }
            }

            if (ExhibitionType.MAIN_SUB.getCode().equals(type)) {
                if (YN.Y.getYn().equals(requestDto.getViewYn())) {
                    Long mainSubBannerCount = exhibitionRepository.searchAllExhibitionCount(type);

                    if (mainSubBannerCount >= 2)
                        throw new IllegalArgumentException("메인 서브 배너는 최대 2개까지 노출 가능합니다.");
                }
            }

            if (ExhibitionType.LOGIN.getCode().equals(type)) {
                if (YN.Y.getYn().equals(requestDto.getViewYn())) {
                    Long loginBannerCount = exhibitionRepository.searchAllExhibitionCount(type);

                    if (loginBannerCount >= 1)
                        throw new IllegalArgumentException("로그인 배너는 한 개만 노출 가능합니다.");
                }
            }
        }

        LocalDate parsedStartDate = null;
        LocalDate parsedEndDate = null;
        if (exhibitionStartDate != null && exhibitionEndDate != null) {
            parsedStartDate = LocalDate.parse(exhibitionStartDate);
            parsedEndDate = LocalDate.parse(exhibitionEndDate);
        }

        exhibition.setTitle(requestDto.getTitle());
        exhibition.setContent(requestDto.getContent());
        exhibition.setLinkUrl(url);
        exhibition.setLinkType(LinkType.ofCode(linkType));
        exhibition.setPcAttachment(attachmentService.findUploadFileDtoByUUID(requestDto.getPcFileUUID()));
        exhibition.setMobileAttachment(attachmentService.findUploadFileDtoByUUID(requestDto.getMoFileUUID()));
        exhibition.setExhibitionStartDate(parsedStartDate);
        exhibition.setExhibitionEndDate(parsedEndDate);
        exhibition.setBackgroundColorType(BackgroundColorType.ofCode(requestDto.getBackgroundColorCode()));
        exhibition.setViewYn(YN.ofYn(requestDto.getViewYn()));
        exhibition.setPosition(requestDto.getPosition());
    }

    /**
     * 관리자 | 프로모션 > 전시 관리 > 목록 > 삭제
     *
     * @param idList 삭제할 시퀀스 값의 배열
     * @author SungHa
     */
    @Transactional
    public void adminPromotionExhibitionListDeleteProc(List<Long> idList) {
        for (Long id : idList) {
            exhibitionRepository.findById(id).ifPresent(exhibition -> exhibition.setDelYn(YN.Y));
        }
    }

    /**
     * 관리자 | 프로모션 > 전시 관리 > 상세 > 삭제
     *
     * @param id 삭제할 시퀀스
     * @author SungHa
     */
    @Transactional
    public void adminPromotionExhibitionDeleteProc(Long id) {
        Exhibition exhibition = exhibitionRepository
                .findById(id)
                .orElseThrow(() -> new IllegalArgumentException("글을 찾을 수 없습니다."));

        exhibition.setDelYn(YN.Y);
    }

}
