package com.keduit.shop.service;

import com.keduit.shop.dto.AdminItemSearchDTO;
import com.keduit.shop.dto.ItemFormDTO;
import com.keduit.shop.dto.ItemImgDTO;
import com.keduit.shop.dto.MainItemDTO;
import com.keduit.shop.entity.Item;
import com.keduit.shop.entity.ItemImg;
import com.keduit.shop.entity.QItemImg;
import com.keduit.shop.repository.ItemImgRepository;
import com.keduit.shop.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

        Item item = itemFormDTO.createItem(); // modelmapper을 이용하여 간단하게 메핑.
        // 이제 나머지 엔티티 필드들을 세팅해줘야한다
        int total = itemFormDTO.getStockFree()
                + itemFormDTO.getStockL()
                + itemFormDTO.getStockM()
                + itemFormDTO.getStockS();

        item.setStockNumber(total);
        item.setDiscountRate(1f); // 할인율 기본값
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

            // 여기선 item객체만 set하고 이제 나머지 값들을 set해야함.
            itemImgService.saveItemImg(itemImg, itemImgFileList.get(i));
             
            // 위 메서드에서 나머지 필드들을 모두 채우고 테이블에 등록까지 완료했다.
        }

       return item.getId();
    }

    @Transactional(readOnly = true)
    public Page<Item> getAdminItemPage(AdminItemSearchDTO adminItemSearchDTO, Pageable pageable) {
        return itemRepository.getAdminItemPage(adminItemSearchDTO, pageable);
        // custom 한 레파지토리를 쓰기위해.
        // 혹시 예외 시 아이탬서비스 impl 에 dsl 추가

    }

    // 이곳은 하나의 itemid로 아이탬의 정보와 이미지를 itemFormDTO에 담아 페이지로 넘겨주기 위한 로직.
    @Transactional(readOnly = true) // 뿌려주기만 하는 역할이므로. 변경감지. 즉 더티체크를 하지않아 성능 이점.
    public ItemFormDTO getItemDtl(Long itemId) {
        // itemImgId 오름차 순으로 해당 itemId를 가진 모든 객체 소환.
        List<ItemImg> itemImgList = itemImgRepository.findByItemIdOrderByIdAsc(itemId);
        // 하지만 화면에는 DTO로 뿌려줘야 하기에 이 VO를 DTO로 변경해줘야한다. 이를 쉽게하기 위해
        // modelMapper을 활용해보자.
        List<ItemImgDTO> itemImgDTOList = new ArrayList<>();

        for (ItemImg itemImg : itemImgList) { // 꺼낸 값을 List타입에 담는데. itemImg에 매핑하기 위한작업.

            ItemImgDTO itemImgDTO = ItemImgDTO.of(itemImg);
            // for문에서의 하나의 itemImg 객체를 itemImgDTO로 변환했다.

            itemImgDTOList.add(itemImgDTO); // 이를 해당되는 하나씩 리스트에 담자!
            // 핵심은 modelMapping 이다.

        }
        // 일단 이미지DTO리스트엔 이미지를 모두 담았다. 이제 이미지 외 정보를들 itemFormDTO에 담아보자!
        Item item = itemRepository.findById(itemId).orElseThrow(EntityNotFoundException::new);
        // 옵셔널타입이기에 예외처리를 친절히 해줬다. 여기서 예외는 itemId에 해당되는 값을 못찾을 경우밖에 없기때문.
        // 물론 따지면 itemId가 Long 타입이 아니라던지도 있을 순 있다.

        // item 객체 하나는 찾아냈다. 이를 마찬가지로 DTO로 변경해야한다.
        ItemFormDTO itemFormDTO = ItemFormDTO.of(item);
        // 이제 최종으로 리턴해야 하는 itemFormDTO를 생성했다.
        // 여기에 DTO타입에 맞게 매핑을 해 이미지 외 모든 요소를 담았다

        itemFormDTO.setItemImgDTOList(itemImgDTOList);
        // 이제 처음에 만들어 뒀던 DTO 타입의 해당 이미지List 를 셋한다.
        // 이로서 모든 요소가 담겨있는 DTO를 찾아내 완성했다.

        return itemFormDTO;
        // 이걸 넘겨주면 서비스 역할은 끝.
    }

    public Long updateItem(ItemFormDTO itemFormDTO, List<MultipartFile> itemImgFileList) throws Exception {

        Item item = itemRepository.findById(itemFormDTO.getId())
                .orElseThrow(EntityNotFoundException::new); // DTO에 담긴 id로 해당 아이탬 찾아냄.

        item.updateItem(itemFormDTO);   // DTO에 있던 정보들로 새로 업데이트.
        // 하지만 itemimgList와 idList는 아직 업데이트되지않음
        List<Long> itemImgIds = itemFormDTO.getItemImgIds();


        // 이미지 등록
        for (int i = 0; i < itemImgFileList.size(); i++) {

            System.out.println("******* " + i + " ------ " + itemImgIds.get(i) + " ----" + itemImgFileList.get(i));
//      상품이미지를 업데이트 하기 위해
//      updateItemImg()메소드에 상품이미지 아이디와 상품이미지 파일 정보를 파라미터로 전달 함.
            itemImgService.updateItemImg(itemImgIds.get(i), itemImgFileList.get(i));
        }
        return item.getId();
    }

    @Transactional(readOnly = true)
    public Page<MainItemDTO> getMainItemPage(AdminItemSearchDTO searchDTO, Pageable pageable) {
        return itemRepository.getMainItemPage(searchDTO, pageable);
    }

    @Transactional
    public ResponseEntity categoryDiscountRate(String categorySelect, float discountRate) {

        List<Item> itemList = itemRepository.findByLevel1(categorySelect);

        if(itemList.isEmpty()){
            return new ResponseEntity<>("카테고리에 해당하는 상품이 없습니다.",HttpStatus.BAD_REQUEST);
        }

        for (Item item : itemList){
            item.setDiscountRate(discountRate);
        }
        itemRepository.saveAll(itemList);
        return new ResponseEntity<>(HttpStatus.OK);

    }

}
