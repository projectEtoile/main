package com.keduit.shop.repository;

import com.keduit.shop.dto.AdminItemSearchDTO;
import com.keduit.shop.dto.ItemSearchDTO;
import com.keduit.shop.dto.MainItemDTO;
import com.keduit.shop.entity.Item;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ItemRepositoryCustom {

    Page<Item> getAdminItemPage(AdminItemSearchDTO adminItemSearchDTO, Pageable pageable);

    Page<MainItemDTO> getMainItemPage(AdminItemSearchDTO searchDTO, Pageable pageable);

    Page<Item> getItemPage(ItemSearchDTO itemSearchDTO, String category, Pageable pageable);


}
