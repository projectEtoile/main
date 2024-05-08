package com.keduit.shop.controller;

import com.keduit.shop.dto.*;
import com.keduit.shop.entity.Item;
import com.keduit.shop.entity.Member;
import com.keduit.shop.entity.QandA;
import com.keduit.shop.repository.ItemRepository;
import com.keduit.shop.service.ItemService;
import com.keduit.shop.service.MemberService;
import com.keduit.shop.service.QandAService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.data.domain.*;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor // 싱글톤 패턴 주입받기 위한
public class ItemController {

    private final ItemService itemService;
    private final ItemRepository itemRepository;
    private final MemberService memberService;
    private final QandAService qandAService;

    @GetMapping("/item/{itemId}")
    public String itemDtl(Model model, @PathVariable("itemId") Long itemId) {
        try {
            ItemFormDTO itemFormDTO = itemService.getItemDtl(itemId);
            model.addAttribute("item", itemFormDTO);

            // QandA 목록 가져오기
            List<QandA> qandaList = qandAService.findQuestionsByItemId(itemId);
            model.addAttribute("qandaList", qandaList);

            return "item/itemDtl";
        } catch (EntityNotFoundException e) {
            model.addAttribute("errorMessage", "존재하지 않는 상품입니다.");
            return "errorPage";
        }
    }


    // 컨트롤러 메서드에서 QandA 목록을 모델에 추가


    // Controller 클래스
    @PostMapping("/item/{itemId}/submitQuestion")
    public ResponseEntity<String> submitQuestion(@PathVariable("itemId") Long itemId, @RequestBody QandADTO request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = authentication.getName();
        Member member = memberService.findByEmail(userEmail);

        // 로그로 이메일 확인
        System.out.println("Authenticated User Email: " + userEmail);

        try {
            Item item = itemService.getItemById(itemId);
            QandA qanda = new QandA();
            qanda.setTitle(request.getTitle());
            qanda.setQuestion(request.getQuestion());
            qanda.setAnswer("");
            qanda.setMember(member);
            qanda.setItem(item);
            qanda.setEmail(userEmail);

            qandAService.save(qanda);

            return ResponseEntity.ok("질문이 제출되었습니다.");
        } catch (EntityNotFoundException e) {
            return ResponseEntity.badRequest().body("해당 아이템을 찾을 수 없습니다.");
        }
    }

  @GetMapping("/items/{category}/{page}")
  public String itemsCategoryListPage(Model model,
                                      @PathVariable("page") Optional<Integer> page,
                                      @PathVariable("category") String category,
                                      ItemSearchDTO itemSearchDTO){
    Pageable pageable = PageRequest.of(page.isPresent() ? page.get() : 0, 10);

    List<ItemFormDTO> itemFormDTOList = new ArrayList<>();
    Page<Item> items = itemService.getItemPage(itemSearchDTO, category, pageable);
    System.out.println("==================items================== " + items.getContent());

    for (Item item : items.getContent()){
      ItemFormDTO itemFormDTO = itemService.getItemDtl(item.getId());
      itemFormDTOList.add(itemFormDTO);
    }

    Page<ItemFormDTO> itemFormDTOs = new PageImpl<>(itemFormDTOList, items.getPageable(), items.getTotalElements());
    System.out.println("==================itemFormDTOs================== " + itemFormDTOs.getContent());

    model.addAttribute("item", items);
    model.addAttribute("itemFormDTOs",itemFormDTOs);
    model.addAttribute("itemSearchDTO",itemSearchDTO);
    model.addAttribute("maxPage",10);

    return "category/categoryPage";
  }
}


