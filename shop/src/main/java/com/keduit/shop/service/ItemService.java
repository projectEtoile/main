package com.keduit.shop.service;

import com.keduit.shop.dto.AdminItemSearchDTO;
import com.keduit.shop.dto.ItemFormDTO;
import com.keduit.shop.dto.ItemImgDTO;
import com.keduit.shop.entity.Item;
import com.keduit.shop.entity.ItemImg;
import com.keduit.shop.repository.ItemImgRepository;
import com.keduit.shop.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

    public Long saveItem(ItemFormDTO itemFormDTO, List<MultipartFile> itemImgFileList) throws Exception{

        Item item = itemFormDTO.createItem(); // modelmapper을 이용하여 간단하게 메핑.
        // 이제 나머지 엔티티 필드들을 세팅해줘야한다
        int total = itemFormDTO.getStockFree()
                +itemFormDTO.getStockL()
                +itemFormDTO.getStockM()
                +itemFormDTO.getStockS();

        item.setStockNumber(total);
        item.setDiscountRate(1); // 기본값인 할인률1

        System.out.println(item+"@@@@@@@@@@@@@@@@@@@@@@@@@@@@");

        itemRepository.save(item);

        // 이제 Itemimg 를 세팅해야한다.

        for (int i = 0; i < itemImgFileList.size(); i++) {

            ItemImg itemImg = new ItemImg(); // 각각 행을 추가해야하므로.
            itemImg.setItem(item);

            // 추가로 if 로직을 짜서 어디에 해당하는 이미지인지 설정값들 줄 수도 있지만 생략함.

            // 여기선 item객체만 set하고 이제 나머지 값들을 set해야함.
            itemImgService.saveItemImg(itemImg, itemImgFileList.get(i));
             
            // 위 메서드에서 나머지 필드들을 모두 채우고 테이블에 등록까지 완료했다.
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
    // 검색을 위한 메서드이므로 값이 변하지 않는다는 것을 알려줌
    // 으로서 성능을 효울적으로 사용
    @Transactional(readOnly = true)
    public Page<Item> getAdminItemPage(AdminItemSearchDTO adminItemSearchDTO, Pageable pageable){
        return itemRepository.getAdminItemPage(adminItemSearchDTO, pageable);
        // custom 한 레파지토리를 쓰기위해.
        // 혹시 예외 시 아이탬서비스 impl 에 dsl 추가

    }
}
