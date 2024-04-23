package com.keduit.shop.repository;

import com.keduit.shop.entity.ItemImg;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ItemImgRepository extends JpaRepository<ItemImg, Long> {

    ItemImg findByItemIdAndRepimgYn(Long itemId, String repimgYn);
    List<ItemImg> findByItemIdOrderByIdAsc(Long itemId);
}
