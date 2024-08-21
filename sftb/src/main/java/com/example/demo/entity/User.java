package com.example.demo.entity;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
@Data
@Getter
@Setter
public class User {
    private String userID; // 아이디
    private String studentID; // 학번
    private String password; // 비밀번호
    private String userName; // 이름
    private String email; // 이메일
    
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
    
}