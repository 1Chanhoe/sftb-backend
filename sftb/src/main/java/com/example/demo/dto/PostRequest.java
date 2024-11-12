package com.example.demo.dto;

import com.example.demo.entity.Post;

public class PostRequest {
    private String title;
    private String userName; // 변경된 필드 이름
    private String content;
    private int boardId; // 게시판 ID 추가
    private String userId;
    private int tierExperience; // 티어 경험치 추가
    private String filePath; //파일 경로

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

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}
	
	// Post 객체로 변환하는 메서드
    public Post toPost() {
        Post post = new Post();
        post.setTitle(this.title);
        post.setUserName(this.userName);
        post.setContent(this.content);
        post.setBoardId(this.boardId);
        post.setViewCount(0); // 초기 조회수 설정
        post.setUserId(this.userId);
        post.setFilePath(filePath);
        return post;

    }
}
