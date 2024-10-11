package com.ss.heartlinkapi.couple.service;

import com.ss.heartlinkapi.couple.entity.CoupleEntity;
import com.ss.heartlinkapi.couple.repository.CoupleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CoupleService {
    @Autowired
    private CoupleRepository coupleRepository;

    // 유저아이디로 커플아이디 조회
    public CoupleEntity findByUser1_IdOrUser2_Id(Long id) {
        return coupleRepository.findCoupleByUserId(id);
    }
}
