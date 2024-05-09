package com.keduit.shop.controller;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.keduit.shop.entity.SearchRank;
import com.keduit.shop.repository.SearchRankRepository;
import com.keduit.shop.service.SearchRankService;
import lombok.RequiredArgsConstructor;
import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class SearchRankController {

    private final SearchRankRepository searchrankRepository;
    private final SearchRankService searchRankService; // 서비스 주입

    @GetMapping("/rankSearch")
    public @ResponseBody ResponseEntity searchRank() throws JsonProcessingException {

        List<SearchRank> searchRankList = searchrankRepository.findTop10ByOrderById();
        System.out.println("@@@@@@@@@@@@@@@@@@@@");
        System.out.println("@@@@@@@@@@@@@@@@@@@@");
        System.out.println(searchRankList);

        ObjectMapper objectMapper = new ObjectMapper();
        String jsonOrderRank = objectMapper.writeValueAsString(searchRankList);

        System.out.println(jsonOrderRank);

        return new ResponseEntity(searchRankList, HttpStatus.OK);
    }
}