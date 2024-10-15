package com.ss.heartlinkapi.mission.repository;

import com.ss.heartlinkapi.mission.entity.LinkMissionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CoupleMissionRepository extends JpaRepository<LinkMissionEntity, Long> {
}
