package com.keduit.shop.controller.admin;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.keduit.shop.constant.OrderStatus;
import com.keduit.shop.constant.Sex;
import com.keduit.shop.entity.Item;
import com.keduit.shop.entity.Order;
import com.keduit.shop.repository.ItemRepository;
import com.keduit.shop.repository.MemberRepository;
import com.keduit.shop.repository.OrderRepository;
import com.keduit.shop.repository.QandARepository;
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
    private final QandARepository qandARepository;

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

        List<Map<String, Object>> orderRank = orderRepository.findTop10ItemsOrderedByCount();


        ObjectMapper objectMapper = new ObjectMapper();
        String jsonOrderRank = objectMapper.writeValueAsString(orderRank);
        System.out.println(jsonOrderRank);


        for (Map<String, Object> item : orderRank) {
            Object itemId = item.get("itemId");
            Object totalCount = item.get("totalCount");

            String output = "ItemId: " + itemId + ", TotalCount: " + totalCount;
            System.out.println(output);
        }

        int unfinished = qandARepository.countByAnswerLengthZero();
        int finished = qandARepository.countByAnswerLengthGreaterThanZero();

        System.out.println(unfinished + "@@" + finished + "@@@@@@@@@@@@@@");

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
        data.put("unfinished", unfinished);
        data.put("finished", finished);


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

    @GetMapping("/main2")
    public String main2(Model model) {
        Long female = memberRepository.countMembersBySex(Sex.FEMALE);
        Long male = memberRepository.countMembersBySex(Sex.MALE);
        Long ageTotal10 = memberRepository.countMembersByAgeRange(10, 19);
        Long ageTotal20 = memberRepository.countMembersByAgeRange(20, 29);
        Long ageTotal30 = memberRepository.countMembersByAgeRange(30, 39);
        Long ageTotal40 = memberRepository.countMembersByAgeRange(40, 49);
        Long ageTotal50 = memberRepository.countMembersByAgeRange(50, 59);
        Long ageTotal60 = memberRepository.countMembersByAgeRange(60, 69);
        Long ageTotal70 = memberRepository.countMembersByAgeRange(70, 79);

        Long january = orderRepository.countByMonth(1);
        Long february = orderRepository.countByMonth(2);
        Long march = orderRepository.countByMonth(3);
        Long april = orderRepository.countByMonth(4);
        Long may = orderRepository.countByMonth(5);
        Long june = orderRepository.countByMonth(6);
        Long july = orderRepository.countByMonth(7);
        Long august = orderRepository.countByMonth(8);
        Long september = orderRepository.countByMonth(9);
        Long october = orderRepository.countByMonth(10);
        Long november = orderRepository.countByMonth(11);
        Long december = orderRepository.countByMonth(12);

        List<Item> itemList = itemRepository.findByStockNumberLessThanEqualOrderByStockNumberAsc(5);

        Map<String, Object> data = new HashMap<>();

        data.put("female", female);
        data.put("male", male);
        data.put("ageTotal10", ageTotal10);
        data.put("ageTotal20", ageTotal20);
        data.put("ageTotal30", ageTotal30);
        data.put("ageTotal40", ageTotal40);
        data.put("ageTotal50", ageTotal50);
        data.put("ageTotal60", ageTotal60);
        data.put("ageTotal70", ageTotal70);

        data.put("january", january);
        data.put("february", february);
        data.put("march", march);
        data.put("april", april);
        data.put("may", may);
        data.put("june", june);
        data.put("july", july);
        data.put("august", august);
        data.put("september", september);
        data.put("october", october);
        data.put("november", november);
        data.put("december", december);

        data.put("itemList", itemList);

        model.addAttribute("data", data);

        return "/admin/main2";
    }

    @GetMapping("/pwCheck")
    public String pwCheck(){
        return "/admin/pwCheck";
    }
    @GetMapping("/changePw")
    public String changePw(){
        return "/admin/changePw";
    }
    @GetMapping("/changeInfo")
    public String changeInfo(){
        return "/admin/changeInfo";
    }

}
