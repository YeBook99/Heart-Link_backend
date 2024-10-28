package com.ss.heartlinkapi.couple.controller;

import com.ss.heartlinkapi.couple.dto.CoupleCode;
import com.ss.heartlinkapi.couple.dto.Dday;
import com.ss.heartlinkapi.couple.entity.CoupleEntity;
import com.ss.heartlinkapi.couple.repository.CoupleRepository;
import com.ss.heartlinkapi.couple.service.CoupleService;
import com.ss.heartlinkapi.post.entity.PostEntity;
import com.ss.heartlinkapi.user.entity.UserEntity;
import com.ss.heartlinkapi.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

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

            if(setCouple == null) {
                return ResponseEntity.badRequest().body("기념일이 설정 실패하였습니다.");
            }

            return ResponseEntity.ok().body("기념일이 설정되었습니다.");

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
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

            if(result == null) {
                return ResponseEntity.badRequest().body("기념일 수정 실패하였습니다.");
            }
            return ResponseEntity.ok().body("기념일이 수정되었습니다.");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
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

            // 유저 검색 실패
            if(user == null) {
                return ResponseEntity.notFound().build();
            }

            if(user.getCoupleCode().equals(code.getCode())) {
                // 자신의 코드를 입력했을 때
                return ResponseEntity.badRequest().body("자신의 커플 코드를 입력할 수 없습니다.");
            }

            // 존재하지 않는 커플코드인지 확인
            int checkResult = coupleService.codeCheck(code);
            if(checkResult==1) {
                return ResponseEntity.badRequest().body("존재하지 않는 커플코드입니다.");
            } else if(checkResult==2) {
                return ResponseEntity.badRequest().body("이미 커플 연결되어 있는 상대방입니다.");
            }

            CoupleEntity result = coupleService.coupleCodeMatch(user, code);

            if(result == null) {
                return ResponseEntity.badRequest().body("커플 연결에 실패하였습니다.");
            }

            return ResponseEntity.status(HttpStatus.CREATED).body(result);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }

    //  커플 해지 (break_date 유예 3개월 설정)
    @PutMapping("/{coupleId}/unlink")
    public ResponseEntity<?> unlinkCouple(@PathVariable Long coupleId) {
        try{
            if(coupleId == null) {
                return ResponseEntity.badRequest().build();
            }

            CoupleEntity couple = coupleService.findById(coupleId);

            if(couple == null) {
                return ResponseEntity.notFound().build();
            }

            CoupleEntity result = coupleService.setBreakDate(couple);

            if(result == null) {
                return ResponseEntity.badRequest().body("연결 해지 실패");
            }

            return ResponseEntity.ok(result);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }

    // 연결 해지 취소 (break_date 삭제)
    @PutMapping("/{coupleId}/unlink/cancel")
    public ResponseEntity<?> cancelUnlinkCouple(@PathVariable Long coupleId) {
        try{
            if(coupleId == null) {
                return ResponseEntity.badRequest().build();
            }

            CoupleEntity couple = coupleService.findById(coupleId);

            if(couple == null) {
                return ResponseEntity.notFound().build();
            }

            CoupleEntity result = coupleService.deleteBreakDate(couple);
            if (result == null) {
                return ResponseEntity.badRequest().body("연결 해지 취소 실패");
            }
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }

//    // 유예기간 종료 후 최종 연결 해지
//    @DeleteMapping("/{coupleId}/finalUnlink")
//    public ResponseEntity<?> finalUnlinkCouple(@PathVariable Long coupleId) {
//        System.out.println("finalUnlinkCouple");
//
//        try{
//            if(coupleId == null) {
//                return ResponseEntity.badRequest().build();
//            }
//
//            CoupleEntity couple = coupleService.findById(coupleId);
//
//            if(couple == null) {
//                return ResponseEntity.notFound().build();
//            }
//            boolean result = coupleService.finalUnlinkCouple(couple);
//
//            if(result) {
//                return ResponseEntity.noContent().build();
//            } else {
//                return ResponseEntity.badRequest().body("아직 커플 유예기간입니다.");
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//            return ResponseEntity.internalServerError().build();
//        }
//    }

    // 유예기간 없이 즉시 연결 해지
    @DeleteMapping("/{coupleId}/finalNowUnlink")
    public ResponseEntity<?> finalNowUnlinkCouple(@PathVariable Long coupleId) {
        try{
            if(coupleId == null) {
                return ResponseEntity.badRequest().build();
            }

            CoupleEntity couple = coupleService.findById(coupleId);

            if(couple == null) {
                return ResponseEntity.notFound().build();
            }

            coupleService.finalNowUnlinkCouple(couple);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }

    }

}
