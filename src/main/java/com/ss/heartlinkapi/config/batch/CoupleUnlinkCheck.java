package com.ss.heartlinkapi.config.batch;

import com.ss.heartlinkapi.couple.service.CoupleService;
import io.micrometer.core.annotation.Counted;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

@Component
public class CoupleUnlinkCheck {

    private final CoupleService coupleService;

    public CoupleUnlinkCheck(CoupleService coupleService) {
        this.coupleService = coupleService;
    }


//    @Scheduled(cron = "0 * * * * ?")
////    @Scheduled(cron = "0 0 0 * * ?")
//    public void CoupleUnlinkCheck() {
//        System.out.println("배치다아아아CoupleUnlinkCheck");
//        coupleService.batchFinalUnlinkCouple();
//        System.out.println(LocalDateTime.now());
//    }
}
