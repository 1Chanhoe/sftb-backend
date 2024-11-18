package com.example.demo.controller;

import com.example.demo.service.StoreService;
import com.example.demo.dto.PurchaseRequest;
import com.example.demo.dto.PurchaseResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class StoreController {

    @Autowired
    private StoreService storeService;

    // 상품 구매 API
    @PostMapping("/api/store/purchase")
    public PurchaseResponse purchaseItem(@RequestBody PurchaseRequest request) {
    	System.out.println("상품 구매 요청 받음: " );  // 로그로 확인
        boolean success = storeService.processPurchase(request.getUserId(), request.getPrice());

        // 구매 성공/실패 응답 반환
        if (success) {
            return new PurchaseResponse(true, "구매 성공!");
        } else {
            return new PurchaseResponse(false, "토큰이 부족합니다.");
        }
    }
}
