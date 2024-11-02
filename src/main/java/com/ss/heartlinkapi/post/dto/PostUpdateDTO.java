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
}
