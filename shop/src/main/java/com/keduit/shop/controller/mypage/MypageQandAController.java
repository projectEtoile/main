package com.keduit.shop.controller.mypage;

import com.keduit.shop.entity.Member;
import com.keduit.shop.entity.QandA;
import com.keduit.shop.service.MemberService;
import com.keduit.shop.service.QandAService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication; // 수정된 임포트
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.data.domain.PageRequest;// 추가된 임포트
import org.springframework.data.domain.Sort; // 추가된 임포트

@Controller
@RequestMapping("/mypage")
@RequiredArgsConstructor
public class MypageQandAController {

    private final QandAService qandAService;
    private final MemberService memberService;

    private static final int PAGE_SIZE = 10;

    @GetMapping("/qanda")
    public String getMyQandA(Model model, Authentication authentication, Integer page) {
        String userEmail = authentication.getName();
        Member member = memberService.findByEmail(userEmail);

        int currentPage = page != null ? page : 0;

        Pageable pageable = PageRequest.of(currentPage, PAGE_SIZE, Sort.by(Sort.Direction.DESC, "regTime"));

        Page<QandA> qandaPage = qandAService.findQuestionsByUserId(member.getId(), pageable);

        model.addAttribute("qandaPage", qandaPage);
        return "mypage/qanda";
    }
}
