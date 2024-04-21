package com.keduit.shop.dto;

import javax.validation.constraints.NotBlank;

public class asdDTO {
    private Long id;

    @NotBlank(message = "상품명은 필수 입력입니다.")
    private String itemNm;
}
