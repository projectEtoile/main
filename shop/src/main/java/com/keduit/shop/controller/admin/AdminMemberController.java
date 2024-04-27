package com.keduit.shop.controller.admin;

import com.keduit.shop.dto.AdminMemberSearchDTO;
import com.keduit.shop.entity.Member;
import com.keduit.shop.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Optional;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor // 싱글톤 패턴 주입받기 위한
public class AdminMemberController {

    private final MemberService memberService;
    private final PasswordEncoder passwordEncoder;

    @GetMapping({"/members/{page}", "/members"})
    public String memberMangeListPage(Model model,
                                      @PathVariable("page") Optional<Integer> page, // 유저에게 받는 page 숫자.
                                      AdminMemberSearchDTO adminMemberSearchDTO) { //쿼리문을 날리기 위한 정보들!

        Pageable pageable = PageRequest.of(page.isPresent() ? page.get() : 0, 10);

        Page<Member> members = memberService.getAdminMemberPage(adminMemberSearchDTO, pageable);

        System.out.println("----- members.getContent() : " + members.getContent());
        System.out.println("----- adminMemberSearchDTO: " + adminMemberSearchDTO);

        model.addAttribute("members", members);
        model.addAttribute("adminMemberSearchDTO", adminMemberSearchDTO);
        model.addAttribute("maxPage", 10);

        System.out.println(members.getNumber()+"@@@@@@@@@@@@@@@@@@@@@@");

        return "admin/memberMng";
    }
}
