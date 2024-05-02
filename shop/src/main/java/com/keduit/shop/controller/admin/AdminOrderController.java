package com.keduit.shop.controller.admin;

import com.keduit.shop.dto.AdminOrderSearchDTO;
import com.keduit.shop.dto.ItemFormDTO;
import com.keduit.shop.entity.Order;
import com.keduit.shop.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Optional;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor // 싱글톤 패턴 주입받기 위한
public class AdminOrderController {

    private final OrderService orderService;

    @GetMapping({"/orders/{page}", "/orders"})
    public String orderListPage(Model model,
                                @PathVariable("page")Optional<Integer> page,
                                AdminOrderSearchDTO adminOrderSearchDTO){

        Pageable pageable = PageRequest.of(page.isPresent() ? page.get() : 0, 10);

        Page<Order> orders = orderService.getAdminOrderPage(adminOrderSearchDTO, pageable);


        System.out.println(orders.getContent().get(0).getOrderItems().get(0).getItem().getItemNm());
        System.out.println(orders.getContent().get(0).getOrderItems().get(0).getCount());
            orders.getContent().get(0).getOrderStatus();

//        orders.getContent().get(


        model.addAttribute("orders", orders);
        model.addAttribute("adminOrderSearchDTO", adminOrderSearchDTO);
        model.addAttribute("maxPage", 10);

        System.out.println(orders.getNumber()+"@@@@@@@@@@@@@@@@@@@@@@");

        return "admin/orderList";

    }



}
