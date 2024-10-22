package com.ss.heartlinkapi.linkmatch.service;

import com.ss.heartlinkapi.admin.service.AdminCoupleMatchService;
import com.ss.heartlinkapi.linkmatch.entity.LinkMatchEntity;
import com.ss.heartlinkapi.linkmatch.repository.CoupleMatchAnswerRepository;
import com.ss.heartlinkapi.linkmatch.repository.CoupleMatchRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CoupleMatchStatisticsService {

    @Autowired
    private CoupleMatchAnswerRepository matchAnswerRepository;

    @Autowired
    private CoupleMatchRepository matchRepository;

    @Autowired
    private AdminCoupleMatchService adminCoupleMatchService;

    // 성별 별 매치 선택 횟수로 통계 구하기
    public void matchCountGenderById(LinkMatchEntity matchQuestion) {
        List<Object[]> result = matchAnswerRepository.matchCountGenderById(matchQuestion.getLinkMatchId());
        int choice0ByF = 0, choice1ByF = 0 ,choice0ByM = 0, choice1ByM = 0;
        for(int i=0; i<result.size(); i++) {
                int choice = Integer.parseInt(result.get(i)[2].toString());
                if(result.get(i)[1].equals("M")&&choice==0) {
                    System.out.println("남: "+result.get(i)[1]);
                    System.out.println("선택지 0: "+result.get(i)[2]);
                    System.out.println("갯수: "+result.get(i)[3]);
                    choice0ByM = Integer.parseInt(result.get(i)[3].toString());
                } else if(result.get(i)[1].equals("M")&&choice==1){
                    System.out.println("남: "+result.get(i)[1]);
                    System.out.println("선택지 1: "+result.get(i)[2]);
                    System.out.println("갯수: "+result.get(i)[3]);
                    choice1ByM = Integer.parseInt(result.get(i)[3].toString());
                } else if(result.get(i)[1].equals("F")&&choice==0){
                    System.out.println("여: "+result.get(i)[1]);
                    System.out.println("선택지 0: "+result.get(i)[2]);
                    System.out.println("갯수: "+result.get(i)[3]);
                    choice0ByF = Integer.parseInt(result.get(i)[3].toString());
                } else if(result.get(i)[1].equals("F")&&choice==1){
                    System.out.println("여: "+result.get(i)[1]);
                    System.out.println("선택지 1: "+result.get(i)[2]);
                    System.out.println("갯수: "+result.get(i)[3]);
                    choice1ByF = Integer.parseInt(result.get(i)[3].toString());
                }
            }
        int totalFCount = choice0ByF+choice1ByF;
        int totalMCount = choice0ByM+choice1ByM;
        System.out.println("totalMCount"+totalMCount);
        System.out.println("totalFCount"+totalFCount);
        System.out.println("choice0ByF"+choice0ByF);
        System.out.println("choice1ByF"+choice1ByF);
        System.out.println("choice0ByM"+choice0ByM);
        System.out.println("choice1ByM"+choice1ByM);
        int select0RateF = (int)Math.round((((double)choice0ByF/totalFCount)*100));
        int select1RateF = (int)Math.round((((double)choice1ByF/totalFCount)*100));
        int select0RateM = (int)Math.round((((double)choice0ByM/totalMCount)*100));
        int select1RateM = (int)Math.round((((double)choice1ByM/totalMCount)*100));
        System.out.println(select0RateF);
        System.out.println(select1RateF);
        System.out.println(select0RateM);
        System.out.println(select1RateM);

        Map<String, Object> matchGenderRate = new HashMap<>();
        

//        {
//            "matchId": 0,
//                "questionId": 0,
//                "selectedOption": "string",
//                "timestamp": "2024-10-21T06:43:46.940Z"
//        }

        }

    // 날짜로 매치 질문 조회
    public LinkMatchEntity findMatchByDate(LocalDate today) {
        return matchRepository.findByDisplayDate(today);
    }
}

