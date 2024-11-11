package com.example.demo.dto;

public class PostRequest {
    private String title;
    private String userName; // 변경된 필드 이름
    private String content;
    private int boardId; // 게시판 ID 추가
    private String userId;
    private int tierExperience; // 티어 경험치 추가

    // Getters and Setters
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getBoardId() {
        return boardId;
    }

    public void setBoardId(int boardId) {
        this.boardId = boardId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public int getTierExperience() {
        return tierExperience;
    }

    public void setTierExperience(int tierExperience) {
        this.tierExperience = tierExperience;
    }
}
