package com.cuv.admin.domain.boardnotice;

import com.cuv.admin.domain.boardnotice.dto.BoardNoticeDetailDto;
import com.cuv.admin.domain.boardnotice.dto.BoardNoticeListDto;
import com.cuv.admin.domain.boardnotice.dto.BoardNoticeSearchDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface BoardNoticeRepositoryCustom {
    Page<BoardNoticeListDto> searchAllBoardNotice(BoardNoticeSearchDto condition, Pageable request);

    BoardNoticeDetailDto searchBoardNoticeById(Long id);

    Long searchAllNoticeCount();

    List<BoardNoticeListDto> searchAllBoardNoticeByMain();
}
