package com.ss.heartlinkapi.couple.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MatchAnswer {
    private Long answerId;
    private Long userId;
    private Long coupleId;
    private Long matchId;
    private int choice;
    private Date createdAt;
}
