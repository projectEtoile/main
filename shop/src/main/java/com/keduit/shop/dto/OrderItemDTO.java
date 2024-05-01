package com.keduit.shop.dto;

import com.keduit.shop.entity.OrderItem;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class OrderItemDTO {

    /*생성자만들기*/
    public OrderItemDTO(OrderItem orderItem, String imgUrl, String size){

        this.itemNm = orderItem.getItem().getItemNm();
        this.count = orderItem.getCount();
        this.orderPrice = orderItem.getOrderPrice();
        this.imgUrl = imgUrl;
        this.size = size;
    }

    /*필드*/
    private String itemNm;
    private int count;
    private int orderPrice;
    private String imgUrl;
    private String size;
}
