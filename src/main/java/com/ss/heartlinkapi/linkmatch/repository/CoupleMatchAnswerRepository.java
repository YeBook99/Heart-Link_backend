package com.ss.heartlinkapi.linkmatch.repository;

import com.ss.heartlinkapi.couple.entity.CoupleEntity;
import com.ss.heartlinkapi.linkmatch.dto.MatchCountGenderDTO;
import com.ss.heartlinkapi.linkmatch.entity.LinkMatchAnswerEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CoupleMatchAnswerRepository extends JpaRepository<LinkMatchAnswerEntity, Long> {

    // 매칭 성공 실패 여부 확인(오늘 날짜 기준 매칭 성공 시 1, 실패 시 0, 한 사용자만 등록했을 경우 2 반환)
    @Query("SELECT CASE WHEN COUNT(DISTINCT a.choice) = 1 AND COUNT(a.choice) > 1 THEN 1 " + // 두 사용자가 동일한 선택을 했을 경우
            "WHEN COUNT(DISTINCT a.choice) > 1 THEN 0 ELSE 2 END FROM LinkMatchAnswerEntity a " +
            "WHERE a.coupleId.coupleId = :coupleId AND DATE(a.createdAt) = CURRENT_DATE")
    int checkTodayMatching(@Param("coupleId") Long coupleId);

    // 매치 답변 내역 조회
    List<LinkMatchAnswerEntity> findByCoupleId(CoupleEntity coupleId);

    // 통계 - 성별 별 선택답변 조회 (id값으로 한 질문에 대해 조회)
    @Query(value = "SELECT m.link_match_id, g.gender, c.choice, " +
            "COALESCE(COUNT(l.id), 0) AS count " +
            "FROM (SELECT 'M' AS gender UNION SELECT 'F') AS g " +
            "CROSS JOIN (SELECT 0 AS choice UNION SELECT 1) AS c " +
            "CROSS JOIN match_answer m " +
            "LEFT JOIN users u ON u.id = m.users_id AND u.gender = g.gender " +
            "LEFT JOIN match_answer l " +
            "    ON l.link_match_id = m.link_match_id " +
            "    AND l.choice = c.choice " +
            "    AND l.users_id = u.id " +
            "GROUP BY m.link_match_id, g.gender, c.choice " +
            "having m.link_match_id = :matchId " +
            "ORDER BY m.link_match_id, g.gender, c.choice", nativeQuery = true)
    MatchCountGenderDTO matchCountGenderById(Long matchId);

    // 통계 - 성별 별 선택답변 조회 (전체 질문)

}
