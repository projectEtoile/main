package com.keduit.shop.controller.mypage;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.keduit.shop.constant.Sex;
import com.keduit.shop.dto.AddressDTO;
import com.keduit.shop.dto.MemberFormDTO;
import com.keduit.shop.entity.Address;
import com.keduit.shop.entity.Member;
import com.keduit.shop.repository.AddressRepository;
import com.keduit.shop.repository.MemberRepository;
import com.keduit.shop.service.MemberService;
import lombok.RequiredArgsConstructor;
import net.minidev.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;
import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/mypage")
@RequiredArgsConstructor
public class MypageMemberController {
    //memberservice생성자 주입
    private final MemberService memberService;
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final AddressRepository addressRepository;


    @GetMapping("/checkPw")
    public String checkPw(MemberFormDTO memberFormDTO, Model model) {

        // 비밀번호만 받아보자. 비밀번호만을 위해 DTO를 더 만들기 보단 기존 DTO 활용 시도
        model.addAttribute("MemberFormDTO", memberFormDTO);

        return "mypage/checkPw";
    }


    @PostMapping("/checkPw")
    public String checkPw(Principal principal, MemberFormDTO memberFormDTO, Model model) {

        boolean result = memberService.checkPw(memberFormDTO.getPassword(), principal.getName());

        if (result) {
            return "redirect:/mypage/myInfo";
        } else
            model.addAttribute("errorMessage", "비밀번호가 틀렸습니다.");
        return "mypage/checkPw";
    }

    @GetMapping("/myInfo")
    public String updateInfo(Model model, Principal principal) {

        MemberFormDTO memberFormDTO = memberService.getMemberInfo(principal.getName());

        model.addAttribute("memberFormDTO", memberFormDTO);
        return "mypage/myInfo";
    }


    @GetMapping("/myOrder")
    public String myOrder() {
        return "mypage/myOrder";
    }


    @PatchMapping("/changePw")
    public @ResponseBody ResponseEntity changePw(@RequestBody @Valid JSONObject newPw1,
                                                 // data는 JSONObject 으로 받는게 벨류를 뽑기 좋다.
                                                 // @RequestBody 는 ajax의 data 를 json에서 자바 형식으로
                                                 // 변환해주는 어노테이션,
                                                 // @Valid 는 여기선 String 형식이 받는지 유효성 검사.
                                                 // 이를 BindingResult 를 통해 반환한다.
                                                 BindingResult bindingResult, Principal principal) {

        if (bindingResult.hasErrors()) {
            StringBuilder sb = new StringBuilder();
            List<FieldError> fieldErrors = bindingResult.getFieldErrors();
            for (FieldError fieldError : fieldErrors) {
                sb.append(fieldError.getDefaultMessage());
            }
            return new ResponseEntity<String>(sb.toString(), HttpStatus.BAD_REQUEST); /*responsbody사용하는이유임*/
        } // 예외처리 작업은 서비스에서 하는 것이 바람직하다. 그리고 중복되는 예외처리 같은 경우 함수처리하는게 좋다
        // 하지만 일단 컨트롤러에서 처리

        // 한줄이니까 그냥 여기서 처리해보자
        memberRepository.updatePassword(passwordEncoder.encode((String) newPw1.get("newPw1")), principal.getName());

        return new ResponseEntity("비밀번호 수정 완", HttpStatus.OK);

    }

    @PutMapping("/updateInfo")
    public @ResponseBody ResponseEntity updateInfo(@RequestBody JSONObject memdata,
                                                   BindingResult bindingResult,
                                                   Principal principal
    ) {
        if (bindingResult.hasErrors()) {
            StringBuilder sb = new StringBuilder();
            List<FieldError> fieldErrors = bindingResult.getFieldErrors();
            for (FieldError fieldError : fieldErrors) {
                sb.append(fieldError.getDefaultMessage());
            }
            return new ResponseEntity<String>(sb.toString(), HttpStatus.BAD_REQUEST);
        }

        memberService.updateInfo(principal.getName(), memdata);

        return new ResponseEntity("수정 되었습니다", HttpStatus.OK);
        // 여기서는 save를 했는데 아이탬 수정에선 왜 따로 save가 없는지 차이 질문.
        // 왜나햐면 엔티티는 영속성 컨텍스트에 의해 관리되기 때문.
        // 그렇기 떄문에 따로 레파지토리save 를 하지 않아도 저절로 등록이 된다...
    }

//    @GetMapping("/address")
//    public String myAddress() {
//        return "mypage/address";
//    }

    @PostMapping("/addAddress")
    public @ResponseBody ResponseEntity addAddress(@RequestBody AddressDTO addressDTO, Principal principal, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            StringBuilder sb = new StringBuilder();
            List<FieldError> fieldErrors = bindingResult.getFieldErrors();
            for (FieldError fieldError : fieldErrors) {
                sb.append(fieldError.getDefaultMessage());
            }
            return new ResponseEntity<String>(sb.toString(), HttpStatus.BAD_REQUEST);
        }

        System.out.println("서버 응답 받음");

        memberService.addAddress(addressDTO, principal.getName());

        return new ResponseEntity("주소가 등록되었습니다", HttpStatus.OK);
    }

    @GetMapping("/address")
    public String address(Model model, Principal principal) {

        Member member = memberRepository.findByEmail(principal.getName());

        List<Address> addresses = addressRepository.findAllByMember(member);

        model.addAttribute("addresses", addresses);

        return "mypage/address";
    }

    @GetMapping("/modify")
    public @ResponseBody ResponseEntity addressModify(@RequestParam("id") Long id, Principal principal) throws JsonProcessingException {

        // json 형식으로 바꾸는 작업이 더 필요하다면 함수처리.
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);

        Address address = addressRepository.findById(id).orElseThrow(EntityNotFoundException::new);

        String jsonValue;
        jsonValue = objectMapper.writeValueAsString(address);

        return new ResponseEntity(jsonValue, HttpStatus.OK);
    }

    @PutMapping("/modify")
    public ResponseEntity<String> modifyAddress(@RequestBody AddressDTO addressDTO, BindingResult bindingResult) {
        // 받아 온 값 저절로 DTO에 담음.

        if (bindingResult.hasErrors()) {
            StringBuilder sb = new StringBuilder();
            List<FieldError> fieldErrors = bindingResult.getFieldErrors();
            for (FieldError fieldError : fieldErrors) {
                sb.append(fieldError.getDefaultMessage());
            }
            return new ResponseEntity<String>(sb.toString(), HttpStatus.BAD_REQUEST);
        }

        memberService.modifyAddress(addressDTO);

        return new ResponseEntity<>(HttpStatus.OK);
    }

//    @GetMapping("/address/{addressId}")
//    public String addressModify(Model model,@PathVariable("addressId") Long addressId){
//
//        Address address = addressRepository.findById(addressId).orElseThrow(EntityNotFoundException::new);
//
//        AddressDTO addressDTO = AddressDTO.of(address);
//
//        model.addAttribute("addressDTO",addressDTO);
//        return ;
//
//
//    }

}