package com.keduit.shop.dto;

public class UserPwRequestDto {
    private String username;
    private String email;

    //기본 생성자
    public UserPwRequestDto(){

    }

    //모든 필드를 초기화하는 생성자
    public UserPwRequestDto(String username, String email){
        this.username = username;
        this.email = email;
    }

    //@Getter @Setter
    public String getUserName(){
        return username;
    }
    public String getEmail(){
        return email;
    }

    public void setUsername(String username){
        this.username = username;
    }

    public void setEmail(String email){
        this.email = email;
    }
}
