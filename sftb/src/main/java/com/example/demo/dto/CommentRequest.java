package com.example.demo.dto;

import com.example.demo.entity.Comment;

public class CommentRequest {
    private Long postId;
    private String content;
    private String memberId; // 작성자 ID 추가 (필요한 경우)
    private Long parentId;
    private String userId; // 사용자 ID 추가
    // Getters and Setters
    public Long getPostId() {
        return postId;
    }

    public void setPostId(Long postId) {
        this.postId = postId;
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
    
    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }
    
    public String getUserId() { // userId에 대한 getter 추가
        return userId;
    }

    public void setUserId(String userId) { // userId에 대한 setter 추가
        this.userId = userId;
    }
    // Comment 객체로 변환하는 메서드
    public Comment toComment() {
        Comment comment = new Comment();
        comment.setPostId(this.postId);
        comment.setContent(this.content);
        comment.setMemberId(this.memberId); // 필요한 경우 설정
        comment.setParentSeq(this.parentId);
        comment.setUserId(this.userId);
        return comment;
    }
}
