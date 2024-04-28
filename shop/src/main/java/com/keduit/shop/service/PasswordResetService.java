package com.keduit.shop.service;

import com.keduit.shop.entity.Member;
import com.keduit.shop.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class PasswordResetService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public PasswordResetService(MemberRepository memberRepository, PasswordEncoder passwordEncoder) {
        this.memberRepository = memberRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public boolean resetPassword(String email, String newPassword) {
        // 이메일로 회원 정보 조회

        Member member = memberRepository.findByEmail(email);
        if (member == null) {
            // 해당 이메일 주소에 대한 회원이 없으면 false 반환
            return false;
        }

        // 새로운 비밀번호를 인코딩하여 설정
        String encodedPassword = passwordEncoder.encode(newPassword);
        member.setPassword(encodedPassword);

        // 회원 정보 저장
        memberRepository.save(member);

        return true;
    }
}
