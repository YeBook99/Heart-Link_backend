package com.ss.heartlinkapi.couple.entity;

import javax.persistence.*;
import java.util.Date;

@Table(name = "link_match")
@Entity
public class CoupleLinkMatchEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long linkMatchId;
    private String match1;
    private String match2;
    @Column(name = "display_date")
    private Date displayDate;

}
