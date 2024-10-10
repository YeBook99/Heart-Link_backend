package com.ss.heartlinkapi.couple.repository;

import com.ss.heartlinkapi.couple.dto.MatchAnswer;
import com.ss.heartlinkapi.linkmatch.entity.LinkMatchAnswerEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CoupleMatchAnswerRepository extends JpaRepository<LinkMatchAnswerEntity, Long> {


}
