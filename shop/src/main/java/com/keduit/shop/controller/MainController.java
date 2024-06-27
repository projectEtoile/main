package com.keduit.shop.controller;

import com.keduit.shop.constant.ItemSellStatus;
import com.keduit.shop.dto.AdminItemSearchDTO;
import com.keduit.shop.dto.ItemFormDTO;
import com.keduit.shop.dto.MainItemDTO;
import com.keduit.shop.entity.Item;
import com.keduit.shop.entity.ItemImg;
import com.keduit.shop.entity.SearchRank;
import com.keduit.shop.repository.ItemImgRepository;
import com.keduit.shop.repository.ItemRepository;
import com.keduit.shop.repository.SearchRankRepository;
import com.keduit.shop.service.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class MainController {

    private final ItemRepository itemRepository;
    private final ItemImgRepository itemImgRepository;
    private final SearchRankRepository searchRankRepository;

  private final ItemService itemService;

  @GetMapping("/")
  public String main(Model model, AdminItemSearchDTO searchDTO, Optional<Integer> page) {

    List<Item> discountRateItems = itemRepository.findTop12ByDiscountRateNotOrderByIdDesc(1f);

    List<ItemFormDTO> itemFormDTOS = new ArrayList<>();

    if(!discountRateItems.isEmpty()){
      for (Item discountRateItem : discountRateItems){
          itemFormDTOS.add(itemService.getItemDtl(discountRateItem.getId()));
      }
      model.addAttribute("message","SALE Category '"+itemFormDTOS.get(0).getLevel1()+"\'");
      model.addAttribute("items", discountRateItems);
      model.addAttribute("itemList", itemFormDTOS);
      return "index.html";
    }
    List<Item> newItems = itemRepository.findFirst12ByItemSellStatusNotOrderByIdDesc(ItemSellStatus.STOP_SALE);
    System.out.println(newItems.toString());
    for (Item newItem : newItems){
      itemFormDTOS.add(itemService.getItemDtl(newItem.getId()));
    }
    model.addAttribute("message","이달의 신상품");
    model.addAttribute("items", newItems);
    model.addAttribute("itemList", itemFormDTOS);




// ------------------

//    List<Item> items = itemRepository.findAll();
//    List<ItemImg> itemImgs = itemImgRepository.findAll();
//    List<ItemFormDTO> itemFormDTOSList = new ArrayList<>();
//
//    System.out.println("============items.size()============" + items.size());
//
//    for (int i = items.size(); i > items.size() - 20; i--) {
//      if (items.size() - 20 < 0) {
//        for (int j = items.size(); j > 0; j--) {
//          itemFormDTOSList.add(itemService.getItemDtl((long) j));
//        }
//      } else {
//        itemFormDTOSList.add(itemService.getItemDtl((long) i));
//      }
//    }
//
//    System.out.println(items.get(0));
//    System.out.println(itemImgs.get(0).getImgUrl());
//
//    model.addAttribute("items", items);
//    model.addAttribute("itemImgs", itemImgs.get(0).getImgUrl());
//    model.addAttribute("itemList", itemFormDTOSList);

        /*Pageable pageable = PageRequest.of(page.isPresent() ? page.get(): 0,  20);
        Page<MainItemDTO> mainItems = itemService.getMainItemPage(searchDTO, pageable);

        model.addAttribute("itemList", mainItems);
        System.out.println(mainItems.getContent());*/
    return "index.html";
  }
}
