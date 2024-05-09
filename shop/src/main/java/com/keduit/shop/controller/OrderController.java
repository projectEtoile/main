package com.keduit.shop.controller;

import com.keduit.shop.constant.OrderStatus;
import com.keduit.shop.dto.OrderDTO;
import com.keduit.shop.dto.OrderHistDTO;
import com.keduit.shop.entity.Address;
import com.keduit.shop.entity.Member;
import com.keduit.shop.repository.AddressRepository;
import com.keduit.shop.repository.MemberRepository;
import com.keduit.shop.service.OrderService;
import lombok.RequiredArgsConstructor;
import netscape.javascript.JSObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;
import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Controller
@RequiredArgsConstructor /*주입받는다는거임*/
public class OrderController {

    private final OrderService orderService;
    private final MemberRepository memberRepository;
    private final AddressRepository addressRepository;

    @PostMapping("/order")
    public @ResponseBody ResponseEntity order(@RequestBody @Valid OrderDTO orderDTO,
                                              BindingResult bindingResult, Principal principal) {
        /*비동기 처리를 위해 @RequestBody, @ResponseBody를 사용*/
        /*@ResponseBody : 자바 객체를 HTTP요청의 Body로 전달*/
        /*@RequestBody : HTTP요청의 본문 Body에 담긴 내용을 자바 객체로 전달*/


        /*주문정보를 받는 orderDTO객체에 데이터 바인딩 할때 에러가 있는지 검사*/
        if (bindingResult.hasErrors()) {
            StringBuilder sb = new StringBuilder();
            List<FieldError> fieldErrors = bindingResult.getFieldErrors();
            for (FieldError fieldError : fieldErrors) {
                sb.append(fieldError.getDefaultMessage());
            }
            return new ResponseEntity<String>(sb.toString(), HttpStatus.BAD_REQUEST); /*responsbody사용하는이유임*/
        }
        /*principal: @Controller에노테이션이 선언된 클래스에서 메서드 인자로 principal을 넘겨줄 경우 해당 객체에 직접 접근이 가능하며
         * principal객체에서 현재 로그인한 회원의 이메일 정보를 조회함*/
        String email = principal.getName();
        Long orderId;
        try {
            /*화면에서 넘어온 주문정보와 이메일정보를 이용하여 주문로직을 가진 service를 호출함*/
            orderId = orderService.order(orderDTO, email); /*DTO는 화면이잖아*/
        } catch (Exception e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
        /*결과 값으로 생성된 주문번호와 요청이 성공임을 나타내는 Http응답을 반환*/
        return new ResponseEntity(orderId, HttpStatus.OK);
    }

    /*order는 한건조회고 orders만들거임*/
    /*왜 get일까? 그냥 select해오는거니까.*/
    @GetMapping({"/orders", "/orders/{page}"})
    public String orderHist(@PathVariable("page") Optional<Integer> page, Principal principal, Model model) {
        Pageable pageable = PageRequest.of(page.isPresent() ? page.get() : 0, 4); /*있으면 페이지가져오고 없으면0 한페이지에 4개만*/
        Page<OrderHistDTO> orderHistDTOList = orderService.getOrderList(principal.getName(), pageable);
        model.addAttribute("orders", orderHistDTOList);
        model.addAttribute("page", pageable.getPageNumber());
        model.addAttribute("maxPage", 5); /*5개페이지를 한화면에 보여줄거야*/
        return "order/orderHist";
    }

    @PostMapping("/order/{orderId}/cancel")
    public ResponseEntity cancelOrder(@PathVariable("orderId") Long orderId, Principal principal) {
        // 주문 취소 권한 검사
        if (!orderService.validateOrder(orderId, principal.getName())) {
            return new ResponseEntity<>("주문 취소 권한이 없습니다.", HttpStatus.FORBIDDEN);
        }

        // 주문 취소 상태 확인
        OrderStatus orderStatus = orderService.getOrderStatus(orderId);
        if (orderStatus != OrderStatus.ORDER) {
            return new ResponseEntity<>("주문을 취소할 수 없는 상태입니다.", HttpStatus.BAD_REQUEST);
        }

        // 주문 취소 처리
        boolean isCancelled = orderService.cancelOrder(orderId);
        if (!isCancelled) {
            return new ResponseEntity<>("주문 취소 중 오류가 발생했습니다.", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>("주문이 성공적으로 취소되었습니다.", HttpStatus.OK);
    }


    @GetMapping("/payment")
    public @ResponseBody ResponseEntity order(Principal principal) {

        Member member =  memberRepository.findByEmail(principal.getName());
        if(addressRepository.findByMemberAndSelectAddressTrue(member).isEmpty()){
            String notFound = "notFound";
            return new ResponseEntity("{\"message\": \"notFound\"}",HttpStatus.OK);
        }
        Address address = addressRepository.findByMemberAndSelectAddressTrue(member).orElseThrow(EntityNotFoundException::new);

        Map<String, Object> responseData = new HashMap<>();
        responseData.put("member", member);
        responseData.put("address", address);

        return new ResponseEntity<>(responseData, HttpStatus.OK);
    }
}
