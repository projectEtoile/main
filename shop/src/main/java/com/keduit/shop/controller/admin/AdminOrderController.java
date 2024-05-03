package com.keduit.shop.controller.admin;

import com.keduit.shop.dto.AdminOrderSearchDTO;
import com.keduit.shop.entity.Order;
import com.keduit.shop.repository.AddressRepository;
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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor // 싱글톤 패턴 주입받기 위한
public class AdminOrderController {

    private final OrderService orderService;
    private final AddressRepository addressRepository;

    @GetMapping({"/orders/{page}", "/orders"})
    public String orderListPage(Model model,
                                @PathVariable("page")Optional<Integer> page,
                                AdminOrderSearchDTO adminOrderSearchDTO){

        Pageable pageable = PageRequest.of(page.isPresent() ? page.get() : 0, 10);

        Page<Order> orders = orderService.getAdminOrderPage(adminOrderSearchDTO, pageable);


        model.addAttribute("orders", orders);
        model.addAttribute("adminOrderSearchDTO", adminOrderSearchDTO);
        model.addAttribute("maxPage", 10);


        return "admin/orderList";

    }

    @PostMapping("/allchangeStatus")
    public @ResponseBody ResponseEntity allChangeStatus(@RequestBody JSONObject requestData){
        String currentState = requestData.getAsString("currentState");
        String newState = requestData.getAsString("newState");
        orderService.allChangeStatus(currentState,newState);
        return new ResponseEntity<>(HttpStatus.OK);
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
}
