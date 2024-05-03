package com.keduit.shop.dto;

import com.keduit.shop.constant.ItemSellStatus;
import com.keduit.shop.constant.OrderStatus;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class AdminOrderSearchDTO {

    private String searchDateType;

    private String searchBy;
    // email 누가 많이 삿는지
    // itemId 어떤게 많이 팔렷는지

    private String searchQuery = "";

    private OrderStatus orderStatus;
}
