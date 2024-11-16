package com.cuv.domain.boardguide;

import com.cuv.domain.boardguide.dto.BoardGuideDetailDto;
import com.cuv.domain.boardguide.dto.BoardGuideListDto;
import com.cuv.domain.boardguide.entity.BoardGuide;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BoardGuideService {

    private final BoardGuideRepository boardGuideRepository;

    /**
     * 안내 > 중고차 가이드 > 목록
     *
     * @author SungHa
     */
    public List<BoardGuideListDto> searchAllBoardGuide() {
        return boardGuideRepository.searchAllBoardGuide();
    }

    /**
     * 안내 > 중고차 가이드 > 상세
     *
     * @param id 글 시퀀스
     * @author SungHa
     */
    public BoardGuideDetailDto searchBoardGuideById(Long id) {
        return boardGuideRepository.searchBoardGuideById(id);
    }

}
