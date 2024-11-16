package com.cuv.domain.boardnotice;

import com.cuv.domain.boardnotice.dto.BoardNoticeDetailDto;
import com.cuv.domain.boardnotice.dto.BoardNoticeListDto;
import com.cuv.domain.boardnotice.dto.BoardNoticeSearchDto;
import com.cuv.domain.boardnotice.entity.BoardNotice;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 공지사항 서비스 (공지사항 조회)
 */
@Service
@RequiredArgsConstructor
@Transactional
public class BoardNoticeService {

    private final BoardNoticeRepository boardNoticeRepository;

    /**
     * 안내 > 공지사황 > 목록
     *
     * @param condition 검색 조건을 담은 DTO
     * @author SungHa
     */
    public Page<BoardNoticeListDto> searchAllBoardNotice(BoardNoticeSearchDto condition, Pageable request) {
        return boardNoticeRepository.searchAllBoardNotice(condition, request);
    }

    /**
     * 안내 > 공지사황 > 상세
     *
     * @param id 글 시퀀스
     * @author SungHa
     */
    public BoardNoticeDetailDto searchBoardNoticeById(Long id) {
        return boardNoticeRepository.searchBoardNoticeById(id);
    }

    /**
     * 안내 > 공지사황 > 조회수 증가
     *
     * @param id 글 시퀀스
     * @author SungHa
     */
    @Transactional
    public void addHit(Long id) {
        boardNoticeRepository.findById(id).ifPresent(BoardNotice::addHits);
    }

    /**
     * 메인 > 공지사황
     *
     * @author SungHa
     */
    public BoardNoticeDetailDto searchBoardNoticeByMain() {
        return boardNoticeRepository.searchBoardNoticeByMain();
    }
}
