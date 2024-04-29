package com.keduit.shop.controller;

import com.keduit.shop.dto.*;
import com.keduit.shop.entity.Item;
import com.keduit.shop.entity.Member;
import com.keduit.shop.repository.MemberRepository;
import com.keduit.shop.repository.MemberRepositoryCustom;
import com.keduit.shop.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static com.keduit.shop.entity.QMember.member;

@Controller
@RequestMapping("/members")
@RequiredArgsConstructor
public class MemberController {
    //memberservice생성자 주입
    private final MemberService memberService;
    private final PasswordEncoder passwordEncoder;


    @GetMapping("/new")
    public String memberForm(Model model){
        model.addAttribute("memberFormDTO",new MemberFormDTO());
        System.out.println("나오냐?????");
        return "member/memberForm";
    }

    @PostMapping("/new")
    //BindingResult bindingResult: 에러발생시 MemberFormDTO(절대null을 주지않음) memberFormDTO결과리턴 에러가있음 다시 돌아가(유효성 체크)
    public String newMember(@Valid MemberFormDTO memberFormDTO,
                            BindingResult bindingResult, Model model) {
        System.out.println("나오니?1111");
        /*memberFormDTO의 유효성 체크결과확인-> 에러이면 다시 입력 폼을 리턴*/
        /*유효성검사*/
        if (bindingResult.hasErrors()) {
            System.out.println("나오니?22");
            return "member/memberForm";
        }
        System.out.println("나오니?33");
        /*회원가입시 메일(findBy)이 중복된 경우 에러를 처리 */
        /*중복체크*/
        try {
            Member member = Member.createMember(memberFormDTO, passwordEncoder);
            memberService.saveMember(member);//새로 생성된 회원 객체를 데이터베이스에 저장합니다. 이를 통해 실제 회원가입이 완료
        }catch (IllegalStateException e){//중복된 이메일 주소가 이미 존재하는 경우 등에는 예외 처리
            model.addAttribute("errorMessage", e.getMessage());
            return "member/memberForm";
        }
        return "redirect:/";//회원가입이 성공하면 홈페이지로 리다이렉트
    }

    /*로그인*/
    @GetMapping("/login")
    public String loginMember(){
        return "member/login";//페이지 리턴
    }


    @GetMapping("/login/error")
    public String loginError(Model model){


        System.out.println("로그인 실패@@@@@@@@@@@@@@@@@@@@@@");
        model.addAttribute("loginErrorMsg", "아이디 혹은 비밀번호를 확인해주세요");
        return "member/login";
    }
/*@PostMapping("/findpw")
    public String findPw(@RequestBody UserPwRequestDto userPwRequestDto) {
       UserService.userCheck(userPwRequestDto);
        return "/member/pwsuccess";

}*/

    /*비밀번호 찾기*/
//    @GetMapping("/findLoginPw")
//    public String findLoginPw(){
//        return "member/findLoginPw";
//    }

    @GetMapping("/pw")
        public String findpw(){
        return "member/pwInquiry";
    }
    @GetMapping("/pwRestSuccess")
    public String showPwRestSuccessPage(){
        return "pwRestSuccess";
    }


    // 비밀번호 재설정 페이지로 이동

    // 비밀번호 재설정 폼에서 새로운 비밀번호를 입력받아 처리하는 메서드
    /*@PostMapping("/pw")
    public String resetPassword(@RequestParam("memberEmail") String memberEmail, Model model) {
        // 이메일로 임시 비밀번호 생성 및 전송
        MailDto dto = memberService.createMailAndChangePassword(memberEmail);
        memberService.mailSend(dto);

        // 비밀번호 재설정 성공 페이지로 이동
        return "member/pwRestSuccess";
    }*/



    /*@PostMapping("/sendEmail")
    public String sendEmail(@RequestParam("memberEmail") String memberEmail) {
        // 메일 전송을 위한 MailDto 생성
        MailDto dto = memberService.createMailAndChangePassword(memberEmail);
        // 생성된 메일을 전송
        memberService.mailSend(dto);
        return "member/login";
    }*/
    @PostMapping("/findPassword")

    public ResponseEntity<Void> findPassword(@RequestParam("email") String email) {
        System.out.println("findpassword====================");
        try {
            // 임시 비밀번호 생성
            String temporaryPassword = memberService.generateTemporaryPassword();

            // 이메일 발송
            memberService.sendTemporaryPasswordByEmail(email, temporaryPassword);

            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }


//    ------------------- 마이페이지 컨트롤러 ----------------------

    @GetMapping("/checkPw")
    public String checkPw(){
        return "mypage/checkPw";
    }

}