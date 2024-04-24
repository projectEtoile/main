package com.keduit.shop.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@ToString
@Table(name = "orders")
public class Order extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    private LocalDateTime orderDate; /*주문일*/

    @Enumerated(EnumType.STRING)
    private OrderItem.OrderStatus orderStatus; /*주문상태*/


    private String onDelivery; /*배송중*/

    private String deliveryOver; /*배송완료*/

    private String shipment; /*배송준비중*/

    /*order입장에서 orderitem이 일대 다임*/
    /*order와 orderItem은 일대 다의 연관 관계를 가진다.*/
    /*외래키가 orderItem에 있으므로 연관관계의 주인은 orderItem이됨 -> order는 주인이 아니므로 mappedBy 걸어줘야함*/
    /*mappedby=order의 order는 orderItem에서 관리하는 fk이름*/
    /* 이렇게됨으로써 양방향이됨*/
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<OrderItem> orderItems = new ArrayList<>(); /*item 리스트를 갖고있겠다*/ /*연관관계로 매핑되어있는상태*/


    /*주문목록의 상품하나하나가 orderItem 이걸 list에 담았는데 이 이름을 orderItems라고부름*/
    public void addOrderItem(OrderItem orderItem) {
        orderItems.add(orderItem);/*orderItems에는 주문 상품 정보를 추가함 orderItem객체를 order객체의 orderItems에 추가함*/
        orderItem.setOrder(this);/*Order엔티티와 OrderItem엔티티가 양방향 참조관계이므로 orderItem객체에도 Order객체를 넣어줌*/
    }

    public static Order createOrder(Member member, List<OrderItem> orderItemList) {
        Order order = new Order();
        order.setMember(member);
        /*상품은 리스트에들어있으니까 orderItemList를 꺼내서 add해주면되겠지*/
        for (OrderItem orderItem : orderItemList) {
            order.addOrderItem(orderItem);
        }
        order.setOrderStatus(OrderItem.OrderStatus.ORDER);
        order.setOrderDate(LocalDateTime.now());
        return order;
    }

    public int getTotalPrice() {
        /*총합계구함*/
        int totalPrice = 0;
        for (OrderItem orderItem : orderItems) {
            totalPrice += orderItem.getTotalPrice();
        }
        return totalPrice;
    }

    public void cancelOrder() {

        /*1. 주문 상태를 cancel로 변경*/
        this.orderStatus = OrderItem.OrderStatus.CANCEL;
        /*2. 주문 상품의 주문 수량을 재고에서 증가시킴*/
        for (OrderItem orderItem : orderItems) {
            orderItem.cancel();
        }
    }
}
