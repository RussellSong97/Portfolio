package com.cuv.admin.domain.boardguide;

import com.cuv.admin.common.YN;
import com.cuv.admin.domain.boardguide.dto.BoardGuideDetailDto;
import com.cuv.admin.domain.boardguide.dto.BoardGuideListDto;
import com.cuv.admin.domain.boardguide.dto.BoardGuideSaveDto;
import com.cuv.admin.domain.boardguide.dto.BoardGuideSearchDto;
import com.cuv.admin.domain.boardguide.entity.BoardGuide;
import com.cuv.admin.domain.boardguide.enumset.BoardGuideType;
import com.cuv.admin.domain.attachment.AttachmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.util.StringUtils.hasText;

/**
 * 중고차 가이드 서비스 (조회, 저장, 수정, 삭제)
 */
@Service
@RequiredArgsConstructor
public class BoardGuideService {

    private final BoardGuideRepository boardGuideRepository;

    private final AttachmentService attachmentService;

    /**
     * 관리자 | 게시판 > 중고차 가이드 > 글 등록
     *
     * @param requestDto 등록할 글 정보를 담은 DTO
     * @author SungHa
     */
    @Transactional
    public void adminBoardGuideWriteProc(BoardGuideSaveDto requestDto) {
        if (!hasText(requestDto.getTitle()))
            throw new IllegalArgumentException("제목을 입력해주세요.");
        if (!hasText(requestDto.getBoardGuideType()))
            throw new IllegalArgumentException("분류를 선택해주세요.");
        if (!hasText(requestDto.getViewYn()))
            throw new IllegalArgumentException("노출 상태를 선택해주세요.");
        if (!hasText(requestDto.getContent()))
            throw new IllegalArgumentException("내용을 입력해주세요.");
        if (!hasText(requestDto.getFileUUID()))
            throw new IllegalArgumentException("이미지를 첨부해주세요.");

        BoardGuide boardGuide = BoardGuide.builder()
                .title(requestDto.getTitle())
                .boardGuideType(BoardGuideType.ofCode(requestDto.getBoardGuideType()))
                .viewYn(YN.ofYn(requestDto.getViewYn()))
                .content(requestDto.getContent())
                .attachment(attachmentService.findUploadFileDtoByUUID(requestDto.getFileUUID()))
                .build();

        boardGuideRepository.save(boardGuide);
    }

    /**
     * 관리자 | 게시판 > 중고차 가이드 > 목록
     *
     * @param condition 검색 조건을 담은 DTO
     * @author SungHa
     */
    public Page<BoardGuideListDto> searchAllBoardGuide(BoardGuideSearchDto condition, Pageable pageable) {
        return boardGuideRepository.searchAllBoardGuide(condition, pageable);
    }

    /**
     * 관리자 | 게시판 > 중고차 가이드 > 상세
     *
     * @param id 글 시퀀스
     * @author SungHa
     */
    public BoardGuideDetailDto searchBoardGuideById(Long id) {
        return boardGuideRepository.searchBoardGuideById(id);
    }

    /**
     * 관리자 | 게시판 > 중고차 가이드 > 수정
     *
     * @param requestDto 등록할 글 정보를 담은 DTO
     * @author SungHa
     */
    @Transactional
    public void adminBoardGuideEditProc(BoardGuideSaveDto requestDto) {
        BoardGuide boardGuide = boardGuideRepository
                .findById(requestDto.getId())
                .orElseThrow(() -> new IllegalArgumentException("글을 찾을 수가 없습니다."));

        if (!hasText(requestDto.getTitle()))
            throw new IllegalArgumentException("제목을 입력해주세요.");
        if (!hasText(requestDto.getBoardGuideType()))
            throw new IllegalArgumentException("분류를 선택해주세요.");
        if (!hasText(requestDto.getViewYn()))
            throw new IllegalArgumentException("노출 상태를 선택해주세요.");
        if (!hasText(requestDto.getContent()))
            throw new IllegalArgumentException("내용을 입력해주세요.");
        if (!hasText(requestDto.getFileUUID()))
            throw new IllegalArgumentException("이미지를 첨부해주세요.");

        boardGuide.setTitle(requestDto.getTitle());
        boardGuide.setBoardGuideType(BoardGuideType.ofCode(requestDto.getBoardGuideType()));
        boardGuide.setViewYn(YN.ofYn(requestDto.getViewYn()));
        boardGuide.setContent(requestDto.getContent());
        boardGuide.setAttachment(attachmentService.findUploadFileDtoByUUID(requestDto.getFileUUID()));
    }

    /**
     * 관리자 | 게시판 > 중고차 가이드 > 삭제
     *
     * @param id 글 시퀀스
     * @author SungHa
     */
    @Transactional
    public void adminBoardGuideDeleteProc(Long id) {
        BoardGuide boardGuide = boardGuideRepository
                .findById(id)
                .orElseThrow(() -> new IllegalArgumentException("글을 찾을 수 없습니다."));

        boardGuide.setDelYn(YN.Y);
    }

}
