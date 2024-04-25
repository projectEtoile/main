package com.keduit.shop.dto;

import com.keduit.shop.constant.OrderStatus;
import com.keduit.shop.entity.Order;
import com.keduit.shop.entity.OrderItem;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ToString
public class OrderHistDTO {


    /*필드*/
    private Long orderId;  // 주문 아이디
    private String orderDate;
    private OrderItem.OrderStatus orderStatus;  // 주문상태

    /*생성자*/
    public OrderHistDTO(Order order) {
        this.orderId = order.getId();
        this.orderDate = order.getOrderDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
        this.orderStatus = order.getOrderStatus();
    }

    /*주문상품리스트*/
    private List<OrderItemDTO> orderItemDTOList = new ArrayList<>();

    /*orderItemDTO를 주문 상품 리스트에 추가*/
    public void addOrderItemDTO(OrderItemDTO orderItemDTO) {
        orderItemDTOList.add(orderItemDTO);
    }
}
