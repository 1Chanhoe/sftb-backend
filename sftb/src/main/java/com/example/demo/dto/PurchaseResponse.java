package com.example.demo.dto;

public class PurchaseResponse {

    private boolean success;  // 구매 성공 여부
    private String message;   // 응답 메시지

    // 생성자
    public PurchaseResponse(boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    // Getter, Setter
    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}