package com.example.demo.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "post") // 테이블 이름을 'post'로 지정
public class Post {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Post_ID")
    private Long postId;

    @Column(name = "Title", nullable = false, length = 100)
    private String title;

    @Column(name = "Member_ID", nullable = false, length = 20)
    private String userName;

    @Column(name = "Content", nullable = false, columnDefinition = "TEXT")
    private String content;

    @Column(name = "ViewCount")
    private int viewCount = 0;
    
    @Column(name = "Heart")
    private int heart = 0;

    @Column(name = "Create_At", nullable = false)
    private LocalDateTime createAt;

    @Column(name = "Update_At")
    private LocalDateTime updateAt;

    @Column(name = "Board_ID", nullable = false)
    private Integer boardId;

    // 추가된 부분: 파일 경로 필드
    @Column(name = "file_path")
    private String filePath;

    // Getter and Setter for filePath
 
    @Column(name = "UserID")
    private String userId;


    // Getter and Setter for postId
    public Long getPostId() {
        return postId;
    }

    public void setPostId(Long postId) {
        this.postId = postId;
    }

    // Getter and Setter for title
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    // Getter and Setter for userName
    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    // Getter and Setter for content
    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    // Getter and Setter for viewCount
    public int getViewCount() {
        return viewCount;
    }

    public void setViewCount(int viewCount) {
        this.viewCount = viewCount;
    }
    public int getHeart() {
        return heart;
    }

    public void setHeart(int heart) {
        this.heart = heart;
    }

    // Getter and Setter for createAt
    public LocalDateTime getCreateAt() {
        return createAt;
    }

    public void setCreateAt(LocalDateTime createAt) {
        this.createAt = createAt;
    }

    // Getter and Setter for updateAt
    public LocalDateTime getUpdateAt() {
        return updateAt;
    }

    public void setUpdateAt(LocalDateTime updateAt) {
        this.updateAt = updateAt;
    }

    // Getter and Setter for boardId
    public Integer getBoardId() {
        return boardId;
    }

    public void setBoardId(Integer boardId) {
        this.boardId = boardId;
    }
    
    public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

    // 생성된 시점에 기본적으로 작성 시간을 현재 시간으로 설정
    @PrePersist
    public void prePersist() {
        this.createAt = LocalDateTime.now();
    }

    // 수정 시점에 수정 시간을 현재 시간으로 설정
    @PreUpdate
    public void preUpdate() {
        this.updateAt = LocalDateTime.now();
    }
    
    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }
}
