package com.keduit.shop.controller;

import org.springframework.web.bind.annotation.GetMapping;

public class UserMemberController {
    @GetMapping("/pwInquiry")
    public String findLoginPw(){
        return "member/pwInquiry";
    }

/*    @GetMapping("/doFindLoginPw")
    public String doFindLoginPw(String email){
        if(Utilit.empty(email)){
            return Utility.jsHistoryBack("이메일을 입력해주세요");
        }
    }*/
}
