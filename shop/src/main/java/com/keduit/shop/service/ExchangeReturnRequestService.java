package com.keduit.shop.service;

import com.keduit.shop.constant.OrderStatus;
import com.keduit.shop.entity.ExchangeReturnRequest;
import com.keduit.shop.entity.Member;
import com.keduit.shop.entity.Order;
import com.keduit.shop.repository.ExchangeReturnRequestRepository;
import com.keduit.shop.repository.MemberRepository;
import com.keduit.shop.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ExchangeReturnRequestService {

    private final ExchangeReturnRequestRepository exchangeReturnRequestRepository;
    private final MemberRepository memberRepository;
    private final OrderRepository orderRepository;

    @Transactional
    public void submitExchangeReturnRequest(Long orderId, String requestType, String reason, String memberEmail) {
        Member member = memberRepository.findByEmail(memberEmail);
        Order order = orderRepository.findById(orderId).orElseThrow(() -> new IllegalArgumentException("Order not found"));

        // ExchangeReturnRequest 생성
        ExchangeReturnRequest request = new ExchangeReturnRequest();
        request.setOrder(order);
        request.setRequestType(requestType); // 교환 또는 반품
        request.setReason(reason); // 사유
        request.setRequestDate(LocalDateTime.now()); // 신청 날짜
        request.setMember(member); // 신청자

        // 테이블에 저장
        exchangeReturnRequestRepository.save(request);

        // 주문의 상태를 변경
        order.setOrderStatus(OrderStatus.RETURN_REQUEST);
    }
}
