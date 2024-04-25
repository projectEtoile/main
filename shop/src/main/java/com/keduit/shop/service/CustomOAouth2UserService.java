package com.keduit.shop.service;
import com.keduit.shop.constant.Role;
import com.keduit.shop.dto.MemberSecurityDTO;
import com.keduit.shop.entity.Member;
import com.keduit.shop.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;

import static com.keduit.shop.entity.QMember.member;

@Service
@RequiredArgsConstructor
@Log4j2
public class CustomOAouth2UserService extends DefaultOAuth2UserService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        log.info("-------------CustomOAuth2UserService----------------");
        log.info(userRequest);

        log.info("--------------oauth2 user----------");
        ClientRegistration clientRegistration = userRequest.getClientRegistration();
        String clientName = clientRegistration.getClientName();
        log.info("****NAME: "+clientName);

        OAuth2User oAuth2User = super.loadUser(userRequest);
        Map<String, Object> paramMap = oAuth2User.getAttributes();

        paramMap.forEach((k,v)->{
            log.info("-----------paramMap-------------");
            log.info(k + ":"+v);
        });


        String email = null;
        switch (clientName){
            case "kakao":
                email = getKakaoEmail(paramMap);
                break;
        }
        log.info("=====");
        log.info(email);
        log.info("=====");

        return generateDTO(email,paramMap);
    }

    private MemberSecurityDTO generateDTO(String email, Map<String, Object> paramMap) {
        Member result = memberRepository.findByEmail(email);

        if (result == null) {
            //회원 추가
            Member member = new Member();
            member.setName("KAKAO");
            member.setEmail(email);
            member.setPassword(passwordEncoder.encode("1111"));
            member.setSocial(true);
            member.setRole(Role.USER);
            memberRepository.save(member);

            //MemberSecurityDTO구성 및 반환 추가
            MemberSecurityDTO memberSecurityDTO = new MemberSecurityDTO(
                    email, "1111", true,
                    Arrays.asList(new SimpleGrantedAuthority("ROLE_USER"))
            );
            memberSecurityDTO.setProps(paramMap);

            return memberSecurityDTO;
        }else {
            MemberSecurityDTO memberSecurityDTO = new MemberSecurityDTO(
                    result.getEmail(),
                    result.getPassword(),
                    result.isSocial(),
                    Arrays.asList(new SimpleGrantedAuthority("ROLE_USER"))

            );

            return memberSecurityDTO;
        }
    }

    private String getKakaoEmail(Map<String, Object> paramMap) {
        log.info("----------------kakao --------------------");
        Object value = paramMap.get("kakao_account");
        log.info(value);

        LinkedHashMap accountMap = (LinkedHashMap) value;
        String email = (String)accountMap.get("email");
        log.info("---email : " + email);
        return email;
    }


}

