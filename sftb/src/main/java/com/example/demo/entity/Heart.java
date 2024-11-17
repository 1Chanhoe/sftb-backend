package com.example.demo.entity;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class Heart {
    private Long heartid;
    private Long postId;
    private String userId;
    
	public Long getHeartid() {
		return heartid;
	}
	
	public void setHeartid(Long heartid) {
		this.heartid = heartid;
	}

	public Long getPostId() {
		return postId;
	}

	public void setPostId(Long postId) {
		this.postId = postId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

}
