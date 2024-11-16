package com.cuv.admin.domain.linkcode;

import com.cuv.admin.common.YN;
import com.cuv.admin.domain.attachment.AttachmentService;
import com.cuv.admin.domain.linkcode.dto.LinkCodeDetailDto;
import com.cuv.admin.domain.linkcode.dto.LinkCodeListDto;
import com.cuv.admin.domain.linkcode.dto.LinkCodeSaveDto;
import com.cuv.admin.domain.linkcode.dto.LinkCodeSearchDto;
import com.cuv.admin.domain.linkcode.entity.LinkCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.util.StringUtils.hasText;

/**
 * 차량 관리의 차량 등급 관련 정보 관리(등급 설정, 수정)
 */
@Service
@RequiredArgsConstructor
public class LinkCodeService {

    private final LinkCodeRepository linkCodeRepository;

    private final AttachmentService attachmentService;

    /**
     * 관리자 | 차량관리 > 코드 관리 > 차량 등급 설정
     * 관리자 | 차량관리 > 코드 관리 > 차량 등급 설정 > 하위 목록 출력
     * 관리자 | 차량관리 > 매물 연동 관리 > 목록 - 차량 매물 검색 조건
     *
     * @param condition 검색 조건을 담은 DTO
     * @author SungHa
     */
    public List<LinkCodeListDto> searchAllLinkCode(LinkCodeSearchDto condition) {
        return linkCodeRepository.searchAllLinkCode(condition);
    }

    /**
     * 관리자 | 차량관리 > 코드 관리 > 차량 등급 설정 > 등록
     *
     * @param requestDto 등록할 코드 정보를 담은 DTO
     * @author SungHa
     */
    @Transactional
    public Long adminManagementCodeWriteProc(LinkCodeSaveDto requestDto) {
        Long parentLinkNbrId = requestDto.getParentLinkNbrId();
        String viewYn = requestDto.getViewYn();
        String afterServiceDate = requestDto.getAfterServiceDate();

        Integer lastOrderSeq = linkCodeRepository.searchLastOrderSeq(parentLinkNbrId, requestDto.getDepth());
        if (lastOrderSeq == null) {
            requestDto.setViewOrder(1);
        } else {
            requestDto.setViewOrder(lastOrderSeq + 1);
        }

        LocalDate parsedDate = null;
        if (afterServiceDate != null) {
            parsedDate = LocalDate.parse(afterServiceDate);
        }

        if (!hasText(viewYn))
            throw new IllegalArgumentException("노출 상태를 설정해주세요.");

        if (!hasText(requestDto.getLinkDataNm()))
            throw new IllegalArgumentException("내용을 입력해주세요.");

        LinkCode linkCode = LinkCode.builder()
                .parentLinkNbrId(requestDto.getParentLinkNbrId())
                .linkDataNm(requestDto.getLinkDataNm())
                .depth(requestDto.getDepth())
                .viewOrder(requestDto.getViewOrder())
                .attachment(attachmentService.findUploadFileDtoByUUID(requestDto.getFileUUID()))
                .afterServiceDate(parsedDate)
                .viewYn(YN.ofYn(viewYn))
                .build();

        return linkCodeRepository.save(linkCode).getId();

    }

    /**
     * 관리자 | 차량관리 > 코드 관리 > 차량 등급 설정 > 수정
     *
     * @param requestDto 등록할 코드 정보를 담은 DTO
     * @author SungHa
     */
    @Transactional
    public void adminManagementCodeEditProc(LinkCodeSaveDto requestDto) {
        LinkCode linkCode = linkCodeRepository
                .findById(requestDto.getId())
                .orElseThrow(() -> new IllegalArgumentException("정보를 찾을 수가 없습니다."));

        if (!hasText(requestDto.getViewYn()))
            throw new IllegalArgumentException("노출 상태를 설정해주세요.");

        if (!hasText(requestDto.getLinkDataNm()))
            throw new IllegalArgumentException("내용을 입력해주세요.");

        LocalDate parsedDate = null;
        String afterServiceDate = requestDto.getAfterServiceDate();
        if (afterServiceDate != null) {
            parsedDate = LocalDate.parse(afterServiceDate);
        }

        linkCode.setLinkDataNm(requestDto.getLinkDataNm());
        linkCode.setAttachment(attachmentService.findUploadFileDtoByUUID(requestDto.getFileUUID()));
        linkCode.setAfterServiceDate(parsedDate);
        linkCode.setViewYn(YN.ofYn(requestDto.getViewYn()));
    }

    /**
     * 관리자 | 차량관리 > 코드 관리 > 차량 등급 설정 > 삭제
     *
     * @param id 삭제할 카테고리 시퀀스
     * @author SungHa
     */
    @Transactional
    public void adminManagementCodeDeleteProc(Long id) {
        LinkCode linkCode = linkCodeRepository
                .findById(id)
                .orElseThrow(() -> new IllegalArgumentException("정보를 찾을 수가 없습니다."));

        linkCode.setDelYn(YN.Y);
    }

    /**
     * 관리자 | 차량관리 > 코드 관리 > 차량 등급 설정 > 노출 순서 변경
     *
     * @param idList 순서 변경할 시퀀스 값의 배열
     * @author SungHa
     */
    @Transactional
    public void adminManagementCodeChange(List<Long> idList) {
        int viewOrder = 1;
        List<LinkCode> linkCodeList = new ArrayList<>();

        for (Long linkCodeId : idList) {
            LinkCode linkCode = linkCodeRepository
                    .findById(linkCodeId)
                    .orElseThrow(() -> new IllegalArgumentException("정보를 찾을 수가 없습니다."));

            linkCode.setViewOrder(viewOrder++);
            linkCodeList.add(linkCode);
        }

        linkCodeRepository.saveAll(linkCodeList);
    }

    /**
     * 관리자 | 차량관리 > 코드 관리 > 차량 등급 설정 > 수정 팝업
     *
     * @param id 코드 카테고리 시퀀스
     * @author SungHa
     */
    public LinkCodeDetailDto searchLinkCodeById(Long id) {
        return linkCodeRepository.searchLinkCodeById(id);
    }
}
