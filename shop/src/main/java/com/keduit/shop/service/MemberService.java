package com.keduit.shop.service;

import com.keduit.shop.dto.AdminMemberSearchDTO;
//import com.keduit.shop.dto.MailDto;
import com.keduit.shop.entity.Member;
import com.keduit.shop.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


import javax.transaction.Transactional;
import java.security.SecureRandom;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberService implements UserDetailsService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    @Autowired
    private final JavaMailSender javaMailSender;



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
        System.out.println("MemberEmail++++++"+email);
        if(member == null){
            System.out.println("MemberEmail++++++sssss"+email);

            throw new UsernameNotFoundException(email);
        }
        return User.builder()
                .username(member.getEmail())
                .password(member.getPassword())
                .roles(member.getRole().toString())
                .build();
    }

    // 메일 내용을 생성하고 임시 비밀번호로 회원 비밀번호를 변경


//이멜 보내는 서비스
    public void sendEmail(String to, String key) {//to: 이멜 주소 수신 , key: 인증번호or임시비번
        System.out.println("sendemailservice====================================================");
        SimpleMailMessage message = new SimpleMailMessage();

        String subject = "shoppingmall 인증번호 입니다.";//제목
        String text = "인증번호는: " + key;//내용

        // 이메일 주소를 RFC 5321에 따라 올바른 형식으로 설정
        String from = "<example@example.com>"; // 이메일 주소 예시

        message.setTo(to);// 수신자 이메일 주소를 설정
        message.setSubject(subject);// 이메일의 제목을 설정
        message.setText(text);// 이메일의 본문 내용을 설정
        // 보내는 이메일 주소 설정
        message.setFrom(from);
        javaMailSender.send(message);
    }

    public String generateKey() {
        System.out.println("generatekey--------------------------------------------");
        SecureRandom random = new SecureRandom();
        StringBuilder randomKey = new StringBuilder(6);

        for (int i = 0; i < 6; i++) {
            // 0 ~ 9까지 랜덤으로 6번 반복해서 randomKeyBuilder에 넣는다.
            randomKey.append(random.nextInt(10));
        }

        return randomKey.toString();
    }



//////////////////////////////////////////////////////////////////////
    public Page<Member> getAdminMemberPage(AdminMemberSearchDTO adminMemberSearchDTO, Pageable pageable) {
        return memberRepository.getAdminMemberPage(adminMemberSearchDTO, pageable);
    }

    public boolean checkPw(String getPass,String getName) {
        Member member = memberRepository.findByEmail(getName);

        return passwordEncoder.matches(getPass,member.getPassword());

    }


}

