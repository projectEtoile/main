package com.keduit.shop.controller;

import com.keduit.shop.dto.ItemFormDTO;
import com.keduit.shop.entity.Item;
import com.keduit.shop.repository.ItemRepository;
import com.keduit.shop.service.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
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

@Controller
@RequiredArgsConstructor // 싱글톤 패턴 주입받기 위한
public class ItemController {

    private final ItemService itemService;
    private final ItemRepository itemRepository;

    @GetMapping("/admin/item/new")
    public String itemForm(Model model) {

        System.out.println("@@@@@@@getMapping 요청받음@@@@@@@@@@@@@@@@@@");
        model.addAttribute("itemFormDTO", new ItemFormDTO());

        return "admin/itemForm";

    }

    @PostMapping("/admin/item/new")
    public String itemNew(@Valid ItemFormDTO itemFormDTO, BindingResult bindingResult, Model model,
                          @RequestParam("itemImgFile") List<MultipartFile> itemImgFileList) {
        // @RequestParam 이부분은 th:field 로 받은게 아닌 name으로 따로 받은 부분이다.
        if (bindingResult.hasErrors()) {
            System.out.println(itemFormDTO + "@@@@@@@@@@@@@@@@@@@@@@@@@");
            System.out.println(itemImgFileList + "#######################");
            return "admin/itemForm";
        }
        System.out.println(itemFormDTO + "@@@@@@@@@@@@@@@@@@@@@@@@@성공@!!!!!!!!!!!!!!!!!!");
        System.out.println(itemImgFileList + "#######################");

        try {
            System.out.println(itemFormDTO + "@@@@@@@@@@@@@@@@@@@@@@@" + itemImgFileList);

            itemService.saveItem(itemFormDTO, itemImgFileList);
        } catch (Exception e) {

            model.addAttribute("errorMessage", "상품 등록 중 에러가 발생했습니다.");
            return "item/itemForm";

        }

        return "main";


    }

    @GetMapping("admin/items")
    public String itemManage(Model model) {
        // 어드민의 아이탬 목록을 뽑을 수 있게 리스트 타입으로 몽땅 뽑음.
        List<Item> items = itemRepository.findAll();
        //일단 페이지 띄우기
        model.addAttribute("items", items); // 뿌릴 키워드 설정.
        return "admin/itemMng";
    }

    @GetMapping("admin/itemsList/page/{num}")
    // {} 이란 무엇인가? 어떤 값을 넣든 들어올 수 있음. 다만. 그 값을 로직에서 사용하기 위해.
    public String itemMangeListPage(Model model,
                                    @PathVariable Integer num) { // 여기서 @PathVariable 이란? 메핑된 url에서의 {num}.
        // 여기서 PageReauest.of ()에 들어오는 파라미터는?
        // 첫째. 몇번 째 페이지를 가져올지.
        // 둘째. 몇개 씩 가져올지
        // 셋쩨 (선택가능) 정렬기준 이다.
        Page<Item> items = itemRepository.findPageBy(PageRequest.of(num - 1, 10));
        // Page 와 Slice의 차이! Page는 일단 총 수량 계산. Slice는 딱 정해진것만 가져옴. 성능 더 좋음

        // 전체 페이지 수를 출력해보자
        System.out.println(items.getTotalElements());

        model.addAttribute("items", items); // 뿌릴 키워드 설정.
        return "admin/itemMng";
    }

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


