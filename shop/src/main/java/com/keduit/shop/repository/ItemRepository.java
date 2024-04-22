package com.keduit.shop.repository;

import com.keduit.shop.entity.Item;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemRepository extends JpaRepository<Item, Long>,
    ItemRepositoryCustom{

    // jpa 에서 제공하는 pageable 문법. Page는 리스트 타입의 일종이며 Item 을 객체로 가진다.
    Page<Item> findPageBy(Pageable page);



}
