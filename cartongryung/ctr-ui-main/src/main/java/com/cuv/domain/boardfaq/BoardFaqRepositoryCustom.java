package com.cuv.domain.boardfaq;

import com.cuv.domain.boardfaq.dto.BoardFaqListDto;

import java.util.List;

public interface BoardFaqRepositoryCustom {
    List<BoardFaqListDto> searchAllBoardFaq();

}
