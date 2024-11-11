package com.example.demo.entity;

import java.time.LocalDateTime;
import java.util.List;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
@Data
@Getter
@Setter

public class Comment {
    private Long commentId;   // 댓글 ID (Comment_ID)
    private Long parentSeq;    // 부모 댓글의 ID (Parent_Seq)
    private String content;     // 댓글 내용 (Content)
    private String memberId;    // 작성자 ID (Member_ID)
    private LocalDateTime createdAt; // 작성 시간 (Created_At)
    private LocalDateTime updatedAt; // 수정 시간 (Update_At)
    private Boolean adopt;         // 채택 여부 (Adopt)
    private Integer heart;      // 좋아요 수 (Heart)
    private Long postId;        // 게시글 ID (Post_ID)
    private String userId;
    private List<Comment> replies; // 대댓글 목록
    private String authorTier;
    // Getters and Setters
    public Long getCommentId() {
        return commentId;
    }

    public void setCommentId(Long commentId) {
        this.commentId = commentId;
    }

    public Long getParentSeq() {
        return parentSeq;
    }

    public void setParentSeq(Long parentSeq) {
        this.parentSeq = parentSeq;
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
    
    public String getUserId() { // userId에 대한 getter 추가
        return userId;
    }

    public void setUserId(String userId) { // userId에 대한 setter 추가
        this.userId = userId;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Boolean getAdopt() {
        return adopt;
    }

    public void setAdopt(Boolean adopt) {
        this.adopt = adopt;
    }

    public Integer getHeart() {
        return heart;
    }

    public void setHeart(Integer heart) {
        this.heart = heart;
    }

    public Long getPostId() {
        return postId;
    }

    public void setPostId(Long postId) {
        this.postId = postId;
    }
    
    public List<Comment> getReplies() {
        return replies;
    }

    public void setReplies(List<Comment> replies) {
        this.replies = replies;
    }

	public String getAuthorTier() {
		return authorTier;
	}

	public void setAuthorTier(String authorTier) {
		this.authorTier = authorTier;
	}
}
