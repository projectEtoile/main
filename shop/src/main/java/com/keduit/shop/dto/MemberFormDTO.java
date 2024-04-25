package com.keduit.shop.dto;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

/*절대null을 주지않음*/

@Getter
@Setter
public class MemberFormDTO {
    @NotBlank(message = "이름은 필수 입력입니다.")
    private String name;


    @NotEmpty(message = "이메일은 필수 입력입니다.")
    private String email;

    @NotEmpty(message = "비밀번호는 필수 입력입니다.")
    private String password;

}
