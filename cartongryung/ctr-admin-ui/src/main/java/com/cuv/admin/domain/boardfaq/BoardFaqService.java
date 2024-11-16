package com.cuv.admin.domain.boardfaq;

import com.cuv.admin.common.JSONResponse;
import com.cuv.admin.common.YN;
import com.cuv.admin.domain.boardfaq.dto.BoardFaqDetailDto;
import com.cuv.admin.domain.boardfaq.dto.BoardFaqListDto;
import com.cuv.admin.domain.boardfaq.dto.BoardFaqSearchDto;
import com.cuv.admin.domain.boardfaq.entity.BoardFaq;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

import static org.springframework.util.StringUtils.hasText;

/**
 * 자주 묻는 질문 서비스 (조회, 저장, 수정, 삭제)
 */
@Service
@Transactional
@RequiredArgsConstructor
public class BoardFaqService {

    private final BoardFaqRepository boardFaqRepository;

    /**
     * 관리자 | 게시판 > 자주 묻는 질문 목록
     *
     * @param condition 검색 조건을 담은 DTO
     * @author 송다운
     */
    public Page<BoardFaqListDto> searchAllBoardFaq(BoardFaqSearchDto condition, Pageable request) {

        return boardFaqRepository.searchAllBoardFaq(condition, request);
    }


    /**
     * 관리자 | 게시판 > 자주 묻는 질문 > 저장
     *
     * @param map 글 정보를 담은 map
     * @author 송다운
     */
    public JSONResponse<?> adminBoardFaqSave(Map<String, Object> map) {
        String title = map.get("title").toString();
        String show = map.get("show").toString();
        String content = map.get("content").toString();

        if(!hasText(title))
            return new JSONResponse<>(500, "제목을 입력해주세요.");
        if(!hasText(show))
            return new JSONResponse<>(500, "노출 상태를 선택해주세요.");
        if(!hasText(content))
            return new JSONResponse<>(500, "내용을 입력해주세요.");

        BoardFaq boardFaq = BoardFaq.builder()
                .title(title)
                .content(content)
                .viewYn(YN.ofYn(show))
                .build();

        try {
            boardFaqRepository.save(boardFaq);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new JSONResponse<>(200, "SUCCESS");
    }


    /**
     * 관리자 | 게시판 > 자주 묻는 질문 > 상세
     *
     * @param id 글 시퀀스
     * @author 송다운
     */
    public BoardFaqDetailDto searchBoardFaqById(Long id) {
        return boardFaqRepository.searchBoardFaqById(id);
    }


    /**
     * 관리자 | 게시판 > 자주 묻는 질문 > 삭제
     *
     * @param map 글 정보를 담은 map
     * @author 송다운
     */
    public JSONResponse<?> adminBoardFaqDeleteProc(Map<String, Object> map) {
        Long id = Long.parseLong(map.get("id").toString());

        BoardFaq boardFaq = boardFaqRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 존재하지 않습니다."));

        boardFaq.setDelYn(YN.Y);
        try {
            boardFaqRepository.save(boardFaq);
        } catch (Exception e) {
            return new JSONResponse<>(500, e.getMessage());
        }
        return new JSONResponse<>(200, "SUCCESS");
    }


    /**
     * 관리자 | 게시판 > 자주 묻는 질문 > 수정
     * @param map
     * @return
     * @author 송다운
     */
    public JSONResponse<?> adminBoardFaqUpdate(Map<String, Object> map) {
        Long id = Long.parseLong(map.get("id").toString());
        String show = map.get("show").toString();
        String title = map.get("title").toString();
        String content = map.get("content").toString();

        BoardFaq boardFaq = boardFaqRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 존재하지 않습니다."));

        boardFaq.setTitle(title);
        boardFaq.setContent(content);
        boardFaq.setViewYn(YN.ofYn(show));

        try {
            boardFaqRepository.save(boardFaq);
        } catch (Exception e) {
            return new JSONResponse<>(500, e.getMessage());
        }
        return new JSONResponse<>(200, "SUCCESS");
    }
}
