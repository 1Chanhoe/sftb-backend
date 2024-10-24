package com.example.demo.dto;

import com.example.demo.entity.User;
public class UserExperienceUpdateRequest {
    private String userId; // UserID
    private int userLevelExperience; // 경험치

    // getters and setters
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

	public int getUserLevelExperience() {
		return userLevelExperience;
	}

	public void setUserLevelExperience(int userLevelExperience) {
		this.userLevelExperience = userLevelExperience;
	}

	public User toEntity() {
        User user = new User();
        user.setUserID(this.userId);
        user.setUserLevelExperience(this.userLevelExperience);
        return user;
    }
}
