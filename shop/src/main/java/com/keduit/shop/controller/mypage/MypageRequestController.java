package com.keduit.shop.controller.mypage;

import com.keduit.shop.service.ExchangeReturnRequestService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class MypageRequestController {

    private final ExchangeReturnRequestService exchangeReturnRequestService;

    @PostMapping("/exchange-return-request")
    public ResponseEntity<?> handleExchangeReturnRequest(@RequestBody Map<String, Object> requestData, @AuthenticationPrincipal User user) {
        try {
            List<String> orderIds = (List<String>) requestData.get("orderIds"); // 주문 ID를 문자열로 받음
            String requestType = (String) requestData.get("requestType");
            String reason = (String) requestData.get("reason");

            // 각 주문별로 교환/반품 요청을 생성 및 처리
            for (String orderId : orderIds) {
                // 주문 ID를 Long 타입으로 변환하여 처리
                Long orderIdLong = Long.valueOf(orderId); // 문자열을 Long으로 변환
                exchangeReturnRequestService.submitExchangeReturnRequest(orderIdLong, requestType, reason, user.getUsername());
            }

            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }


}
