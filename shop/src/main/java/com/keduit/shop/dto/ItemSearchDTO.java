package com.keduit.shop.dto;

import com.keduit.shop.constant.ItemSellStatus;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ItemSearchDTO {


    private String level;

    private String searchBy;
    // itemName

    private String saleItems;

    private String searchQuery = "";



}

