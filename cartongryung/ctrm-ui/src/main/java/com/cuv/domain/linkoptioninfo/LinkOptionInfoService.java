package com.cuv.domain.linkoptioninfo;

import com.cuv.common.JSONResponse;
import com.cuv.domain.linkoptioninfo.dto.LinkOptionInfoListDto;
import com.cuv.domain.linkoptioninfo.entity.LinkOptionInfo;
import com.cuv.domain.product.dto.ProductLinkOptionDto;
import com.cuv.domain.product.dto.SpecGroupDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 차량 옵션 정보 조회
 */
@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class LinkOptionInfoService {
    private final LinkOptionInfoRepository linkOptionInfoRepository;

    /**
     * 검색 페이지 옵션 데이터
     * @return
     */
    public JSONResponse<?> searchApiProductOption() {
        List<LinkOptionInfoListDto> list = linkOptionInfoRepository.searchApiProductOptionList();

        return new JSONResponse<>(200, "SUCCESS", list);
    }

    /**
     * 차량 검색 옵션 더 보기 데이터
     * @return
     */
    public JSONResponse<?> searchApiProductMoreOption() {
        try {
            List<SpecGroupDto> list = linkOptionInfoRepository.searchApiProductMoreOptionList();

            return new JSONResponse<>(200, "SUCCESS", list);
        } catch (Exception e) {
            log.error(e.getMessage());
            return new JSONResponse<>(500, e.getMessage());
        }


    }

    /**
     * 모바일 옵션 데이터 현재 주석
     * @return
     */
    public List<LinkOptionInfoListDto> searchMobileOptionList() {
        return linkOptionInfoRepository.searchMobileOptionList();
    }


    /**
     * 모바일 옵션 정보"
     * @return
     */
    public JSONResponse<?> searchMobileOption() {

        return new JSONResponse<>(200, "SUCCESS", linkOptionInfoRepository.searchMobileOptionList());
    }
}
