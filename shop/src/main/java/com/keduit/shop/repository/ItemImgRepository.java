package com.keduit.shop.repository;

import com.keduit.shop.entity.ItemImg;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ItemImgRepository extends JpaRepository<ItemImg, Long> {

    ItemImg findByItemId(Long itemId);


    // 아이탬 아이디로 해당 되는 이미지를 꺼내오기 위한 메서드 시그니처.
    // jpa 문법 해성 : itemId로 모두 꺼내라. 순서는 itemImgId 오름차순으로.
    List<ItemImg> findByItemIdOrderByIdAsc(Long itemId);
}
