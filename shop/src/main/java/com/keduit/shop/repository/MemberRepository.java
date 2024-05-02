package com.keduit.shop.repository;

import com.keduit.shop.constant.Sex;
import com.keduit.shop.dto.AdminItemSearchDTO;
import com.keduit.shop.entity.Item;
import com.keduit.shop.entity.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> ,MemberRepositoryCustom {//<이름, 타입>
    Member findByEmail(String email);

    @Query("select m from Member m where m.email = :email and m.social=false")
    Optional<Member> getWithRoles(String email);

    //password update
    @Modifying
    @Transactional
    @Query("update Member m set m.password = :password where m.email = :email")
    void updatePassword(@Param("password")String password, @Param("email")String email);

    @Query("SELECT COUNT(m) FROM Member m WHERE m.sex = :sex")
    Long countMembersBySex(@Param("sex")Sex sex);
}


