package com.keduit.shop.repository;

import com.keduit.shop.dto.AdminMemberSearchDTO;
import com.keduit.shop.entity.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface MemberRepositoryCustom {
   Page<Member> getAdminMemberPage(AdminMemberSearchDTO adminMemberSearchDTO, Pageable pageable);
}
