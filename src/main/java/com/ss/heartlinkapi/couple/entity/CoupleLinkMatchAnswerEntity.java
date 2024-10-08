package com.ss.heartlinkapi.couple.entity;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "match_answer")
public class CoupleLinkMatchAnswerEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long LinkMatchAnswerId;

    // user 외래키 달기
    private Long userId;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "coupleId", nullable = false)
    private CoupleEntity coupleId;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "linkMatchId")
    private CoupleLinkMatchEntity matchId;

    private int choice;
    private Date createdAt;

}
