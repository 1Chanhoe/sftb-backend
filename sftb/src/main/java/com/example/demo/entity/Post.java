package com.example.demo.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "CerInfo_Post") // 테이블 이름 지정
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 기본 키 자동 생성
    @Column(name = "Cre_seq") // DB 테이블의 컬럼명과 매핑
    private Long creSeq; // 게시물 고유 번호

    @Column(name = "Title", nullable = false, length = 100) // 제목 필드
    private String title; // 게시물 제목

    @Column(name = "Member_ID", nullable = false, length = 10) // 작성자 ID
    private String userName; // 작성자의 회원 ID (로그인된 사용자)

    @Column(name = "Content", nullable = false, columnDefinition = "TEXT") // 내용 필드
    private String content; // 게시물 내용

    @Column(name = "ViewCount") // 조회수 필드 (초기값 0)
    private int viewCount = 0; // 조회수

    @Column(name = "Create_At", nullable = false) // 작성 시간 필드
    private LocalDateTime createAt; // 작성 시간

    @Column(name = "Update_At") // 수정 시간 필드 (NULL 가능)
    private LocalDateTime updateAt; // 수정 시간

    // Getter and Setter for creSeq
    public Long getCreSeq() {
        return creSeq;
    }

    public void setCreSeq(Long creSeq) {
        this.creSeq = creSeq;
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
