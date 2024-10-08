package com.ss.heartlinkapi.couple.entity;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "match_answer")
public class CoupleLinkMatchAnswerEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long LinkMatchAnswerId;

    @Column(name = "user_id")
    private Long userId;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "couple_id")
    private CoupleEntity coupleId;

    private Long matchId;
    private int choice;
    private Date createdAt;

}
