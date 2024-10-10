package com.ss.heartlinkapi.couple.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MatchAnswer {
    private Long userId;
    private Long questionId;
    private int selectedOption;
}
