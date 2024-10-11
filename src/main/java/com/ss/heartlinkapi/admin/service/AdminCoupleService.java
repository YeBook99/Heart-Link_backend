package com.ss.heartlinkapi.admin.service;

import com.ss.heartlinkapi.admin.repository.AdminCoupleMatchRepository;
import com.ss.heartlinkapi.linkmatch.entity.LinkMatchEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class AdminCoupleService {

    @Autowired
    private AdminCoupleMatchRepository coupleMatchRepository;

    public Page<LinkMatchEntity> findAllByOrderByIdDesc(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return coupleMatchRepository.findAllByOrderByLinkMatchIdDesc(pageable);
    }

    public void addMatchQuestion(LinkMatchEntity questionText) {
        coupleMatchRepository.save(questionText);
    }
}
