package com.cuv.domain.boardfaq;

import com.cuv.domain.boardfaq.dto.BoardFaqListDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 자주 묻는 질문 서비스 (자주 묻는 질문 조회)
 */
@Service
@RequiredArgsConstructor
public class BoardFaqService {

    private final BoardFaqRepository boardFaqRepository;

    /**
     * 안내 > 자주 묻는 질문 > 목록
     *
     * @author SungHa
     */
    public List<BoardFaqListDto> searchAllBoardFaq() {
        return boardFaqRepository.searchAllBoardFaq();
    }

}
