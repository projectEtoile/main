package com.keduit.shop.dto;

import com.keduit.shop.constant.ItemSellStatus;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class AdminItemSearchDTO {

    private String searchDateType;
    // all : 전체
    // 1d : 최근 하루동안 등록된 상품
    // 1w : 최근 1주
    // 1m : 최근 1달
    // 6m : 최근 6개월 동안.

    // 여기서 전체일 경우 어떻게 처리했는지 확인하기.

    private String level1;
    private String level2;
    // 위 기간 별 상품과 마찬가지로 카테고리 별 선택 기능.
    // null 값일 시 전체 카테고리로 검색?
    
    private String searchBy; 
    // itemNm : 상품명으로 조회
    // itemId : 상품 id 번호로 조회

    private String searchQuery = "";
    // 조회할 검색어 저장 변수.
}
