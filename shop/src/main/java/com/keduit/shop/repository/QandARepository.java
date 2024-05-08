package com.keduit.shop.repository;

// QandARepository.java

import com.keduit.shop.entity.QandA;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@Repository
public interface QandARepository extends JpaRepository<QandA, Long>, QandARepositoryCustom {
    List<QandA> findAllByItemId(Long itemId);

    List<QandA> findAllByMemberId(Long memberId);

    Page<QandA> findAllByMemberId(Long memberId, Pageable pageable);

    Page<QandA> findAllByItemId(Long itemId, Pageable pageable);
}

