package com.example.demo.entity;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class User {
    private String userID;      // 아이디
    private String studentID;   // 학번
    private String password;    // 비밀번호
    private String userName;    // 이름
    private String email;       // 이메일
    private boolean newMember;
    private int experiencePoints; // 경험치 필드 추가


    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getStudentID() {
        return studentID;
    }

    public void setStudentID(String studentID) {
        this.studentID = studentID;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
    
    public boolean getNewMember() { // Getter 메서드 추가
        return newMember;
    }

    public void setNewMember(boolean newMember) { // Setter 메서드 추가
        this.newMember = newMember;
    }
    
    // 경험치 getter
    public int getExperiencePoints() {
        return experiencePoints;
    }

    // 경험치 setter
    public void setExperiencePoints(int experiencePoints) {
        this.experiencePoints = experiencePoints;
    }

}

