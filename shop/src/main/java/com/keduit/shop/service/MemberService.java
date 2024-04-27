package com.keduit.shop.service;

import com.keduit.shop.dto.AdminMemberSearchDTO;
import com.keduit.shop.dto.MailDto;
import com.keduit.shop.dto.MemberFormDTO;
import com.keduit.shop.entity.Member;
import com.keduit.shop.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


import javax.transaction.Transactional;

@Service
@Transactional
@RequiredArgsConstructor//필요한 부분만 생성자로
public class MemberService implements UserDetailsService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    public Member saveMember(Member member) {//새로운 멤버를 등록할 시 중복x
        validateDuplicateMember(member);//중복 여부를 검증하는 과정
        return memberRepository.save(member);//새로운 회원을 실제로 등록하고, 등록된 회원 객체를 반환하는 과정

    }

    private void validateDuplicateMember(Member member) {
        Member findMember = memberRepository.findByEmail(member.getEmail());

        if (findMember != null){
            throw new IllegalStateException("이미 가입된 회원입니다.");

        }
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        Member member =memberRepository.findByEmail(email);
        if(member == null){
            throw new UsernameNotFoundException(email);
        }
        return User.builder()
                .username(member.getEmail())
                .password(member.getPassword())
                .roles(member.getRole().toString())
                .build();
    }
    // 메일 내용을 생성하고 임시 비밀번호로 회원 비밀번호를 변경
    public MailDto createMailAndChangePassword(String memberEmail) {
        return MemberService.createMailAndChangePassword(memberEmail);
    }


    public Page<Member> getAdminMemberPage(AdminMemberSearchDTO adminMemberSearchDTO, Pageable pageable) {
        return memberRepository.getAdminMemberPage(adminMemberSearchDTO, pageable);
    }

    public boolean checkPw(String getPass,String getName) {
        Member member = memberRepository.findByEmail(getName);

        return passwordEncoder.matches(getPass,member.getPassword());

    }
}

