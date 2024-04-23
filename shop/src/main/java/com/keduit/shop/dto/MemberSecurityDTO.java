package com.keduit.shop.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.Map;
@Getter
@Setter
public class MemberSecurityDTO extends User implements OAuth2User {
    private long id;
    private String name;

    private String email;
    private String password;
    private String address;
    private boolean social;
    private Map<String, Object> props; //소셜로그인 정보

    @Override
    public Map<String, Object> getAttributes() {
        return this.getProps();
    }

    public MemberSecurityDTO(String username, String password,
                             boolean social, Collection<? extends GrantedAuthority> authorities) {
        super(username, password, authorities);
        this.email = username;
        this.password = password;
        this.social = social;
    }
}

