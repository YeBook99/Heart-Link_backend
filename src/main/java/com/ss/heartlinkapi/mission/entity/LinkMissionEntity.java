package com.ss.heartlinkapi.mission.entity;

import com.ss.heartlinkapi.couple.entity.CoupleEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "link_mission")
public class LinkMissionEntity {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long linkMissionId;
    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "couple_id")
    private CoupleEntity coupleId;

    // 나중에 연결하기
    @Column(name = "linktag_id")
    private Long linkTagId;
    private String missionMonth;
    private int completedCount;
}
