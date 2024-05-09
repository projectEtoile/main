package com.keduit.shop.controller.admin;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.keduit.shop.dto.AdminOrderSearchDTO;
import com.keduit.shop.entity.ExchangeReturnRequest;
import com.keduit.shop.entity.Order;
import com.keduit.shop.entity.QandA;
import com.keduit.shop.repository.AddressRepository;
import com.keduit.shop.repository.ExchangeReturnRequestRepository;
import com.keduit.shop.repository.OrderRepository;
import com.keduit.shop.service.OrderService;
import com.nimbusds.jose.shaded.json.JSONObject;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;
import java.util.*;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor // 싱글톤 패턴 주입받기 위한
public class AdminOrderController {

    private final OrderService orderService;
    private final AddressRepository addressRepository;
    private final ExchangeReturnRequestRepository exchangeReturnRequestRepository;
    private final OrderRepository orderRepository;

    @GetMapping({"/orders/{page}", "/orders"})
    public String orderListPage(Model model,
                                @PathVariable("page")Optional<Integer> page,
                                AdminOrderSearchDTO adminOrderSearchDTO){

        Pageable pageable = PageRequest.of(page.isPresent() ? page.get() : 0, 10);

        Page<Order> orders = orderService.getAdminOrderPage(adminOrderSearchDTO, pageable);

//        orders.getContent().get(0).getDeliveryAddress().getDetailAddress();
//        orders.getContent().get(0).getDeliveryAddress().getPostcode();

        model.addAttribute("orders", orders);
        model.addAttribute("adminOrderSearchDTO", adminOrderSearchDTO);
        model.addAttribute("maxPage", 10);


        return "admin/orderList";

    }

    @PostMapping("/allchangeStatus")
    public @ResponseBody ResponseEntity allChangeStatus(@RequestBody JSONObject requestData){
        String currentState = requestData.getAsString("currentState");
        String newState = requestData.getAsString("newState");

        return orderService.allChangeStatus(currentState,newState);
    }

    @PostMapping("/changeStatus")
    public @ResponseBody ResponseEntity changeStatus(@RequestBody JSONObject requestData){
        String currentState = requestData.getAsString("currentState");
        String newState = requestData.getAsString("newState");

        String stringCheckedOrderIds = requestData.getAsString("checkedOrderIds");

        List<Long> checkedOrderIds = new ArrayList<>();
        for (String num : stringCheckedOrderIds.replaceAll("[^0-9,]", "").split(",")) {
            checkedOrderIds.add(Long.parseLong(num.trim()));
        }

        try {
            String result = orderService.changeStatus(currentState,newState,checkedOrderIds);
        }catch (IllegalStateException e){
            return new ResponseEntity<>(e.getMessage(),HttpStatus.BAD_REQUEST);
        }
            return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/getReason")
    public @ResponseBody ResponseEntity getReason(@RequestParam("id") Long id) throws JsonProcessingException {

        Order order = orderRepository.findById(id).orElseThrow(EntityNotFoundException::new);

        ExchangeReturnRequest exchangeReturnRequest =
                exchangeReturnRequestRepository.findByOrder(order);

        String requestType = exchangeReturnRequest.getRequestType();
        String reason = exchangeReturnRequest.getReason();

        Map<String, String> data = new HashMap<>();

        data.put("requestType",requestType);
        data.put("reason",reason);
        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(data);
        System.out.println(json);
        return new ResponseEntity(json, HttpStatus.OK);
    }
}
