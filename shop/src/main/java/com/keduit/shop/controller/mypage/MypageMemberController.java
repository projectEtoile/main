package com.keduit.shop.controller.mypage;

import com.keduit.shop.dto.MemberFormDTO;
import com.keduit.shop.entity.Member;
import com.keduit.shop.repository.MemberRepository;
import com.keduit.shop.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;
import java.security.Principal;

@Controller
@RequestMapping("/mypage")
@RequiredArgsConstructor
public class MypageMemberController {
    //memberservice생성자 주입
    private final MemberService memberService;
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;


    @GetMapping("/checkPw")
    public String checkPw(MemberFormDTO memberFormDTO, Model model){

        // 비밀번호만 받아보자. 비밀번호만을 위해 DTO를 더 만들기 보단 기존 DTO 활용 시도
        model.addAttribute("MemberFormDTO",memberFormDTO);

        return "mypage/checkPw";
    }

    @PostMapping("/checkPw")
    public String checkPw(Principal principal,MemberFormDTO memberFormDTO,Model model){



        boolean result = memberService.checkPw(memberFormDTO.getPassword(),principal.getName());

        if (result){
            return "mypage/myInfo";
        }else
//            model.addAttribute("errorMessage", "비밀번호가 틀렸습니다.");
        return "mypage/checkPw";
    }

    @GetMapping("/myOrder")
    public String myOrder(){
        return "mypage/myOrder";
    }

}