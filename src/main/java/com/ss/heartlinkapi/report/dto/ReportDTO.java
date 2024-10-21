package com.ss.heartlinkapi.report.dto;

import java.time.LocalDateTime;
import java.util.List;

import com.ss.heartlinkapi.post.dto.PostDTO;
import com.ss.heartlinkapi.post.dto.PostFileDTO;
import com.ss.heartlinkapi.post.entity.Visibility;
import com.ss.heartlinkapi.report.entity.Status;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReportDTO {
	
	private Long reportId;
	private String postId;
	private String commentId;
	private String userId;
	private String reason;
	private Status status;
	private LocalDateTime createAt;

}
