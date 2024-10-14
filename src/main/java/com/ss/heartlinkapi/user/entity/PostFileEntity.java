package com.ss.heartlinkapi.user.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.ss.heartlinkapi.post.entity.PostEntity;

import lombok.Data;

@Entity
@Data
@Table(name = "post_file")
public class PostFileEntity {
	@Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long postFileId;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "post_id", nullable = false)
	private PostEntity postId;
	
	@Column(name = "file_url", nullable = false)
	private String fileUrl;
	
	@Column(name = "file_type", length = 50)
	private String file_type;
	
	@Column(unique = true, name = "sort_order")
	private int sortOrder;

}
