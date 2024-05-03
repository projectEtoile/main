package com.keduit.shop.service;

import com.keduit.shop.constant.Sex;
import com.keduit.shop.dto.MemberFormDTO;
import com.keduit.shop.dto.OrderDTO;
import com.keduit.shop.entity.Member;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;


@SpringBootTest
public class OrderServiceTests {

    @Autowired
    OrderService orderService;

    // 오더서비스의 오더를 이용. orderDTO와 email 만 필요. 주문상태는 그떄그때 수정.

    @Test
    @DisplayName("주문 테스트")
    public void createOrder() {

        OrderDTO orderDTO = new OrderDTO();
        orderDTO.setCount(3);
        orderDTO.setItemId(1l);
        orderDTO.setCount(3);
        orderDTO.setSize("S");

        for (int i = 0; i < 50; i++) {
            orderService.order(orderDTO,"asddsad@asd.com");
        }


    }

}
