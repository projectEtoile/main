package com.keduit.shop.controller.admin;

import com.keduit.shop.dto.AdminItemSearchDTO;
import com.keduit.shop.dto.ItemFormDTO;
import com.keduit.shop.entity.Item;
import com.keduit.shop.repository.ItemRepository;
import com.keduit.shop.service.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminItemController {

    private final ItemService itemService;
    private final ItemRepository itemRepository;

    @GetMapping("/item/new")
    public String itemForm(Model model) {
        model.addAttribute("itemFormDTO", new ItemFormDTO());
        return "admin/itemForm";
    }

    @PostMapping("/item/new")
    public String itemNew(@Valid ItemFormDTO itemFormDTO, BindingResult bindingResult, Model model,
                          @RequestParam("itemImgFile") List<MultipartFile> itemImgFileList) {
        // @RequestParam 이부분은 th:field 로 받은게 아닌 name으로 따로 받은 부분이다.
        if (bindingResult.hasErrors()) {
            return "admin/itemForm";
        }
        try {
            itemService.saveItem(itemFormDTO, itemImgFileList);
        } catch (Exception e) {
            model.addAttribute("errorMessage", "상품 등록 중 에러가 발생했습니다.");
            return "admin/itemForm";
        }
        return "redirect:/admin/items";
    }


//    @GetMapping("admin/items")
//    public String itemManage(Model model){
//        // 어드민의 아이탬 목록을 뽑을 수 있게 리스트 타입으로 몽땅 뽑음.
//        List<Item> items = itemRepository.findAll();
//       //일단 페이지 띄우기
//        model.addAttribute("items",items); // 뿌릴 키워드 설정.
//        return "admin/itemMng";
//    }


    @GetMapping({"/items/{page}", "/items"})
// {} 이란 무엇인가? 어떤 값을 넣든 들어올 수 있음. 다만. 그 값을 로직에서 사용하기 위해.
    public String itemMangeListPage(Model model,
                                    // 여기서 @PathVariable 이란? 메핑된 url에서의 {num}.
                                    @PathVariable("page") Optional<Integer> page, // 유저에게 받는 page 숫자.
                                    AdminItemSearchDTO adminItemSearchDTO) { //쿼리문을 날리기 위한 정보들!

        // 없을 경우도 로직을 정하기 위해 Optional 을 사용. (isPresent())

        // 여기서 PageReauest.of ()에 들어오는 파라미터는?
        // 첫째. 몇번 째 페이지를 가져올지.
        // 둘째. 몇개 씩 가져올지
        // 셋쩨 (선택가능) 정렬기준 이다.

        Pageable pageable = PageRequest.of(page.isPresent() ? page.get() : 0, 10);
        // 일단 페이지 너블 따로 생성. 값 설정까지.

        Page<Item> items = itemService.getAdminItemPage(adminItemSearchDTO, pageable);

        System.out.println("----- items.getContent() : " + items.getContent());
        System.out.println("----- adminItemSearchDTO: " + adminItemSearchDTO);

        model.addAttribute("items", items);
        model.addAttribute("adminItemSearchDTO", adminItemSearchDTO); // 검색어 유지시키기 위한 설정!
        model.addAttribute("maxPage", 10); // 부트스트랩 페이징 뷰를 위한?
        // 맞다. 1부터 10까지 선택 가능하게할지!

        // Page 와 Slice의 차이! Page는 일단 총 수량 계산. Slice는 딱 정해진것만 가져옴. 성능 더 좋음

        // 전체 페이지 수를 출력해보자
        System.out.println(items.getTotalElements());

        return "admin/itemMng";
    }

    @GetMapping("/item/modify/{itemId}")
    public String adminItemDtl(Model model, @PathVariable("itemId") Long itemId) {

        ItemFormDTO itemFormDTO = itemService.getItemDtl(itemId);
        System.out.println("#$$$$$$#$#$#$#$#$#$$$$" + itemFormDTO);
        model.addAttribute("itemFormDTO", itemFormDTO);
        return "admin/itemModify";

    }

    @PostMapping("/item/modify/{itemId}")
    public String itemUpdate(@Valid ItemFormDTO itemFormDTO,
                             BindingResult bindingResult,
                             @RequestParam("itemImgFile") List<MultipartFile> itemImgFileList,
                             Model model) {

        if (bindingResult.hasErrors()) {
            System.out.println(" post 수정 유효성 검사 실패@@@@@@@@@@@@@@@@@@@");
            System.out.println(itemFormDTO.getItemImgIds());
            return "item/itemForm"; // 유효성 검사 실패시 돌아간다.
            // 하지만 이럼 수정했던 정보가 날아가므로 html에서 유효성 검사를 모두 한다.

        }
        try {
            itemService.updateItem(itemFormDTO, itemImgFileList);
        } catch (Exception e) {
            model.addAttribute("errorMessage", "상품 수정 중 에러가 발생했습니다.");
            System.out.println(itemFormDTO.getItemImgIds());
            System.out.println("실패@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");
            return "redirect:/admin/main";
        }
        System.out.println(itemFormDTO.getItemImgIds());
        System.out.println("포스트수정 응답받음@@@@@@@@@@@@@@@@@");
        return "redirect:/admin/item/modify/"+itemFormDTO.getId();
    }

}
