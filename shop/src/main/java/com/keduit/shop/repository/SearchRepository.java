package com.keduit.shop.repository;

import com.keduit.shop.dto.ItemFormDTO;
import com.keduit.shop.dto.SearchDTO;
import com.keduit.shop.entity.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface SearchRepository  {

  /**
   * 리스트 조회
   * @return 리스트
   */
  @Query(value = "SELECT * FROM item WHERE item_sell_status == SELL ORDER BY id DESC LIMIT #{pagination.limitStart}, #{recordSize}", nativeQuery = true)
  List<ItemFormDTO> findAll(SearchDTO params);

  /**
   * 게시글 수 카운팅
   * @return 게시글 수
   */
  @Query(value = "SELECT COUNT(*) FROM item WHERE item_sell_status == SELL", nativeQuery = true)
  int count(SearchDTO params);
}
