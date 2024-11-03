package com.ss.heartlinkapi.message.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class BlockUserCheckDTO {
    private Long userId;
    private Long blockUserId;
}

//  block유저인지 체크할 때 쓰이는 DTO
