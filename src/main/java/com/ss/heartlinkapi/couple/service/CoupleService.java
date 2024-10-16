package com.ss.heartlinkapi.couple.service;

import com.ss.heartlinkapi.couple.entity.CoupleEntity;
import com.ss.heartlinkapi.couple.repository.CoupleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CoupleService {
    @Autowired
    private CoupleRepository coupleRepository;

//    @Autowired
//    private

    // 유저아이디로 커플아이디 조회
    public CoupleEntity findByUser1_IdOrUser2_Id(Long id) {
        return coupleRepository.findCoupleByUserId(id);
    }

    // 커플 아이디로 커플 객체 반환
    public CoupleEntity findById(Long coupleId) {
        return coupleRepository.findById(coupleId).orElse(null);
    }

    // 디데이 날짜 추가/수정
    public CoupleEntity setAnniversary(CoupleEntity couple) {
        return coupleRepository.save(couple);
    }

    // 커플 매칭 카운트 증가
    public int matchCountUp(Long coupleId) {
        return coupleRepository.matchCountUp(coupleId);
    }

    // 커플 연결
    public CoupleEntity coupleCodeMatch(Long userId, String code) {
        return null;
    }
}
