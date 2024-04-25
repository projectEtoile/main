package com.keduit.shop.entity;

import com.keduit.shop.constant.ItemSellStatus;
import com.keduit.shop.dto.ItemFormDTO;
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
public class Item extends BaseTimeEntity {

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

    public void updateItem(ItemFormDTO itemFormDTO) {
        this.itemNm = itemFormDTO.getItemNm();
        this.brandNm = itemFormDTO.getBrandNm();
        this.level1 = itemFormDTO.getLevel1();
        this.level2 = itemFormDTO.getLevel2();
        this.itemSellStatus = itemFormDTO.getItemSellStatus();
        this.price = itemFormDTO.getPrice();
        this.material = itemFormDTO.getMaterial();
        this.itemText = itemFormDTO.getItemText();
        this.stockS = itemFormDTO.getStockS();
        this.stockM = itemFormDTO.getStockM();
        this.stockL = itemFormDTO.getStockL();
        this.stockFree = itemFormDTO.getStockFree();
        this.stockNumber = itemFormDTO.getStockS()+itemFormDTO.getStockM()+itemFormDTO.getStockL()+itemFormDTO.getStockFree();
    }

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

    public void removeStock(String size, int stockNumber) {
        switch (size) {
            case "S":
                if (this.stockS < stockNumber) {

                    throw new OutOfStockException("S 사이즈 재고가 부족합니다.");

                }
                this.stockS -= stockNumber; // S 사이즈 감소
                break;
            case "M":
                if (this.stockM < stockNumber) {

                    throw new OutOfStockException("M 사이즈 재고가 부족합니다.");

                }
                this.stockM -= stockNumber; // M 사이즈 감소
                break;
            case "L":
                if (this.stockL < stockNumber) {

                    throw new OutOfStockException("L 사이즈 재고가 부족합니다.");

                }
                this.stockL -= stockNumber; // L 사이즈 감소
                break;
            case "Free":
                if (this.stockFree < stockNumber) {

                    throw new OutOfStockException("Free 사이즈 재고가 부족합니다.");

                }
                this.stockFree -= stockNumber; // Free 사이즈 감소
                break;
            default:
                throw new IllegalArgumentException("유효하지 않은 사이즈입니다.");
        }


        // 모든 사이즈의 재고 감소와 함께 stock_number 감소

        this.stockNumber -= stockNumber;
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
