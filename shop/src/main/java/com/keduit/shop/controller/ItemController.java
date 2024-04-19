package com.keduit.shop.controller;

import com.keduit.shop.dto.ItemFormDTO;
import com.keduit.shop.service.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequiredArgsConstructor // 싱글톤 패턴 주입받기 위한
public class ItemController {

    private final ItemService itemService;

   @GetMapping("/admin/item/new")
   public String itemForm(Model model){

       System.out.println("@@@@@@@getMapping 요청받음@@@@@@@@@@@@@@@@@@");
        model.addAttribute("itemFormDTO", new ItemFormDTO());

       return "admin/itemForm";

   }
    @PostMapping("/admin/item/new")
    public String itemNew(@Valid ItemFormDTO itemFormDTO, BindingResult bindingResult, Model model,
                          @RequestParam("itemImgFile") List<MultipartFile> itemImgFileList){
                            // @RequestParam 이부분은 th:field 로 받은게 아닌 name으로 따로 받은 부분이다.
        if(bindingResult.hasErrors()){
            System.out.println(itemFormDTO+"@@@@@@@@@@@@@@@@@@@@@@@@@");
            System.out.println(itemImgFileList +"#######################");
            return "admin/itemForm";
        }
        System.out.println(itemFormDTO+"@@@@@@@@@@@@@@@@@@@@@@@@@성공@!!!!!!!!!!!!!!!!!!");
        System.out.println(itemImgFileList +"#######################");

        try{
            System.out.println(itemFormDTO + "@@@@@@@@@@@@@@@@@@@@@@@"+itemImgFileList);

            itemService.saveItem(itemFormDTO, itemImgFileList);
        }catch (Exception e){

            model.addAttribute("errorMessage", "상품 등록 중 에러가 발생했습니다.");
            return "item/itemForm";

        }

        return "template";

    }
}
