package com.ss.heartlinkapi.couple.service;

import com.ss.heartlinkapi.couple.dto.MatchAnswer;
import com.ss.heartlinkapi.couple.entity.CoupleEntity;
import com.ss.heartlinkapi.couple.repository.CoupleMatchAnswerRepository;
import com.ss.heartlinkapi.couple.repository.CoupleMatchRepository;
import com.ss.heartlinkapi.couple.repository.CoupleUserRepository;
import com.ss.heartlinkapi.linkmatch.entity.LinkMatchAnswerEntity;
import com.ss.heartlinkapi.linkmatch.entity.LinkMatchEntity;
import com.ss.heartlinkapi.user.entity.UserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
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

    @Autowired
    private CoupleUserRepository coupleUserRepository;

    // 커플 답변 저장
    public LinkMatchAnswerEntity answerSave(MatchAnswer matchAnswer) {

        LinkMatchAnswerEntity answerEntity = new LinkMatchAnswerEntity();

        answerEntity.setUserId(coupleUserRepository.findById(matchAnswer.getUserId()).orElse(null));
        LinkMatchEntity match = matchRepository.findById(matchAnswer.getQuestionId()).orElse(null);
        answerEntity.setMatchId(match);
        CoupleEntity couple = coupleService.findByUser1_IdOrUser2_Id(matchAnswer.getUserId());
        answerEntity.setCoupleId(couple);
        Date now = new Date();
        LocalDate today = now.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        answerEntity.setCreatedAt(today);
        answerEntity.setChoice(matchAnswer.getSelectedOption());

        try{
            LinkMatchAnswerEntity entity = answerRepository.save(answerEntity);

            System.out.println("커플아이디 : "+couple.getCoupleId());
            int result = answerRepository.checkTodayMatch(couple.getCoupleId());
            System.out.println("결과 : "+result);
            return entity;
        } catch (Exception e){
            e.printStackTrace();
            return null;
        }

    }

    // 매일 새 매치질문 조회
    public LinkMatchEntity getMatchQuestion() {
        Date now = new Date();
        LocalDate today = now.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        return matchRepository.findByDisplayDate(today);
    }

}
