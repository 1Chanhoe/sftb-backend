package com.example.demo.entity;
//데이터베이스 테이블과 매핑되는 클래스, 이 클래스는 데이터베이스 테이블의 구조를 정의한다.
public class User {
    private String UserID; // 아이디
    private String StudentID; // 학번
    private String Password; // 비밀번호
    private String UserName; // 이름
    private String Email; // 이메일

    public String getUserID() {
        return UserID;
    }

    public void setUserID(String UserID) {
        this.UserID = UserID;
    }

    public String getStudentID() {
        return StudentID;
    }

    public void setStudentID(String StudentID) {
        this.StudentID = StudentID;
    }

    public String getPassword() {
        return Password;
    }

    public void setPasswd(String Password) {
        this.Password = Password;
    }

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String UserName) {
        this.UserName = UserName;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String Email) {
        this.Email = Email;
    }
}