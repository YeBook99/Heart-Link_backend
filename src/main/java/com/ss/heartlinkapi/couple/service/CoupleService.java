package com.ss.heartlinkapi.couple.service;

import com.ss.heartlinkapi.couple.entity.CoupleEntity;
import com.ss.heartlinkapi.couple.repository.CoupleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CoupleService {
    @Autowired
    private CoupleRepository coupleRepository;

    public CoupleEntity findByUser1_IdOrUser2_Id(Long id) {
        return coupleRepository.findCoupleByUserId(id);
    }
}
