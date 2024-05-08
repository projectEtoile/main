package com.keduit.shop.service;

import com.keduit.shop.constant.Sex;
import com.keduit.shop.dto.MemberFormDTO;
import com.keduit.shop.entity.Member;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
//@Transactional //테스트에 붙어있을땐, 실행이 끝나면 데이터가 rollback이 돼서 없어짐=>실행되기 전 상태(commitx)
public class MemberServiceTests {

    @Autowired
    MemberService memberService;

    @Autowired
    PasswordEncoder passwordEncoder;

    public Member createMember(int i) {
        MemberFormDTO memberFormDTO = new MemberFormDTO();
        memberFormDTO.setEmail("test" + i + "@Hong.com");
        memberFormDTO.setName("홍길동");
        memberFormDTO.setPassword("12345678");
        memberFormDTO.setAge(i);
        if(i % 3 == 0) {
            memberFormDTO.setSex(Sex.FEMALE);
        } else {
            memberFormDTO.setSex(Sex.MALE);
        }
        return Member.createMember(memberFormDTO, passwordEncoder);
    }

    @Test
    @DisplayName("회원 가입 테스트")
    public void saveMemberTest() {
        for (int i = 0; i < 20; i++) {

            Member member = createMember(i);
            Member saveMember = memberService.saveMember(member);//리퍼지토리 호출해서 넣음

            //비교(각각필드 중복되는지), assert: member.~~와 aveMember~~가 같기를 기대해(충족vs실패)
            assertEquals(member.getEmail(), saveMember.getEmail());
            assertEquals(member.getName(), saveMember.getName());
            assertEquals(member.getPassword(), saveMember.getPassword());
            assertEquals(member.getRole(), saveMember.getRole());
        }
    }

//    @Test
//    @DisplayName("중복 회원 가입 테스트")
//    public void saveDuplcateMemberTest() {
//        /*createMember생성: 내용이 같은 인스턴스(위에 메서드 선언)*/
//        Member member1 = createMember();
//        Member member2 = createMember();
//        memberService.saveMember(member1);
//
//        Throwable e = assertThrows(IllegalAccessException.class,
//                () -> {
//                    memberService.saveMember(member2);
//                });
//        assertEquals("이미 가입된 회원입니다.", e.getMessage());//일치하면 통과
//    }
}
