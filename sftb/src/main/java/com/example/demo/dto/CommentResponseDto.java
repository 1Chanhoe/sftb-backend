package com.example.demo.dto;

import java.time.LocalDateTime;
import com.example.demo.entity.Comment;

public class CommentResponseDto {
    private Long commentId;
    private String content;
    private String memberId;
    private String postAuthorId; // 게시글 작성자 ID 추가
    private LocalDateTime createdAt; // 댓글 작성 시간

    public CommentResponseDto(Comment comment, String postAuthorId) {
        this.setCommentId(comment.getCommentId());
        this.setContent(comment.getContent());
        this.setMemberId(comment.getMemberId());
        this.setPostAuthorId(postAuthorId);
        this.setCreatedAt(comment.getCreatedAt());
    }

	public Long getCommentId() {
		return commentId;
	}

	public void setCommentId(Long commentId) {
		this.commentId = commentId;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getMemberId() {
		return memberId;
	}

	public void setMemberId(String memberId) {
		this.memberId = memberId;
	}

	public String getPostAuthorId() {
		return postAuthorId;
	}

	public void setPostAuthorId(String postAuthorId) {
		this.postAuthorId = postAuthorId;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}
}