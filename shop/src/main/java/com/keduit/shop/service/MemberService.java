package com.keduit.shop.service;

import com.keduit.shop.dto.AdminMemberSearchDTO;
import com.keduit.shop.dto.MailDto;
import com.keduit.shop.entity.Member;
import com.keduit.shop.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
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
@RequiredArgsConstructor//필요한 부분만 생성자로
public class MemberService implements UserDetailsService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
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

    public  MailDto createMailAndChangePassword(String memberEmail) {
        String tempPassword = getTempPassword(); // 임시 비밀번호 생성
        MailDto dto = new MailDto();
        dto.setAddress(memberEmail);
        dto.setTitle("shoppingmall 임시비밀번호 안내 이메일 입니다.");
        dto.setMessage("안녕하세요. shoppingmall 임시비밀번호 안내 관련 이메일 입니다." + " 회원님의 임시 비밀번호는 "
                + tempPassword + " 입니다." + "로그인 후에 비밀번호를 변경을 해주세요");
        memberRepository.updatePassword(passwordEncoder.encode(tempPassword), memberEmail); // 임시 비밀번호로 업데이트
        return dto;
    }
    //임시 비밀번호로 업데이트

    public void updatePassword(String newPassword, String userEmail) {
        memberRepository.updatePassword(passwordEncoder.encode(newPassword), userEmail);
    }

    //랜덤함수로 임시비밀번호 구문 만들기

    public String getTempPassword(){
        char[] charSet = new char[] { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F',
                'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z' };

        SecureRandom secureRandom = new SecureRandom();
//StringBuilder 객체를 사용하여 문자열을 추가
        StringBuilder str = new StringBuilder();
        // 문자 배열 길이의 값을 랜덤으로 10개를 뽑아 구문을 작성함
        int idx = 0;
        for (int i = 0; i < 10; i++) {
            int randomIndex = secureRandom.nextInt(charSet.length);
            str.append(charSet[randomIndex]);
        }
        return str.toString();
    }

    // 메일보내기
    public boolean mailSend(MailDto mailDto) {
        try {
            System.out.println("전송 완료!");
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(mailDto.getAddress());
            message.setSubject(mailDto.getTitle());
            message.setText(mailDto.getMessage());
            message.setFrom("보낸이@naver.com");
            message.setReplyTo("보낸이@naver.com");
            System.out.println("message"+message);
            javaMailSender.send(message);
            System.out.println("이메일이 성공적으로 전송되었습니다.");
            return true; // 이메일 전송 성공 시 true 반환
        } catch (MailException e) {
            System.err.println("이메일 전송 중 오류가 발생했습니다: " + e.getMessage());
            return false; // 이메일 전송 실패 시 false 반환
        }
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

