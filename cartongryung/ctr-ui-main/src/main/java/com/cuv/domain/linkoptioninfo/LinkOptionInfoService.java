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

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class LinkOptionInfoService {
    private final LinkOptionInfoRepository linkOptionInfoRepository;

    public JSONResponse<?> searchApiProductOption() {
        List<LinkOptionInfoListDto> list = linkOptionInfoRepository.searchApiProductOptionList();

        for(LinkOptionInfoListDto info : list) {
            System.out.println("넘어온 info : " + info.getOptionName());
        }
        return new JSONResponse<>(200, "SUCCESS", list);
    }

    public JSONResponse<?> searchApiProductMoreOption() {
        try {
            List<SpecGroupDto> list = linkOptionInfoRepository.searchApiProductMoreOptionList();

            return new JSONResponse<>(200, "SUCCESS", list);
        } catch (Exception e) {
            log.error(e.getMessage());
            return new JSONResponse<>(500, e.getMessage());
        }


    }

    public List<LinkOptionInfoListDto> searchMobileOptionList() {
        return linkOptionInfoRepository.searchMobileOptionList();
    }
}
