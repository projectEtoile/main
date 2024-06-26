package com.keduit.shop.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@ToString
public class OrderItem extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_item_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private Item item;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;

    private int orderPrice; /*주문금액*/
    private int count; /*수량*/
    private String size; /*사이즈*/


 /*   public static OrderItem createOrderItem(Item item, int count) {
        OrderItem orderItem = new OrderItem();
        orderItem.setItem(item);
        orderItem.setCount(count);
        orderItem.setOrderPrice(item.getOriginPrice());
        *//*이제 재고계산 가능 다있으니까*//*
        item.removeStock(count);
        return orderItem;

    }*/

    public static OrderItem createOrderItem(Item item, int count, String size) {
        OrderItem orderItem = new OrderItem();
        orderItem.setItem(item);
        orderItem.setCount(count);
        orderItem.setOrderPrice(item.getPrice() * count); // 주문 가격 = 상품 가격 * 주문 수량
        orderItem.setSize(size);

        // 선택한 사이즈에 따라 재고 감소


        return orderItem;
    }

    public int getTotalPrice() {
        return orderPrice * count;
    }

    /*주문 취소시 재고 증가*/
    public void cancel() {
        this.getItem().addStock(count);
    }
}
