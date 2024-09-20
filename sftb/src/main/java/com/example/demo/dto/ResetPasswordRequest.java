package com.example.demo.dto;

public class ResetPasswordRequest {
    private String userID;
    private String newPassword;

    // 기본 생성자
    public ResetPasswordRequest() {}

    // 매개변수 생성자
    public ResetPasswordRequest(String userID, String newPassword) {
        this.userID = userID;
        this.newPassword = newPassword;
    }

    // Getter 및 Setter
    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    @Override
    public String toString() {
        return "ResetPasswordRequest{" +
                "userID='" + userID + '\'' +
                ", newPassword='" + newPassword + '\'' +
                '}';
    }
}
