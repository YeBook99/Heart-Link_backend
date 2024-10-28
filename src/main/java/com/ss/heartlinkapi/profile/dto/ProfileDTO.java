package com.ss.heartlinkapi.profile.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ProfileDTO {
	private String userimg;
	private String pairimg;
	private String bio;
	private String loginId;
	private String nickname;
	private int followerCount;
	private int followingCount;
	private Long coupleUserId;
}
