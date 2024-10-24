package com.example.demo.dto;

public class UserRegistrationDto {
    private String userID;
    private String password;
    private String studentID;
    private String email;

    // 기본 생성자
    public UserRegistrationDto() {}

    // 모든 필드를 받는 생성자
    public UserRegistrationDto(String userID, String password, String studentID, String email) {
        this.userID = userID;
        this.password = password;
        this.studentID = studentID;
        this.email = email;
    }

    // Getters and Setters
    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getStudentID() {
        return studentID;
    }

    public void setStudentID(String studentID) {
        this.studentID = studentID;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
