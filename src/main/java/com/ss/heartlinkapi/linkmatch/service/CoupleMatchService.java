package com.ss.heartlinkapi.linkmatch.service;

import com.ss.heartlinkapi.couple.service.CoupleService;
import com.ss.heartlinkapi.linkmatch.dto.MatchAnswer;
import com.ss.heartlinkapi.couple.entity.CoupleEntity;
import com.ss.heartlinkapi.linkmatch.repository.CoupleMatchAnswerRepository;
import com.ss.heartlinkapi.linkmatch.repository.CoupleMatchRepository;
import com.ss.heartlinkapi.linkmatch.entity.LinkMatchAnswerEntity;
import com.ss.heartlinkapi.linkmatch.entity.LinkMatchEntity;
import com.ss.heartlinkapi.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

@Service
public class CoupleMatchService {

    @Autowired
    private CoupleMatchAnswerRepository answerRepository;

    @Autowired
    private CoupleMatchRepository matchRepository;

    @Autowired
    private CoupleService coupleService;

    @Autowired
    private UserRepository userRepository;

    // 커플 답변 저장
    public LinkMatchAnswerEntity answerSave(MatchAnswer matchAnswer) {

        LinkMatchAnswerEntity answerEntity = new LinkMatchAnswerEntity();

        answerEntity.setUserId(userRepository.findById(matchAnswer.getUserId()).orElse(null));
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

            int result = answerRepository.checkTodayMatching(couple.getCoupleId());
            return entity;
        } catch (Exception e){
            e.printStackTrace();
            return null;
        }

    }

    // 오늘의 매치 질문 조회
    public LinkMatchEntity getMatchQuestion() {
        Date now = new Date();
        LocalDate today = now.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        return matchRepository.findByDisplayDate(today);
    }

    // 매칭 성공 체크
    public int checkTodayMatching(Long coupleId) {
        return answerRepository.checkTodayMatching(coupleId);
    }

    // 매치 답변 내역 조회
    public List<LinkMatchAnswerEntity> findAnswerListByCoupleId(CoupleEntity couple) {
        return answerRepository.findByCoupleId(couple);
    }
}
