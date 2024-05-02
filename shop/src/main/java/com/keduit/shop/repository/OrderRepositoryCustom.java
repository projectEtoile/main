package com.keduit.shop.repository;

import com.keduit.shop.dto.AdminOrderSearchDTO;
import com.keduit.shop.entity.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface OrderRepositoryCustom {
    Page<Order> getAdminOrderPage(AdminOrderSearchDTO adminOrderSearchDTO, Pageable pageable);

}
