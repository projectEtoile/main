package com.keduit.shop.service;

import com.keduit.shop.constant.ItemSellStatus;
import com.keduit.shop.dto.ItemFormDTO;
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
    @DisplayName("상품 100개 등록해보기")
    void saveItem() throws Exception {

        for (int i = 0; i < 100; i++) {


            ItemFormDTO itemFormDTO = new ItemFormDTO();

            itemFormDTO.setItemNm("테스트 상품명!");
            itemFormDTO.setBrandNm("테스트 브랜드명!");
            itemFormDTO.setPrice(55555);
            itemFormDTO.setLevel1("Top");
            itemFormDTO.setLevel2("블라우스");
            itemFormDTO.setItemText("테스트 상품 설명!");
            itemFormDTO.setMaterial("테스트 상품 소재!");
            itemFormDTO.setStockFree(10);
            itemFormDTO.setStockM(20);
            itemFormDTO.setStockL(30);
            itemFormDTO.setStockS(40);
            itemFormDTO.setItemSellStatus(ItemSellStatus.SELL);

            List<MultipartFile> multipartFileList = createMultipartFiles();

            itemService.saveItem(itemFormDTO, multipartFileList);
        }
    }
}
