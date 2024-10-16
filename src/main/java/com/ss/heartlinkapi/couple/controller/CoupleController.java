package com.ss.heartlinkapi.couple.controller;

import com.ss.heartlinkapi.couple.dto.CoupleCode;
import com.ss.heartlinkapi.couple.dto.Dday;
import com.ss.heartlinkapi.couple.entity.CoupleEntity;
import com.ss.heartlinkapi.couple.service.CoupleService;
import com.ss.heartlinkapi.user.entity.UserEntity;
import com.ss.heartlinkapi.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/couple")
public class CoupleController {

    @Autowired
    private CoupleService coupleService;

    @Autowired
    private UserRepository userRepository;

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
            return ResponseEntity.status(HttpStatus.CREATED).body(setCouple);

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
            return ResponseEntity.status(HttpStatus.CREATED).body(result);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }

    // 자신의 커플 코드 확인
    @GetMapping("/match/{userid}/code")
    public ResponseEntity<?> selectMyCode(@PathVariable Long userid) {
        try{
            if(userid == null) {
                return ResponseEntity.badRequest().build();
            }

            UserEntity user = userRepository.findById(userid).orElse(null);

            if(user == null) {
                return ResponseEntity.notFound().build();
            }

            return ResponseEntity.ok(user.getCoupleCode());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }

    // 커플 연결
    @PostMapping("/match/{userId}/code/link")
    public ResponseEntity<?> coupleCodeMatch(@PathVariable("userId") Long userId, @RequestBody CoupleCode code){
        try{
            if(userId == null || code == null) {
                return ResponseEntity.badRequest().build();
            }
            UserEntity user = userRepository.findById(userId).orElse(null);
            if(user == null) {
                return ResponseEntity.notFound().build();
            }

            CoupleEntity result = coupleService.coupleCodeMatch(user, code);

            if(result == null) {
                return ResponseEntity.notFound().build();
            }

            return ResponseEntity.status(HttpStatus.CREATED).body(result);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }




}
