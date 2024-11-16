package com.cuv.admin.web.scheduler;

import com.cuv.admin.domain.linklisting.LinkListingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Tag(
        name = "관리자 -> 카이즈유 스케줄러",
        description = "관리자 -> 카이즈유 스케줄러"
)
@Component
@RequiredArgsConstructor
@EnableAsync
@Slf4j
public class LinkCodeScheduler {

    private final LinkListingService linkListingService;

    /**
     * 관리자 | 카이즈유 > 업데이트된 차량 등급 및 해당 하는 정보 가져오기
     *
     * @author SungHa
     */
    @Operation(
            summary = "관리자 | 카이즈유 > 업데이트된 차량 등급 및 해당 하는 정보 가져오기",
            description = "관리자 카이즈유 업데이트된 차량 등급 및 해당 하는 정보 가져오기"
    )
    @Scheduled(cron = "0 2 0 * * ?")
    public void getNewGradeList() throws InterruptedException {
        log.info("start getNewGradeList at - {}", new Date());
        long ms = (long) (Math.random() * 25000);
        Thread.sleep(ms);

        // 신차 업데이트 차량 목록 조회 (s3)
        List<Map<String, String>> newUpdatedGradeList = new ArrayList<>();
        try {
            newUpdatedGradeList = linkListingService.adminManagementGetNewCarGradeList();
            log.info("Scheduler newUpdatedGradeList SUCCESS *************************** = {} ", newUpdatedGradeList.size());

        } catch (Exception e) {
            log.error("Get New Grade List Error = {}", e.getMessage());
        }

        log.info("Get New Grade List End ***************************");

        // 신차 업데이트 차량 상세정보 조회 (s4)
        if (!newUpdatedGradeList.isEmpty()) {
            try {
                linkListingService.adminManagementGetUpdatedData(newUpdatedGradeList);
                log.info("Scheduler adminManagementGetUpdatedData SUCCESS *************************** ");

            } catch (Exception e) {
                log.error("Save New Grade List Error = {}", e.getMessage());
            }
        }

        log.info("Save New Detail List End ***************************");

    }
}
