package com.ss.heartlinkapi.couple.entity;

import lombok.Data;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Date;

@Entity
@Data
public class CoupleEntity {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long coupleId;
    // 외래키 설정 필요
    private Long user1;
    private Long user2;

    @Column(name = "created_at")
    private Timestamp createdAt;
    @Column(name = "anniversary_date")
    private Date anniversaryDate;
    @Column(name = "breakup_date")
    private Date breakupDate;
}
