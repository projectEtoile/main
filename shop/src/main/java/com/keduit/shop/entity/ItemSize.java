package com.keduit.shop.entity;


import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ItemSize extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "item_size_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private Item item;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "size_id")
    private Size size;

    @Column(nullable = false)
    private int stock;  // 해당 사이즈의 재고 수량
    /*Item Size엔티티가 있어야 각상품이 가지는 사이즈정보와 해당 사이즈의 재고수량을
    * 관리할수있음. */

}
