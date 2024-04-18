package com.keduit.shop.entity1;

import com.keduit.shop.constant.ItemSellStatus;
import lombok.*;

import javax.persistence.*;

@Entity
@Setter
@Getter
@ToString
@AllArgsConstructor // 모든 필드 포함 생성자
@NoArgsConstructor // 디폴트 생성자
public class item extends BaseTimeEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "item_id")
    private Long id;

    @Column(nullable = false,length = 50)
    private String itemNm;

    @Enumerated(EnumType.STRING)
    private ItemSellStatus itemSellStatus;

    @Column(nullable = false)
    private int price;




}
