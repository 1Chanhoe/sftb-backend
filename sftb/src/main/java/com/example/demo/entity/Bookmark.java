package com.example.demo.entity;
import java.util.List;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
@Data
@Getter
@Setter
public class Bookmark {
	private Long bookmarkId;
	private Long postId;        // 게시글 ID (Post_ID)
	private String userId;
	
	public Long getBookmarkId() {
		return bookmarkId;
	}
	
	public void setBookmarkId(Long bookmarkId) {
		this.bookmarkId = bookmarkId;
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
