package com.keduit.shop.dto;

import com.keduit.shop.constant.ItemSellStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
@AllArgsConstructor
@Data
public class CategoryDTO {
  private Long id;

  private String itemNm;

  private String brandNm;

  private String level1; // 폼 js에서 자동 설정

  private String level2;

  @Enumerated(EnumType.STRING)
  private ItemSellStatus itemSellStatus;    // 상품 판매 현황

  private String imgUrl;

  private Integer price;

  private String material;

  private String itemText; // 상품 상세 설명 필수x

}
