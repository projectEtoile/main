package com.keduit.shop.service;

import com.keduit.shop.dto.UserPwRequestDto;
import org.springframework.security.core.userdetails.User;

public class UserService {
    public void userCheck(UserPwRequestDto userPwRequestDto){
        User user = userRepository.findByUserId(userPwRequestDto.getUserName()).get();
        if (user == null && !user.getUsername().equals(userPwRequestDto.getUserName())){
            throw new CustomException(ErrorCode.ID_NOT_FOUND_ERROR);
        }else{
            sendEmail(userPwRequestDto);
        }
    }

    public void sendEmail(UserPwRequestDto userPwRequestDto){
        MailDto dto = sendEmailService.createMailAndChargePassword(userPwRequestDto);
        sendEmailService.mailSend(dto);
    }
}
