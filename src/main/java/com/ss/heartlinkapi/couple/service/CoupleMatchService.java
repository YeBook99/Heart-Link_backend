package com.ss.heartlinkapi.couple.service;

import com.ss.heartlinkapi.couple.entity.CoupleEntity;
import com.ss.heartlinkapi.couple.repository.CoupleMatchAnswerRepository;
import com.ss.heartlinkapi.couple.repository.CoupleMatchRepository;
import com.ss.heartlinkapi.linkmatch.entity.LinkMatchAnswerEntity;
import com.ss.heartlinkapi.linkmatch.entity.LinkMatchEntity;
import com.ss.heartlinkapi.user.entity.UserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@Service
public class CoupleMatchService {

    @Autowired
    private CoupleMatchAnswerRepository answerRepository;

    @Autowired
    private CoupleMatchRepository matchRepository;

    @Autowired
    private CoupleService coupleService;


    // 커플 답변 저장
    public void answerSave(Long userId, Long questionId, int selectedOption) {

        LinkMatchAnswerEntity answerEntity = new LinkMatchAnswerEntity();

        UserEntity user = new UserEntity();
        user.setUserId(1L);
        user.setPhone("123456789");
        Date date = new Date();
        LocalDateTime localDate = date.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();
        user.setCreatedAt(localDate);

        answerEntity.setUserId(user);
        LinkMatchEntity match = matchRepository.findById(questionId).orElse(null);
        answerEntity.setMatchId(match);
        CoupleEntity couple = coupleService.findById(userId);
        answerEntity.setCoupleId(couple);
        answerEntity.setCreatedAt(new Date());
        answerEntity.setChoice(selectedOption);

        answerRepository.save(answerEntity);
    }
}
