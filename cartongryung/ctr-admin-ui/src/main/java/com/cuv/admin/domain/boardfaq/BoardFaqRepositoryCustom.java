package com.cuv.admin.domain.boardfaq;

import com.cuv.admin.domain.boardfaq.dto.BoardFaqDetailDto;
import com.cuv.admin.domain.boardfaq.dto.BoardFaqListDto;
import com.cuv.admin.domain.boardfaq.dto.BoardFaqSearchDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BoardFaqRepositoryCustom {
    Page<BoardFaqListDto> searchAllBoardFaq(BoardFaqSearchDto condition, Pageable request);

    BoardFaqDetailDto searchBoardFaqById(Long id);
}
