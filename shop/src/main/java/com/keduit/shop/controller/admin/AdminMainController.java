package com.keduit.shop.controller.admin;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.keduit.shop.constant.OrderStatus;
import com.keduit.shop.constant.Sex;
import com.keduit.shop.entity.Order;
import com.keduit.shop.repository.ItemRepository;
import com.keduit.shop.repository.MemberRepository;
import com.keduit.shop.repository.OrderRepository;
import com.keduit.shop.service.OrderService;
import lombok.RequiredArgsConstructor;
import net.minidev.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.*;


@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminMainController {

    private final MemberRepository memberRepository;
    private final OrderRepository orderRepository;
    private final ItemRepository itemRepository;
    private final OrderService orderService;

    @GetMapping("/main")
    public String itemForm(Model model) throws JsonProcessingException {
        List<Order> orderList = orderRepository.findByOrderStatus(OrderStatus.ORDER);
        List<Order> deliveringList = orderRepository.findByOrderStatus(OrderStatus.DELIVERING);
        List<Order> deliveredList = orderRepository.findByOrderStatus(OrderStatus.DELIVERED);
        List<Order> return_requestList = orderRepository.findByOrderStatus(OrderStatus.RETURN_REQUEST);
        List<Order> return_completedList = orderRepository.findByOrderStatus(OrderStatus.RETURN_COMPLETED);

        Long orderTotalPrice = orderService.totalPrice(orderList);
        Long deliveringTotalPrice = orderService.totalPrice(deliveringList);
        Long deliveredTotalPrice = orderService.totalPrice(deliveredList);
        Long return_requestTotalPrice = orderService.totalPrice(return_requestList);
        Long return_completedTotalPrice = orderService.totalPrice(return_completedList);

        int orderSize = orderList.size();
        int deliveringSize = deliveringList.size();
        int deliveredSize = deliveredList.size();
        int return_requestSize = return_requestList.size();
        int return_completedSize = return_completedList.size();

        Optional<Integer> TTP = orderRepository.getTotalPriceByLevel1("Top");
        int ttp = TTP.orElse(0);
        Optional<Integer> OTP = orderRepository.getTotalPriceByLevel1("Outer");
        int otp = OTP.orElse(0);
        Optional<Integer> PTP = orderRepository.getTotalPriceByLevel1("Pants");
        int ptp = PTP.orElse(0);
        Optional<Integer> SDTP = orderRepository.getTotalPriceByLevel1("Skirt/Dress");
        int sdtp = SDTP.orElse(0);
        Optional<Integer> STP = orderRepository.getTotalPriceByLevel1("Shoes");
        int stp = STP.orElse(0);

        List<Map<String, Object>> orderRank =orderRepository.findTop10ItemsOrderedByCount();
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonOrderRank = objectMapper.writeValueAsString(orderRank);
        System.out.println(jsonOrderRank);


        for (Map<String, Object> item : orderRank) {
            Object itemId =  item.get("itemId");
            Object totalCount = item.get("totalCount");

            String output = "ItemId: " + itemId + ", TotalCount: " + totalCount;
            System.out.println(output);
        }

        Map<String, Object> data = new HashMap<>();

        data.put("orderTotalPrice", orderTotalPrice);
        data.put("deliveringTotalPrice", deliveringTotalPrice);
        data.put("deliveredTotalPrice", deliveredTotalPrice);
        data.put("return_requestTotalPrice", return_requestTotalPrice);
        data.put("return_completedTotalPrice", return_completedTotalPrice);
        data.put("orderSize", orderSize);
        data.put("deliveringSize", deliveringSize);
        data.put("deliveredSize", deliveredSize);
        data.put("return_requestSize", return_requestSize);
        data.put("return_completedSize", return_completedSize);
        data.put("ttp", ttp);
        data.put("otp", otp);
        data.put("ptp", ptp);
        data.put("sdtp", sdtp);
        data.put("stp", stp);
        data.put("jsonOrderRank", jsonOrderRank);


        model.addAttribute("data", data);

        return "admin/main";
        /*        model.addAttribute("data", Map.of(
                "orderTotalPrice", orderTotalPrice,
                "deliveringTotalPrice", deliveringTotalPrice,
                "deliveredTotalPrice", deliveredTotalPrice,
                "return_requestTotalPrice", return_requestTotalPrice,
                "return_completedTotalPrice",return_completedTotalPrice,
                "orderSize", orderSize,
                "deliveringSize", deliveringSize,
                "deliveredSize", deliveredSize,
                "return_requestSize", return_requestSize,
                "return_completedSize",return_completedSize,
                "TTP",TTP,
                "OTP",OTP,
                "PTP",PTP,
                "SDTP",SDTP,
                "STP",STP,
        ));*/
    }

}
