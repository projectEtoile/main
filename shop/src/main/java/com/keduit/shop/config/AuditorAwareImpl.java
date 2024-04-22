package com.keduit.shop.config;

import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

// 스프링 JPA의 Auditing기능을 이용하여
// 엔티티가 저장, 수정 될 때 자동으로
// 등록일, 수정일, 등록자, 수정자등을 입력해 줌.
public class AuditorAwareImpl implements AuditorAware<String> {
    @Override
    public Optional<String> getCurrentAuditor() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userid = "";
        if (authentication != null){
//      현재 로그인한 사용자의 정보를 조회하여 사용자의 이름을 가져옴.
            userid = authentication.getName();
        }
        return Optional.of(userid);
    }
}
