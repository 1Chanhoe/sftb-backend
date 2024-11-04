package com.example.demo.dto;

import java.time.LocalDateTime; // LocalDateTime을 사용하기 위해 추가

public class PostDto {
    private String title;
    private String content;
    private Long postId;
    private LocalDateTime updateAt; // 수정된 시간 추가
    private String filePath; // 사진 파일 경로 추가

    // Getters and Setters
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
    
    public Long getPostId() {
        return postId;
    }

    public void setPostId(Long postId) {
        this.postId = postId;
    }
    
    public LocalDateTime getUpdateAt() { 
        return updateAt;
    }

    public void setUpdateAt(LocalDateTime updateAt) { 
        this.updateAt = updateAt;
    }
    
    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) { 
        this.filePath = filePath;
    }
}
