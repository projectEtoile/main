package com.keduit.shop.service;

import com.keduit.shop.constant.Sex;
import com.keduit.shop.dto.AddressDTO;
import com.keduit.shop.dto.AdminMemberSearchDTO;
//import com.keduit.shop.dto.MailDto;
import com.keduit.shop.dto.MemberFormDTO;
import com.keduit.shop.entity.Address;
import com.keduit.shop.entity.Member;
import com.keduit.shop.repository.AddressRepository;
import com.keduit.shop.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import net.minidev.json.JSONObject;
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


import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.util.List;
import java.security.SecureRandom;

@Service
@Transactional
@RequiredArgsConstructor//필요한 부분만 생성자로
public class MemberService implements UserDetailsService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final AddressRepository addressRepository;
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
    public void sendEmail(String to, String newPw) {//to: 이멜 주소 수신 , key: 인증번호or임시비번
        System.out.println("sendemailservice====================================================");
        SimpleMailMessage message = new SimpleMailMessage();

        String subject = "shoppingmall 인증번호 입니다.";//제목
        String text = "인증번호는: " + "12345678";//내용

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

    public MemberFormDTO getMemberInfo(String email) {

        Member member = memberRepository.findByEmail(email);
        MemberFormDTO memberFormDTO = new MemberFormDTO();
        memberFormDTO.setEmail(member.getEmail());
        memberFormDTO.setName(member.getName());
        memberFormDTO.setAge(member.getAge());
        memberFormDTO.setSex(member.getSex());

        return memberFormDTO;
    }

    public void updateInfo(String email, JSONObject memdata) {
        String name = (String) memdata.get("name");
        int age = Integer.parseInt(memdata.get("age").toString());
        String sexValue = (String) memdata.get("sex");
        Sex sex = Sex.valueOf(sexValue);

        Member member = memberRepository.findByEmail(email);

        member.updateMember(name,age,sex);

        memberRepository.save(member);

    }

    public void addAddress(AddressDTO addressDTO, String email) {

        Address address = new Address();
        address.setPostcode(addressDTO.getPostcode());
        address.setRoadAddress(addressDTO.getRoadAddress());
        address.setJibunAddress(addressDTO.getJibunAddress());
        address.setDetailAddress(addressDTO.getDetailAddress());
        address.setExtraAddress(addressDTO.getExtraAddress());

        System.out.println(addressDTO.getSelectAddress());

        if(addressDTO.getSelectAddress().equals(true)){
            List<Address> allAddresses = addressRepository.findAll();
            for (Address address1 : allAddresses) {
                address1.setSelectAddress(false);
            }
            addressRepository.saveAll(allAddresses);

            address.setSelectAddress(true);

        }else{
            address.setSelectAddress(false);
        }


        Member member =  memberRepository.findByEmail(email);

        address.setMember(member);

        addressRepository.save(address);
    }

    public void modifyAddress(AddressDTO addressDTO) {

        Address address = addressRepository.findById(addressDTO.getId()).orElseThrow(EntityNotFoundException::new);

        address.setPostcode(addressDTO.getPostcode());
        address.setRoadAddress(addressDTO.getRoadAddress());
        address.setJibunAddress(addressDTO.getJibunAddress());
        address.setDetailAddress(addressDTO.getDetailAddress());
        address.setExtraAddress(addressDTO.getExtraAddress());

        if (addressDTO.getSelectAddress().equals(true)) {
            System.out.println("트루로직");
            List<Address> allAddresses = addressRepository.findAll();
            for (Address address1 : allAddresses) {
                address1.setSelectAddress(false);
            }
            addressRepository.saveAll(allAddresses);

            address.setSelectAddress(true);

        } else {
            System.out.println("펄스로직");
            address.setSelectAddress(false);
        }

        addressRepository.save(address);
    }


}

