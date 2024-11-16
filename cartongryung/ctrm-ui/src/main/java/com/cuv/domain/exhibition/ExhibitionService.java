package com.cuv.domain.exhibition;

import com.cuv.domain.exhibition.dto.ExhibitionDetailDto;
import com.cuv.domain.exhibition.dto.ExhibitionListDto;
import com.cuv.domain.exhibition.dto.ExhibitionSearchDto;
import com.cuv.domain.exhibition.entity.Exhibition;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 전시 서비스 (전시 관리 조회)
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ExhibitionService {

    private final ExhibitionRepository exhibitionRepository;

    /**
     * 메인 > 전시 배너
     *
     * @param exhibitionType 전시 유형
     * @author SungHa
     */
    @Cacheable(value = "topStripBanner", key = "#exhibitionType")
    public List<ExhibitionListDto> searchAllExhibition(String exhibitionType) {
        return exhibitionRepository.searchAllExhibition(exhibitionType);
    }

    @Scheduled(cron = "0 */1 * * * *")
    @CacheEvict(value = "topStripBanner", allEntries = true)
    public void evictCachesByTopStripBanner() {
        log.info("cacheEvictScheduler() has been called.");
    }

    /**
     * 이벤트
     *
     * @author SungHa
     */
    public List<ExhibitionListDto> searchAllEvent(ExhibitionSearchDto condition) {
        return exhibitionRepository.searchAllEvent(condition);
    }

    /**
     * 이벤트 > 상세
     *
     * @param id 전시 시퀀스
     * @author SungHa
     */
    public ExhibitionDetailDto searchExhibitionById(Long id) {
        return exhibitionRepository.searchExhibitionById(id);
    }

    /**
     * 이벤트 > 조회수 증가
     *
     * @param id 글 시퀀스
     * @author SungHa
     */
    @Transactional
    public void addHit(Long id) {
        exhibitionRepository.findById(id).ifPresent(Exhibition::addHits);
    }

}
