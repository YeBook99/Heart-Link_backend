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
import java.util.*;

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
    public LinkMatchAnswerEntity answerSave(MatchAnswer matchAnswer, Long userId) {

        LinkMatchAnswerEntity answerEntity = new LinkMatchAnswerEntity();

        answerEntity.setUserId(userRepository.findById(userId).orElse(null));
        LinkMatchEntity match = matchRepository.findById(matchAnswer.getQuestionId()).orElse(null);
        answerEntity.setMatchId(match);
        CoupleEntity couple = coupleService.findByUser1_IdOrUser2_Id(userId);
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
    public List<Map<String, Object>> findAnswerListByCoupleId(CoupleEntity couple, Long userId) {
        List<LinkMatchAnswerEntity> coupleList = answerRepository.findByCoupleId(couple);
        List<Map<String, Object>> answerList = new ArrayList<>();
        for(LinkMatchAnswerEntity answerEntity : coupleList) {
            Map<String, Object> totalAnswerMap = new HashMap<>();
            LinkMatchEntity matchQuestion = matchRepository.findById(answerEntity.getMatchId().getLinkMatchId()).orElse(null);
            totalAnswerMap.put("match1", matchQuestion.getMatch1());
            totalAnswerMap.put("match2", matchQuestion.getMatch2());
            if(answerEntity.getUserId().getUserId()==userId) {
                Map<String, Object> myAnswerMap = new HashMap<>();
                myAnswerMap.put("myChoice", answerEntity.getChoice());
                myAnswerMap.put("myDate", answerEntity.getCreatedAt());
                totalAnswerMap.put("myAnswer", myAnswerMap);
            } else {
                Map<String, Object> partnerAnswerMap = new HashMap<>();
                partnerAnswerMap.put("partnerChoice", answerEntity.getChoice());
                partnerAnswerMap.put("partnerDate", answerEntity.getCreatedAt());
                totalAnswerMap.put("partnerAnswer", partnerAnswerMap);
            }
            answerList.add(totalAnswerMap);
        }
        // 상대 답, 내 답, 날짜, 매치1, 매치2
        return answerList;
    }
}
