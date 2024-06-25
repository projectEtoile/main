package com.keduit.shop.service;

import com.keduit.shop.constant.ItemSellStatus;
import com.keduit.shop.dto.ItemFormDTO;
import com.keduit.shop.entity.Item;
import com.keduit.shop.repository.ItemRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
public class ItemServiceTests {

  @Autowired
  ItemService itemService;

  @Autowired
  ItemRepository itemRepository;


  List<MultipartFile> createMultipartFiles() {

    List<MultipartFile> multipartFiles = new ArrayList<>();

    for (int i = 0; i < 3; i++) {
      String path = "D:/shop/item/";
      String imageName = "image" + i + ".jpg";
      MockMultipartFile multipartFile =
              new MockMultipartFile(path, imageName, "image/jpg", new byte[]{1, 2, 3, 4});
      multipartFiles.add(multipartFile);
    }
    return multipartFiles;
  }

  @Test
  void testItemStop(){
    List<Item> itemList = new ArrayList<Item>();
     itemList = itemRepository.findAll();

     for (Item a : itemList){
       if(a.getItemNm().contains("테스트")){
         a.setItemSellStatus(ItemSellStatus.STOP_SALE);
         itemRepository.save(a);
       }
     }
  }

  @Test
  @DisplayName("상품 100개 등록해보기")
  void saveItem() throws Exception {

    for (int i = 1; i < 201; i++) {

      ItemFormDTO itemFormDTO = new ItemFormDTO();

      itemFormDTO.setItemNm("테스트 상품명 " + i);
      itemFormDTO.setBrandNm("테스트 브랜드명 " + i);
      itemFormDTO.setPrice(8888 + (i * 50));

      itemFormDTO.setLevel1("Outer");

      if (i % 3 == 0) {
        itemFormDTO.setLevel2("코트");
      } else if (i % 4 == 0) {
        itemFormDTO.setLevel2("재킷");
      } else if (i % 5 == 0) {
        itemFormDTO.setLevel2("집업");
      } else {
        itemFormDTO.setLevel2("패딩");
      }

      itemFormDTO.setItemText("테스트 상품 설명 " + i);
      itemFormDTO.setMaterial("테스트 상품 소재 " + i);
      itemFormDTO.setStockFree(10);
      itemFormDTO.setStockM(20);
      itemFormDTO.setStockL(30);
      itemFormDTO.setStockS(40);
      if (i % 3 == 0) {
        itemFormDTO.setItemSellStatus(ItemSellStatus.STOP_SALE);
      } else if(i % 4 == 0) {
        itemFormDTO.setItemSellStatus(ItemSellStatus.SOLD_OUT);
      } else {
        itemFormDTO.setItemSellStatus(ItemSellStatus.SELL);
      }

      List<MultipartFile> multipartFileList = createMultipartFiles();

      itemService.saveItem(itemFormDTO, multipartFileList);
    }
  }
}
