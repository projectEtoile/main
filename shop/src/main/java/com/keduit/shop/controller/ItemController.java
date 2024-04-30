package com.keduit.shop.controller;

import com.keduit.shop.dto.AdminItemSearchDTO;
import com.keduit.shop.dto.ItemFormDTO;
import com.keduit.shop.entity.Item;
import com.keduit.shop.repository.ItemRepository;
import com.keduit.shop.service.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor // 싱글톤 패턴 주입받기 위한
public class ItemController {

    private final ItemService itemService;
    private final ItemRepository itemRepository;

    @GetMapping("/categoryPadding")
    public String main() {
        return "category/categoryPadding";
    }


    @GetMapping("/item/{itemId}")
    public String itemDtl(Model model, @PathVariable("itemId") Long itemId) {
        ItemFormDTO itemFormDTO = itemService.getItemDtl(itemId);
        model.addAttribute("item", itemFormDTO);
        return "item/itemDtl";
    }

    @GetMapping("/itemDtl/{itemId}")
    public String showItemDetailPage(@PathVariable("itemId") Long itemId, Model model) {
        try {
            ItemFormDTO itemFormDTO = itemService.getItemDtl(itemId);
            model.addAttribute("item", itemFormDTO);
            return "item/itemDtl"; // 상품 상세 페이지의 뷰 이름을 반환합니다.
        } catch (EntityNotFoundException e) {
            model.addAttribute("errorMessage", "존재하지 않는 상품입니다.");
            return "errorPage"; // 오류 페이지나 적절한 대체 페이지 경로를 지정
        }

    }
}


