package com.keduit.shop.repository;

import com.keduit.shop.entity.Address;
import com.keduit.shop.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AddressRepository extends JpaRepository<Address,Long> {

    List<Address> findAllByMember(Member member);

}
