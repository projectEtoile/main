package com.keduit.shop.dto;

import com.keduit.shop.constant.ItemSellStatus;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class MainItemDTO {
  private Long id;    /*상품 코드*/
  private String itemNm;  /*상품이름*/
  private String brandNm; /*상품 브랜드 이름*/
  private String itemDetail;  /*상품 설명*/
  private String imgUrl;  /*이미지 경로*/
  private Integer price;  /*가격*/
  private int discountRate; /*할인율*/
  private ItemSellStatus itemSellStatus;  /*상품 판매 현황*/

  @QueryProjection
  public MainItemDTO(Long id, String itemNm, String brandNm, String itemDetail, String imgUrl, Integer price, int discountRate, ItemSellStatus itemSellStatus) {
    this.id = id;
    this.itemNm = itemNm;
    this.brandNm = brandNm;
    this.itemDetail = itemDetail;
    this.imgUrl = imgUrl;
    this.price = price;
    this.discountRate = (1 - discountRate) * 100;
    this.itemSellStatus = itemSellStatus;
  }
}
