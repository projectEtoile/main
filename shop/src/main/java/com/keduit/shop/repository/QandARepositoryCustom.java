package com.keduit.shop.repository;

import com.keduit.shop.dto.AdminItemSearchDTO;
import com.keduit.shop.dto.AdminQNASearchDTO;
import com.keduit.shop.entity.Item;
import com.keduit.shop.entity.QandA;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface QandARepositoryCustom {

    Page<QandA> getAdminQandAListPage(AdminQNASearchDTO adminQNASearchDTO, Pageable pageable);
}
