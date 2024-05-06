package com.keduit.shop.repository;

import com.keduit.shop.entity.SearchRank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SearchRankRepository extends JpaRepository<SearchRank, Long> {

    // 중복 없이 키워드 이름 1개 출력
    @Query(value = "SELECT s.keyword FROM SearchRank s GROUP BY s.keyword HAVING COUNT(s.keyword) = 1")
    List<String> findDistinctKeywords();
}