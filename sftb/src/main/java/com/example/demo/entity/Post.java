package com.example.demo.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "post") // 테이블 이름을 'post'로 지정
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 기본 키 자동 생성
    @Column(name = "Post_ID") // DB 테이블의 컬럼명과 매핑
    private Long postId; // 게시물 고유 번호

    @Column(name = "Title", nullable = false, length = 100) // 제목 필드
    private String title; // 게시물 제목

    @Column(name = "Member_ID", nullable = false, length = 20) // 작성자 ID
    private String userName; // 작성자의 회원 ID (로그인된 사용자)

    @Column(name = "Content", nullable = false, columnDefinition = "TEXT") // 내용 필드
    private String content; // 게시물 내용

    @Column(name = "ViewCount") // 조회수 필드 (초기값 0)
    private int viewCount = 0; // 조회수
    
    @Column(name = "Heart")
    private int heart = 0; // 좋아요 수 기본값

    @Column(name = "Create_At", nullable = false) // 작성 시간 필드
    private LocalDateTime createAt; // 작성 시간

    @Column(name = "Update_At") // 수정 시간 필드 (NULL 가능)
    private LocalDateTime updateAt; // 수정 시간

    @Column(name = "Board_ID", nullable = false) // 게시판 ID 필드 (NOT NULL)
    private Integer boardId; // 게시판 ID (외래키)
    
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
}
