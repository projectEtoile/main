package com.keduit.shop.repository;

import com.keduit.shop.entity.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartRepository extends JpaRepository<Cart, Long> {
    /*현재 로그인한 회원의 Cart 엔티티를 읽어옴*/
    Cart findByMemberId(Long memberId);
}
