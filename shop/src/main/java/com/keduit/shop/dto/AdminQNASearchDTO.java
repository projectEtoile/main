package com.keduit.shop.dto;

import com.keduit.shop.constant.OrderStatus;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class AdminQNASearchDTO {

    private String searchDateType;

    private String searchBy;
    // email
    // memberId
    // itemId

    private String searchQuery = "";

    private String answerStatus;
    // unfinished
    // finished
}
