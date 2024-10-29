package com.ss.heartlinkapi.post.dto;

import java.util.List;

import com.ss.heartlinkapi.post.entity.FileType;
import com.ss.heartlinkapi.post.entity.Visibility;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PostUpdateDTO {

	private String content;						// 수정할 게시글 내용
	private Visibility visibility;				// 수정할 공개 범위
	private List<String> existingFileUrls;		// 기존 파일 URL 목록(유지할 파일)
	private List<String> newFileUrls;			// 새로 추가할 파일 URL 목록
	private List<String> filesToDelete;
}
