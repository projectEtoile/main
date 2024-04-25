package com.keduit.shop.controller;
import com.keduit.shop.entity.SearchRank;
import com.keduit.shop.repository.SearchRankRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class SearchRankController {

    @Autowired
    private SearchRankRepository searchrankRepository;

    @GetMapping("/searchrank")
    public String searchRank(Model model) {
        List<SearchRank> searchRankList = searchrankRepository.findAll();
        model.addAttribute("searchRankList", searchRankList);
        return "fragments/header";
    }
}