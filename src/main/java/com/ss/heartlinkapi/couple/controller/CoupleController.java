package com.ss.heartlinkapi.couple.controller;

import com.ss.heartlinkapi.couple.dto.Dday;
import com.ss.heartlinkapi.couple.entity.CoupleEntity;
import com.ss.heartlinkapi.couple.service.CoupleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/couple")
public class CoupleController {

    @Autowired
    private CoupleService coupleService;

    // 디데이 설정
    @PostMapping("/dday")
    public ResponseEntity<?> setAnniversaryDay(@RequestBody Dday dDayData) {
        // 오류 500 검사
        try{
            // 오류 400 검사
            if(dDayData == null) {
                return ResponseEntity.badRequest().build();
            }

            CoupleEntity couple = coupleService.findById(dDayData.getCoupleId());

            // 오류 404 검사
            if(couple == null) {
                return ResponseEntity.notFound().build();
            }
            couple.setAnniversaryDate(dDayData.getFirstMetDate());
            CoupleEntity setCouple = coupleService.setAnniversary(couple);
            return ResponseEntity.ok(setCouple);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }

    // 디데이 수정
    @PutMapping("/dday/update")
    public ResponseEntity<?> updateAnniversaryDay(@RequestBody Dday dDayData) {
        // 오류 500 검사
        try{
            // 오류 400 검사
            if(dDayData == null) {
                return ResponseEntity.badRequest().build();
            }
            CoupleEntity couple = coupleService.findById(dDayData.getCoupleId());
            // 오류 404 검사
            if(couple == null) {
                return ResponseEntity.notFound().build();
            }
            couple.setAnniversaryDate(dDayData.getFirstMetDate());
            CoupleEntity result = coupleService.setAnniversary(couple);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }


}
