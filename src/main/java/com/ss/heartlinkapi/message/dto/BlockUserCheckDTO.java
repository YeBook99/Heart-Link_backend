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
