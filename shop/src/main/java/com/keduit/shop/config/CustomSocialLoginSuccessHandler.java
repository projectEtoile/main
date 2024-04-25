package com.keduit.shop.config;

import com.keduit.shop.dto.MemberSecurityDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Log4j2
@RequiredArgsConstructor
public class CustomSocialLoginSuccessHandler implements AuthenticationSuccessHandler {

    private final PasswordEncoder passwordEncoder;


    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        log.info("-------------------onAuthenticationSuccess---------------------");
        log.info(authentication.getPrincipal());
        MemberSecurityDTO memberSecurityDTO = (MemberSecurityDTO) authentication.getPrincipal();
        String encodedPw = memberSecurityDTO.getPassword();

        if (memberSecurityDTO.isSocial() && (memberSecurityDTO.getPassword().equals("1111")
                || passwordEncoder.matches("1111", memberSecurityDTO.getPassword()))){
            log.info("여기에서 비밀번호 변경할 수 있는 로직을 넣으세요.");
            log.info("받아온 새로운 비번을 member에 update");

            response.sendRedirect("/");
        }else{
            response.sendRedirect("/");
        }


    }
}

