package com.ss.heartlinkapi.linkmatch.service;

import com.ss.heartlinkapi.admin.service.AdminCoupleMatchService;
import com.ss.heartlinkapi.couple.entity.CoupleEntity;
import com.ss.heartlinkapi.couple.repository.CoupleRepository;
import com.ss.heartlinkapi.linkmatch.dto.MatchGenderRateDTO;
import com.ss.heartlinkapi.linkmatch.entity.LinkMatchEntity;
import com.ss.heartlinkapi.linkmatch.repository.CoupleMatchAnswerRepository;
import com.ss.heartlinkapi.linkmatch.repository.CoupleMatchRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
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

    @Autowired
    private CoupleMatchRepository coupleMatchRepository;

    @Autowired
    private CoupleRepository coupleRepository;

    // 통계 - 커플 매치 통계
    public Map<String, Object> matchRate(LinkMatchEntity matchQuestion, Long coupleId) {
        List<Object[]> result = matchAnswerRepository.matchCountGenderById(matchQuestion.getLinkMatchId());

        // 성별 별 매치 선택 카운트, 비율
        MatchGenderRateDTO genderRateResult = matchGenderRate(result);
        // 오늘 매치 질문에 대한 매칭 성공 확률
        int todayMatchRate = matchCoupleRate(matchQuestion);
        // 이번 달 모든 커플의 매칭 성공 횟수
        // 0 : 이번달, 1 : 저번달, 2 : 저저번달
        int[] monthAllMatchCount = monthSuccessMatchCount();
        // 해당 커플의 매치카운트 횟수 조회
        CoupleEntity couple = coupleRepository.findById(coupleId).orElse(null);
        monthSuccessMatchCountByCoupleId(coupleId);

        // 커플 조회 실패 시
        if(couple == null) {
            return null;
        }

        Map<String, Object> mathRate = new HashMap<>();
//        mathRate.put("gender_m_0_count", genderRateResult.getChoice0ByM());
//        mathRate.put("gender_m_1_count", genderRateResult.getChoice1ByM());
//        mathRate.put("gender_f_0_count", genderRateResult.getChoice0ByF());
//        mathRate.put("gender_f_1_count", genderRateResult.getChoice1ByF());
//        mathRate.put("gender_m_totalCount", genderRateResult.getTotalMCount());
//        mathRate.put("gender_f_totalCount", genderRateResult.getTotalFCount());
        mathRate.put("gender_m_0_rate", genderRateResult.getSelect0RateM());
        mathRate.put("gender_m_1_rate", genderRateResult.getSelect1RateM());
        mathRate.put("gender_f_0_rate", genderRateResult.getSelect0RateF());
        mathRate.put("gender_f_1_rate", genderRateResult.getSelect1RateF());
        mathRate.put("todayMatchRate", todayMatchRate);
        mathRate.put("thisMonthAllMatchCount", monthAllMatchCount[0]);
        mathRate.put("before1MonthAllMatchCount", monthAllMatchCount[1]);
        mathRate.put("before2MonthAllMatchCount", monthAllMatchCount[2]);
        mathRate.put("ourCoupleMatchCount", couple.getMatchCount());

        return mathRate;
    }

    // 날짜로 매치 질문 조회
    public LinkMatchEntity findMatchByDate(LocalDate today) {
        return matchRepository.findByDisplayDate(today);
    }

    // 통계 - 이번달, 저번달, 저저번달 해당 커플의 매치 성공 횟수
    private int[] monthSuccessMatchCountByCoupleId(Long coupleId){
        
        return matchAnswerRepository.monthSuccessMatchCountByCoupleId(coupleId);
    }

    // 통계 - 성별 별 매치 선택 횟수로 통계 구하기
    private MatchGenderRateDTO matchGenderRate(List<Object[]> result) {
        MatchGenderRateDTO genderRate = new MatchGenderRateDTO();
        for (int i = 0; i < result.size(); i++) {
            int choice = Integer.parseInt(result.get(i)[2].toString());
            if (result.get(i)[1].equals("M") && choice == 0) {
                genderRate.setChoice0ByM(Integer.parseInt(result.get(i)[3].toString()));
            } else if (result.get(i)[1].equals("M") && choice == 1) {
                genderRate.setChoice1ByM(Integer.parseInt(result.get(i)[3].toString()));
            } else if (result.get(i)[1].equals("F") && choice == 0) {
                genderRate.setChoice0ByF(Integer.parseInt(result.get(i)[3].toString()));
            } else if (result.get(i)[1].equals("F") && choice == 1) {
                genderRate.setChoice1ByF(Integer.parseInt(result.get(i)[3].toString()));
            }
        }
        genderRate.setTotalFCount(genderRate.getChoice0ByF() + genderRate.getChoice1ByF());
        genderRate.setTotalMCount(genderRate.getChoice0ByM() + genderRate.getChoice1ByM());
        genderRate.setSelect0RateF((int) Math.round((((double) genderRate.getChoice0ByF() / genderRate.getTotalFCount()) * 100)));
        genderRate.setSelect1RateF((int) Math.round((((double) genderRate.getChoice1ByF() / genderRate.getTotalFCount()) * 100)));
        genderRate.setSelect0RateM((int) Math.round((((double) genderRate.getChoice0ByM() / genderRate.getTotalMCount()) * 100)));
        genderRate.setSelect1RateM((int) Math.round((((double) genderRate.getChoice1ByM() / genderRate.getTotalMCount()) * 100)));

        return genderRate;
    }

    // 통계 - 매칭된 커플 비율(매치 확률)
    private int matchCoupleRate(LinkMatchEntity matchQuestion) {
        Date todayDate = new Date();
        LocalDate today = todayDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        List<Integer> countList = matchAnswerRepository.todayTotalAnswerCountGroupByCoupleId(today);
        int todayAllAnswerCount = countList.size(); // 오늘 매치 답변한 커플 쌍의 수
        int todayMatchCountResult = matchAnswerRepository.todaySuccessMatchCount(); // 오늘 매치에 성공한 커플 쌍의 수

        int todayMatchRate = (int) Math.round(((double) todayMatchCountResult / todayAllAnswerCount) * 100);

        return todayMatchRate;
    }

    // 통계 - 이번달, 저번달, 저저번달 모든 커플의 매치 성공 횟수
    private int[] monthSuccessMatchCount() {
        LocalDate today = LocalDate.now();
        LocalDate startDate = LocalDate.of(today.getYear(), today.getMonth(), 1);          // 시작일
        LocalDate endDate = startDate.plusMonths(1);
        int thisMonthSuccessMatchCount = matchAnswerRepository.monthSuccessMatchCount(startDate.toString(), endDate.toString());
        startDate = startDate.minusMonths(1);
        endDate = startDate.plusMonths(1);
        int before1MonthSuccessMatchCount = matchAnswerRepository.monthSuccessMatchCount(startDate.toString(), endDate.toString());
        startDate = startDate.minusMonths(1);
        endDate = startDate.plusMonths(1);
        int before2MonthSuccessMatchCount = matchAnswerRepository.monthSuccessMatchCount(startDate.toString(), endDate.toString());

        int[] result = {thisMonthSuccessMatchCount, before1MonthSuccessMatchCount, before2MonthSuccessMatchCount};
        return result;
    }
}