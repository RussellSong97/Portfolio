package com.cuv.domain.pick;

import com.cuv.domain.pick.dto.PickCountDto;
import com.cuv.domain.pick.dto.PickListDto;

import java.util.List;

public interface PickRepositoryCustom {

    List<PickListDto> searchPickListNoPageList(Long memberId);

    Long searchPicksMemberIdReadYn(Long id);

    Long searchPicksMemberIdCount(Long memberId);

    Long searchPickCountByProductId(Long productId);

    PickCountDto searchPickSummary(Long memberId);

}
