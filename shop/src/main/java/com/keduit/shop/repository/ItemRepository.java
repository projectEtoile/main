package com.keduit.shop.repository;

import com.keduit.shop.entity.Item;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;

import java.util.List;

/*<Item, Long> = <entity이름, primary key의 데이터 타입>*/
/*QuerydslPredicateExecutor : QueryDsl사용 시 조건에 맞는 조회를 위해 추가*/
public interface ItemRepository extends JpaRepository<Item, Long>,
        QuerydslPredicateExecutor<Item>
        {

    List<Item> findByItemNm(String itemNm);

    List<Item> findByItemNmOrItemText(String itemNm, String itemText);

    List<Item> findByPriceLessThan(Integer price); /*이거 래퍼클래스로주는이유는 객체로 처리해줘야해서*/

    List<Item> findByPriceLessThanOrderByPriceDesc(Integer price);/*금액큰거부터 나오게하기*/

    Page<Item> findPageBy(Pageable page);


    /*위에랑 다르게 JPQL사용하기*/
    @Query("select i from Item i where i.itemText like" +
            " %:itemText% order by i.price desc")
    List<Item> findByItemText(@Param("itemText") String itemDetail);

    /*nativeQuery 사용하면 데이터베이스에서 사용하는 쿼리문 그대로 사용해야함 위에랑 차이점을 확인하기.*/
    @Query(value = "select * from item i where i.item_text like " +
            " %:itemText% order by i.price desc", nativeQuery = true)
    List<Item> findByItemTextByNative(@Param("itemText") String itemText);
}
