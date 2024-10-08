package com.ss.heartlinkapi.couple.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class CoupleLinkMissionEntity {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long linkMissionId;
    @Column(name = "couple_id")
    private Long coupleId;
    @Column(name = "linktag_id")
    private Long linkTagId;
    private String missionMonth;
    private int completedCount;
}
