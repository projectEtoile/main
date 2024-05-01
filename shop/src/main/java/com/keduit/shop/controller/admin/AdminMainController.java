package com.keduit.shop.controller.admin;

import com.keduit.shop.constant.Sex;
import com.keduit.shop.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminMainController {

    private final MemberRepository memberRepository;

    @GetMapping("/main")
    public String itemForm(Model model) {

        long male = memberRepository.countMembersBySex(Sex.MALE);
        long female = memberRepository.countMembersBySex(Sex.FEMALE);

        model.addAttribute("male",male);
        model.addAttribute("female",female);
        return "admin/main";
    }

}
