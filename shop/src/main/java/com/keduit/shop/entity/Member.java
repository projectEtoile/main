package com.keduit.shop.entity;

import com.keduit.shop.constant.Role;
import com.keduit.shop.dto.MemberFormDTO;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.*;

@Entity
@Getter
@Setter
@ToString
public class Member extends BaseTimeEntity{
    @Id
    @Column(name="member_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Column(unique = true)
    private String email;

    private String password;

    private String address;

    private boolean social;

    @Enumerated(EnumType.STRING)
    private Role role;



    /*dto주면 entity 리턴하는애임 이게핵심. html폼에서 입력받는 주체가 dto 그걸 entity에 set해주는것 그걸리턴함*/
   public static Member createMember(MemberFormDTO memberFormDTO, PasswordEncoder passwordEncoder){
        Member member = new Member();
        member.setName(memberFormDTO.getName());
        member.setEmail(memberFormDTO.getEmail());

        String password = passwordEncoder.encode(memberFormDTO.getPassword());
        member.setPassword(password);
        member.setRole(Role.ADMIN);
        return member;
    }
}
