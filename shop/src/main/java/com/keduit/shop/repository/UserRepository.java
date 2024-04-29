package com.keduit.shop.repository;

import com.keduit.shop.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByUserid(String userId);
}