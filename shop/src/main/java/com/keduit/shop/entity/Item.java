package com.keduit.shop.entity;

import com.keduit.shop.constant.ItemSellStatus;
import com.keduit.shop.exception.OutOfStockException;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Item extends BaseTimeEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "item_id") /*다른테이블에도 id가있으니까 구분하기위해 */
    private Long id;  /*상품 코드*/

    @Column(nullable = false, length = 50)
    private String itemNm;  /*상품이름*/

    @Column(nullable = false)
    private String brandNm; /*상품 브랜드 이름*/

    @Column(nullable = false)
    private int price; // 가격

/*    @Column(name = "originPrice", nullable = false)
    private int originPrice; *//*원래 상품 가격*//*

    @Column(name = "discountPrice", nullable = false)
    private int discountPrice; *//*할인된 상품 가격*/

    @Column(nullable = false)
    private int stockS;

    @Column(nullable = false)
    private int stockM;  

    @Column(nullable = false)
    private int stockL;  

    @Column(nullable = false)
    private int stockFree;

    @Column(nullable = false)
    private int stockNumber;  // 총 재고 수량. 필요한 칼럼.

    @Column(nullable = false)
    private String level1; /*상품상위카테고리*/

    @Column(nullable = false)
    private String level2; /*상품하위카테고리*/

    @Column(nullable = false)
    private int discountRate; // 기본 값 1. N * 상품 가격 으로 할인 예. 10프로 할인 일시 0.9로 등록

    @Lob
    @Column(nullable = false)
    private String itemText; /*상품 요약정보*/

    @Column(nullable = false)
    private String material; /*상품 소재*/


    @Enumerated(EnumType.STRING)  /*도메인만들어줌*/
    private ItemSellStatus itemSellStatus = ItemSellStatus.SELL; // 디폴트SELL  /*상품 판매 현황*/

/*    @Column(nullable = false)
    private String thImgUrl; *//*상품 썸네일 사진*//*

    @Column(nullable = false)
    private String mainImgUrl; *//*상품 메인 사진*/




 /*   public void updateItem(ItemFormDTO itemFormDTO){
    this.itemNm = itemFormDTO.getItemNm();
    this.originPrice = itemFormDTO.getPrice();
    this.stockNumber = itemFormDTO.getStockNumber();
    this.itemText = itemFormDTO.getItemDetail();
    this.itemSellStatus = itemFormDTO.getItemSellStatus();
    }*/

    public void removeStock(int stockNumber) {
        int restStock = this.stockNumber - stockNumber; /*재고계산*/
        /*재고계산했는데 -가 나면 에러를 던질거임*/
        if (restStock < 0) {
            throw new OutOfStockException("상품 재고가 부족합니다. (현재 재고 수량: " + this.stockNumber + ")");
        }
        this.stockNumber = restStock; /*reststock이 이제 재고가 되는것.*/
    }

    /*주문 취소시 재고 증가*/
    public void addStock(int stockNumber) {

        this.stockNumber += stockNumber;
    }

}

    /*@CreationTimestamp
    private LocalDateTime regDate;  *//*등록 시간*//*

    @UpdateTimestamp
    private LocalDateTime updateTime;  *//*수정 시간*//*
}*/
