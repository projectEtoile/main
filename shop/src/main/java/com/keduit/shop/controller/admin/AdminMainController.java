package com.keduit.shop.controller.admin;

import com.keduit.shop.constant.OrderStatus;
import com.keduit.shop.constant.Sex;
import com.keduit.shop.entity.Order;
import com.keduit.shop.repository.ItemRepository;
import com.keduit.shop.repository.MemberRepository;
import com.keduit.shop.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;


@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminMainController {

    private final MemberRepository memberRepository;
    private final OrderRepository orderRepository;
    private final ItemRepository itemRepository;

    @GetMapping("/main")
    public String itemForm(Model model) {

        long male = memberRepository.countMembersBySex(Sex.MALE);
        long female = memberRepository.countMembersBySex(Sex.FEMALE);

        System.out.println(orderRepository.findByOrderStatus(OrderStatus.ORDER).size()+"@@@@");
        System.out.println(orderRepository.findByOrderStatus(OrderStatus.DELIVERING).size()+"@@@@");
        System.out.println(orderRepository.findByOrderStatus(OrderStatus.DELIVERED).size()+"@@@@");
        List<Order> orderList = orderRepository.findByOrderStatus(OrderStatus.DELIVERED);

        int AllOrderPrice = 0;
        for (Order order : orderList){
            int orderPrice = order.getOrderItems().get(0).getCount() *
            order.getOrderItems().get(0).getItem().getPrice();
            AllOrderPrice += orderPrice;
        }



        model.addAttribute("male",male);
        model.addAttribute("female",female);
        return "admin/main";
    }

}
