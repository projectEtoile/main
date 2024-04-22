package com.keduit.shop.controller;

import com.keduit.shop.entity.Item;
import com.keduit.shop.entity.ItemImg;
import com.keduit.shop.repository.ItemImgRepository;
import com.keduit.shop.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class MainController {

    private final ItemRepository itemRepository;
    private final ItemImgRepository itemImgRepository;

    @GetMapping("/")
    public String main(Model model){
        List<Item> items = itemRepository.findAll();

        List<ItemImg> itemImgs = itemImgRepository.findAll();
        System.out.println(items.get(0));
        System.out.println(itemImgs.get(0).getImgUrl());

        model.addAttribute("items",items);
        model.addAttribute("itemImgs",itemImgs.get(0).getImgUrl());

        return "main";
    }
}
