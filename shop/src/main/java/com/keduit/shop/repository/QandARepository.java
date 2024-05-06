package com.keduit.shop.repository;

// QandARepository.java

import com.keduit.shop.entity.QandA;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

@Repository
public interface QandARepository extends JpaRepository<QandA, Long> {
    List<QandA> findAllByItemId(Long itemId);
}

