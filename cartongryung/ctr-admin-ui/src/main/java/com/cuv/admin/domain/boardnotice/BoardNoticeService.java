package com.cuv.admin.domain.boardnotice;

import com.cuv.admin.common.JSONResponse;
import com.cuv.admin.common.YN;
import com.cuv.admin.domain.attachment.AttachmentService;
import com.cuv.admin.domain.boardnotice.dto.BoardNoticeDetailDto;
import com.cuv.admin.domain.boardnotice.dto.BoardNoticeListDto;
import com.cuv.admin.domain.boardnotice.dto.BoardNoticeSaveDto;
import com.cuv.admin.domain.boardnotice.dto.BoardNoticeSearchDto;
import com.cuv.admin.domain.boardnotice.entity.BoardNotice;
import com.cuv.admin.domain.memberadmin.MemberAdminRepository;
import com.cuv.admin.domain.memberadmin.entity.MemberAdmin;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

import static org.springframework.util.StringUtils.hasText;

/**
 * 공지사항 서비스 (조회, 저장, 수정, 삭제)
 */
@Service
@RequiredArgsConstructor
@Transactional
public class BoardNoticeService {

    private final BoardNoticeRepository boardNoticeRepository;

    private final MemberAdminRepository memberAdminRepository;

    private final AttachmentService attachmentService;

    /**
     * 관리자 | 게시판 > 공지사항 > 목록
     *
     * @param condition 검색 조건을 담은 DTO
     * @author 송다운
     */
    public Page<BoardNoticeListDto> searchAllBoardNotice(BoardNoticeSearchDto condition, Pageable request) {
        return boardNoticeRepository.searchAllBoardNotice(condition, request);
    }

    /**
     * 관리자 | 게시판 > 공지사항 > 글 등록
     *
     * @param requestDto 등록할 글 정보를 담은 DTO
     * @author 송다운
     */
    public JSONResponse<?> adminBoardNoticeWriteProc(BoardNoticeSaveDto requestDto) {
        if(!hasText(requestDto.getTitle()))
            return new JSONResponse<>(500, "제목을 입력해주세요.");
        if(!hasText(requestDto.getViewYn()))
            return new JSONResponse<>(500, "노출 상태를 선택해주세요.");
        if(!hasText(requestDto.getContent()))
            return new JSONResponse<>(500, "내용을 입력해주세요.");

        if (YN.Y.getYn().equals(requestDto.getNoticeYn())) {
            Long noticeCount = boardNoticeRepository.searchAllNoticeCount();

            if (noticeCount >= 2)
                throw new IllegalArgumentException("공지는 최대 2개까지 설정 가능합니다.");
        }

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String memberAdminId = auth.getName();
        MemberAdmin memberAdmin = memberAdminRepository
                .findByLoginId(memberAdminId)
                .orElseThrow(() -> new IllegalArgumentException("일치하는 회원이 없습니다."));

        BoardNotice boardNotice = BoardNotice.builder()
                .title(requestDto.getTitle())
                .content(requestDto.getContent())
                .viewYn(YN.ofYn(requestDto.getViewYn()))
                .hits(0L)
                .noticeYn(YN.ofYn(requestDto.getNoticeYn()))
                .attachment(attachmentService.findAllUploadFileDtoByUUID(requestDto.getFileUUId()))
                .memberAdminId(memberAdmin.getId())
                .build();

        try {
            boardNoticeRepository.save(boardNotice);
        } catch (Exception e) {
            return new JSONResponse<>(500, "FAIL");
        }
        return new JSONResponse<>(200, "SUCCESS");
    }

    /**
     * 관리자 | 게시판 > 공지사항 > 상세/수정 폼
     *
     * @param id 글 시퀀스
     * @author 송다운
     */
    public BoardNoticeDetailDto searchBoardNoticeById(Long id) {
        return boardNoticeRepository.searchBoardNoticeById(id);
    }

    /**
     * 관리자 | 게시판 > 공지사항 > 삭제
     *
     * @param map 글 시퀀스를 담은 map
     * @author 송다운
     */
    public JSONResponse<?> adminBoardDeleteProc(Map<String, Object> map) {
        Long boardNoticeId = Long.valueOf(map.get("boardNoticeId").toString());

        BoardNotice boardNotice = boardNoticeRepository.findById(boardNoticeId)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 존재하지 않습니다."));

        boardNotice.setDelYn(YN.Y);

        try {
            boardNoticeRepository.save(boardNotice);
        } catch (Exception e) {
            return new JSONResponse<>(500, "ERROR");
        }

        return new JSONResponse<>(200, "SUCCESS");
    }

    /**
     * 관리자 | 게시판 > 공지사항 > 수정
     *
     * @param requestDto 등록할 글 정보를 담은 DTO
     * @author 송다운
     */
    public JSONResponse<?> adminBoardNoticeEditProc(BoardNoticeSaveDto requestDto) {
        BoardNotice boardNotice = boardNoticeRepository.findById(requestDto.getId())
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 존재하지 않습니다."));

        if(!hasText(requestDto.getTitle()))
            return new JSONResponse<>(500, "제목을 입력해주세요.");
        if(!hasText(requestDto.getViewYn()))
            return new JSONResponse<>(500, "노출 상태를 선택해주세요.");
        if(!hasText(requestDto.getContent()))
            return new JSONResponse<>(500, "내용을 입력해주세요.");

        boolean isNewNotice = YN.Y.getYn().equals(requestDto.getNoticeYn());
        boolean isCurrentlyNotice = YN.Y == boardNotice.getNoticeYn();

        if (isNewNotice && !isCurrentlyNotice) {
            Long noticeCount = boardNoticeRepository.searchAllNoticeCount();

            if (noticeCount >= 2)
                throw new IllegalArgumentException("공지는 최대 2개까지 설정 가능합니다.");
        }

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String memberAdminId = auth.getName();
        MemberAdmin memberAdmin = memberAdminRepository
                .findByLoginId(memberAdminId)
                .orElseThrow(() -> new IllegalArgumentException("일치하는 회원이 없습니다."));

        boardNotice.setTitle(requestDto.getTitle());
        boardNotice.setContent(requestDto.getContent());
        boardNotice.setViewYn(YN.ofYn(requestDto.getViewYn()));
        boardNotice.setNoticeYn(YN.ofYn(requestDto.getNoticeYn()));
        boardNotice.setAttachment(attachmentService.findAllUploadFileDtoByUUID(requestDto.getFileUUId()));
        boardNotice.setMemberAdminId(memberAdmin.getId());

        try {
            boardNoticeRepository.save(boardNotice);
        } catch (Exception e) {
            return new JSONResponse<>(500, "ERROR");
        }

        return new JSONResponse<>(200, "SUCCESS");
    }

    /**
     * 관리자 | 홈 > 공지사항
     *
     * @author SungHa
     */
    public List<BoardNoticeListDto> searchAllBoardNoticeByMain() {
        return boardNoticeRepository.searchAllBoardNoticeByMain();
    }
}
