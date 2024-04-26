package com.keduit.shop.service;

import com.keduit.shop.dto.ItemFormDTO;
import com.keduit.shop.dto.ItemImgDTO;
import com.keduit.shop.entity.Item;
import com.keduit.shop.entity.ItemImg;
import com.keduit.shop.repository.ItemImgRepository;
import com.keduit.shop.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class ItemService {

    private final ItemRepository itemRepository;
    private final ItemImgService itemImgService;
    private final ItemImgRepository itemImgRepository;

    public Long saveItem(ItemFormDTO itemFormDTO, List<MultipartFile> itemImgFileList) throws Exception {
        // 아이템 생성
        Item item = itemFormDTO.createItem();
        int total = itemFormDTO.getStockFree() + itemFormDTO.getStockL() + itemFormDTO.getStockM() + itemFormDTO.getStockS();

        item.setStockNumber(total);
        item.setDiscountRate(1); // 할인율 기본값
        itemRepository.save(item);

        // 이미지 파일을 순회하며 설정
        for (int i = 0; i < itemImgFileList.size(); i++) {
            ItemImg itemImg = new ItemImg();
            itemImg.setItem(item);

            if (i == 0) {
                // 첫 번째 이미지는 대표 이미지로 설정
                itemImg.setRepimgYn("Y");
            } else {
                itemImg.setRepimgYn("N");
            }

            itemImgService.saveItemImg(itemImg, itemImgFileList.get(i));
        }

        return item.getId();
    }



    @Transactional(readOnly = true)
    public ItemFormDTO getItemDtl(Long itemId) {
        // 아이템 조회
        Item item = itemRepository.findById(itemId).orElseThrow(EntityNotFoundException::new);

        // 아이템 이미지들 조회
        List<ItemImg> itemImgList = itemImgRepository.findByItemIdOrderByIdAsc(itemId);
        List<ItemImgDTO> itemImgDTOList = new ArrayList<>();
        for (ItemImg itemImg : itemImgList) {
            ItemImgDTO itemImgDTO = ItemImgDTO.of(itemImg);
            itemImgDTOList.add(itemImgDTO);
        }

        // 조회된 아이템과 이미지 정보를 DTO로 변환하여 반환
        ItemFormDTO itemFormDTO = ItemFormDTO.of(item);
        itemFormDTO.setItemImgDTOList(itemImgDTOList);
        return itemFormDTO;
    }
}
