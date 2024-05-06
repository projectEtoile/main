package com.keduit.shop.controller;

import com.keduit.shop.dto.CartDetailDTO;
import com.keduit.shop.dto.CartItemDTO;
import com.keduit.shop.dto.CartOrderDTO;
import com.keduit.shop.entity.Address;
import com.keduit.shop.repository.AddressRepository;
import com.keduit.shop.repository.MemberRepository;
import com.keduit.shop.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class CartController {
    private final CartService cartService;
    private final AddressRepository addressRepository;
    private final MemberRepository memberRepository;


    /*ResponseBody한다는건 데이터를넘겨주겠다는것*/
    @PostMapping("/cart")
    public ResponseEntity order(@Valid @RequestBody CartItemDTO cartItemDTO, BindingResult bindingResult, Principal principal) {
        if (bindingResult.hasErrors()) { /*bindingResult가 error가있는가? 확인*/
            StringBuilder sb = new StringBuilder();
            List<FieldError> fieldErrorList = bindingResult.getFieldErrors();
            for (FieldError fieldError : fieldErrorList) {
                sb.append(fieldError.getDefaultMessage());
            }
            return new ResponseEntity(sb.toString(), HttpStatus.BAD_REQUEST); /*에러가났다는것에대한 내용을 전달함*/
        }

        /*에러안난상황*/
        String email = principal.getName();
        Long cartItemId;

        try {
            cartItemId = cartService.addCart(cartItemDTO, email);
        } catch (Exception e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity(cartItemId, HttpStatus.OK);
    }

    @GetMapping("/cart")
    public String orderHist(Principal principal, Model model) {
        String email = principal.getName(); // 로그인한 사용자의 이메일
        List<CartDetailDTO> cartDetailDTOList = cartService.getCartList(email);

        if (cartDetailDTOList == null || cartDetailDTOList.isEmpty()) {
            model.addAttribute("cartItems", new ArrayList<>()); // 기본 빈 리스트 추가
        } else {
            model.addAttribute("cartItems", cartDetailDTOList); // 데이터가 존재하면 추가
        }

        // 카트에 있는 총 상품 항목의 수를 모델에 추가
        int totalCartItemCount = cartService.getTotalCartItemCount(email);
        model.addAttribute("totalCartItemCount", totalCartItemCount);

        return "cart/cartList"; // 반환되는 뷰 이름 확인
    }


    /*put : 전체수정, patch: 일부수정*/
    @PatchMapping("/cartItem/{cartItemId}")
    public @ResponseBody ResponseEntity updateCartItem(@PathVariable("cartItemId") Long cartItemId,
                                                       int count, Principal principal) {
        if (count <= 0) {
            return new ResponseEntity("최소 1개 이상 담아주세요.", HttpStatus.BAD_REQUEST);
        } else if (!cartService.validateCartItem(cartItemId, principal.getName())) {
            return new ResponseEntity("수정 권한이 없습니다.", HttpStatus.FORBIDDEN);
        }
        cartService.updateCartItemCount(cartItemId, count);
        return new ResponseEntity(cartItemId, HttpStatus.OK);
    }

    @DeleteMapping("/cartItem/{cartItemId}")
    public @ResponseBody ResponseEntity deleteCartItem(@PathVariable("cartItemId") Long cartItemId, Principal principal) {
        if(!cartService.validateCartItem(cartItemId,principal.getName())){
            return new ResponseEntity("수정 권한이 없습니다.", HttpStatus.FORBIDDEN);
        }
        cartService.deleteCartItem(cartItemId);
        return new ResponseEntity(cartItemId, HttpStatus.OK);
    }

    @PostMapping("/cart/orders")
    public ResponseEntity<?> orderCartItem(@RequestBody CartOrderDTO cartOrderDTO, Principal principal) {
        List<CartOrderDTO> cartOrderDTOList = cartOrderDTO.getCartOrderDTOList();

        if(addressRepository.findAllByMember(memberRepository.findByEmail(principal.getName())).isEmpty()){
            return ResponseEntity.badRequest().body("기본 주소지를 설정해주세요.");
        }

        if (cartOrderDTOList == null || cartOrderDTOList.isEmpty()) {
            return ResponseEntity.badRequest().body("주문할 상품을 선택해주세요.");
        }

        List<Long> orderIds = new ArrayList<>();
        for (CartOrderDTO cartOrder : cartOrderDTOList) {
            if (!cartService.validateCartItem(cartOrder.getCartItemId(), principal.getName())) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("주문 권한이 없습니다.");
            }

            // 주문 처리만 하고 재고는 여기서 감소시키도록 수정
            Long orderId = cartService.orderCartItem(cartOrder, principal.getName());
            orderIds.add(orderId);

            // 주문 처리 후에 각 상품에 대해 재고를 감소시킴
            cartService.decreaseItemStock(cartOrder.getCartItemId(), principal.getName());
        }
        return ResponseEntity.ok(orderIds);
    }





    @GetMapping("/cart/totalItemCount")
    public String getTotalCartItemCount(Principal principal, Model model) {
        String email = principal.getName();
        int totalCartItemCount = cartService.getTotalCartItemCount(email);
        model.addAttribute("totalCartItemCount", totalCartItemCount);
        return "cart/totalItemCount"; // 총 상품 항목 수를 보여줄 HTML 페이지의 이름
    }

}
