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
    private String tier; // 티어
    private int tierExperience; // 티어경험치
    private int userLevel; // User Level
    private int userLevelExperience; // User Level 경험치
    private int token; // 성과금
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

	public int getTierExperience() {
		return tierExperience;
	}
	
	public void setTierExperience(int tierExperience) {
		this.tierExperience = tierExperience;
	}

	public String getTier() {
		return tier;
	}

	public void setTier(String tier) {
		this.tier = tier;
	}

	public int getUserLevel() {
		return userLevel;
	}

	public void setUserLevel(int userLevel) {
		this.userLevel = userLevel;
	}

	public int getUserLevelExperience() {
		return userLevelExperience;
	}

	public void setUserLevelExperience(int userLevelExperience) {
		this.userLevelExperience = userLevelExperience;
	}

	public int getToken() {
		return token;
	}

	public void setToken(int token) {
		this.token = token;
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
