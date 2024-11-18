package com.example.demo.dto;

public class PurchaseRequest {

    private String userId;  // 사용자 ID
    private int price;      // 상품 가격

    // Getter, Setter
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }
}