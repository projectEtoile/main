package com.keduit.shop.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class AdminMemberSearchDTO {

    private String searchDateType;

    private String searchBy;
    // name
    // id
    // email
    private String searchQuery = "";
}
