package com.keduit.shop.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotEmpty;

@Getter
@Setter
@ToString
public class QandADTO {
    @NotEmpty(message = "제목은 필수 항목입니다.")
    private String title;

    @NotEmpty(message = "질문 내용은 필수 항목입니다.")
    private String question;

    private String answer; // 필요하다면 추가
    private String email; // 필요하다면 추가
    private Long itemId; // 필요하다면 추가

    public QandADTO(String title, String question, String answer, String email, Long itemId) {
        this.title = title;
        this.question = question;
        this.answer = answer;
        this.email = email;
        this.itemId = itemId;
    }
}
