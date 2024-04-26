package com.keduit.shop.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class CartDetailDTO {
    private Long cartItemId;
    private String itemNm;
    private int price;
    private int count;
    private String imgUrl;
    private String size;


    public CartDetailDTO(Long cartItemId, String itemNm, int price, int count, String imgUrl, String size) {
        this.cartItemId = cartItemId;
        this.itemNm = itemNm;
        this.price = price;
        this.count = count;
        this.imgUrl = imgUrl;
        this.size = size; // 필드 초기화
    }
}
