package com.keduit.shop.controller.mypage;

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
        Member member = memberRepository.findByEmail(principal.getName());
        MemberFormDTO memberFormDTO = new MemberFormDTO();
        memberFormDTO.setEmail(member.getEmail());
        memberFormDTO.setName(member.getName());
        memberFormDTO.setAge(member.getAge());
        memberFormDTO.setSex(member.getSex());
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

        String name = (String) memdata.get("name");
        int age = Integer.parseInt(memdata.get("age").toString());
        String sexValue = (String) memdata.get("sex");
        Sex sex = Sex.valueOf(sexValue);

        System.out.println(name+""+age+""+sex);

            Member member = memberRepository.findByEmail(principal.getName());

            member.updateMember(name,age,sex);

            memberRepository.save(member);
            return new ResponseEntity("수정 되었습니다", HttpStatus.OK);
        // 여기서는 save를 했는데 아이탬 수정에선 왜 따로 save가 없는지 차이 질문.
    }

//    @GetMapping("/address")
//    public String myAddress() {
//        return "mypage/address";
//    }

    @PostMapping("/addAddress")
    public @ResponseBody ResponseEntity addAddress(@RequestBody AddressDTO addressDTO,Principal principal,BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            StringBuilder sb = new StringBuilder();
            List<FieldError> fieldErrors = bindingResult.getFieldErrors();
            for (FieldError fieldError : fieldErrors) {
                sb.append(fieldError.getDefaultMessage());
            }
            return new ResponseEntity<String>(sb.toString(), HttpStatus.BAD_REQUEST);
        }

        System.out.println("서버 응답 받음");
        Address address = new Address();
        address.setPostcode(addressDTO.getPostcode());
        address.setRoadAddress(addressDTO.getRoadAddress());
        address.setJibunAddress(addressDTO.getJibunAddress());
        address.setDetailAddress(addressDTO.getDetailAddress());
        address.setExtraAddress(addressDTO.getExtraAddress());

        System.out.println(addressDTO.getSelectAddress());

        if(addressDTO.getSelectAddress().equals(true)){
            System.out.println("트루로직");
            List<Address> allAddresses = addressRepository.findAll();
            for (Address address1 : allAddresses) {
                address1.setSelectAddress(false);
            }
            addressRepository.saveAll(allAddresses);

            address.setSelectAddress(true);

        }else{
            System.out.println("펄스로직");
            address.setSelectAddress(false);
        }


        Member member =  memberRepository.findByEmail(principal.getName());

        address.setMember(member);

        addressRepository.save(address);

        // 저장된 주소 정보를 확인하는 메시지를 반환합니다.
        return new ResponseEntity("주소가 등록되었습니다", HttpStatus.OK);
    }

    @GetMapping("/address")
    public String address(Model model){

        List<Address> addresses = addressRepository.findAll();

        model.addAttribute("addresses",addresses);

        return "mypage/address";
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