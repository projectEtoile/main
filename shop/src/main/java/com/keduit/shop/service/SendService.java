package com.keduit.shop.service;

import com.keduit.shop.dto.UserPwRequestDto;
import org.springframework.mail.javamail.JavaMailSender;

public class SendService {
    private JavaMailSender mailSender;
    private static final String FROM_ADDRESS = "아이디@gamil.com";

    public MailDto createMailAndChargePassword(UserPwRequestDto requestDto){
        String str = getTempPaaword();
        MailDto dto = new MailDto();
        dto.setAddress(requestDto.getUserEmail());
        dto.setTitle(requestDto.getUserName()+"님의 임시번호 안내 이메일 입니다.");
        dto.setMessage("안녕하세요. 임시번호 안내 관련 메일 입니다." + "[" + requestDto.getUserName()+"]"+"님의 "+str+"입니다.");
        updatePassword(str,requestDto);
        return dto;
    }

    public void updatePassword(String str, UserPwRequestDto userPwRequestDto){
        Long id = userRepository.findByUserId()
    }
}
