package com.keduit.shop.controller;
import com.keduit.shop.entity.SearchRank;
import com.keduit.shop.repository.SearchRankRepository;
import com.keduit.shop.service.SearchRankService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class SearchRankController {

    private final SearchRankRepository searchrankRepository;
    private final SearchRankService searchRankService; // 서비스 주입

    @GetMapping("/rank")
    public String searchRank(Model model) {
        List<SearchRank> searchRankList = searchRankService.getAllSearchRank();
        model.addAttribute("searchRankList", searchRankList);
        System.out.println("=====================searchRankList.get(3)==================" + searchRankList.get(3));
        return "fragments/header";
    }
}