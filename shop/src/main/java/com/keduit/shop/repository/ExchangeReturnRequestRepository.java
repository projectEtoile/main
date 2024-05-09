package com.keduit.shop.repository;

import com.keduit.shop.entity.ExchangeReturnRequest;
import com.keduit.shop.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ExchangeReturnRequestRepository extends JpaRepository<ExchangeReturnRequest, Long> {

    ExchangeReturnRequest findByOrder(Order order);
}
