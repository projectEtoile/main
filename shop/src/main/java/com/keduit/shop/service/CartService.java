package com.keduit.shop.service;

import com.keduit.shop.constant.OrderStatus;
import com.keduit.shop.dto.CartDetailDTO;
import com.keduit.shop.dto.CartItemDTO;
import com.keduit.shop.dto.CartOrderDTO;
import com.keduit.shop.dto.OrderDTO;
import com.keduit.shop.entity.*;
import com.keduit.shop.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.util.StringUtils;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service/*서비스라고알려주기위해*/
@RequiredArgsConstructor /*주입받기위해*/
@Transactional
public class CartService {
    private final MemberRepository memberRepository;
    private final ItemRepository itemRepository;
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final OrderService orderService;
    private final OrderRepository orderRepository;

    /*장바구니 추가하기*/
    public Long addCart(CartItemDTO cartItemDTO, String email) {
        Item item = itemRepository.findById(cartItemDTO.getItemId()).orElseThrow(EntityNotFoundException::new);
        Member member = memberRepository.findByEmail(email);
        Cart cart = cartRepository.findByMemberId(member.getId());

        if (cart == null) {
            cart = Cart.createCart(member);
            cartRepository.save(cart);
        }

        CartItem cartItem = cartItemRepository.findByCartIdAndItemIdAndSize(cart.getId(), item.getId(), cartItemDTO.getSize());
        if (cartItem != null) {
            cartItem.addCount(cartItemDTO.getCount()); // 기존 수량에 새로운 수량 추가
            cartItemRepository.save(cartItem); // 변경된 내용 저장
        } else {
            cartItem = CartItem.createCartItem(cart, item, cartItemDTO.getCount(), cartItemDTO.getSize());
            cartItemRepository.save(cartItem);
        }

        return cartItem.getId();
    }



    /*목록읽어오기*/
    @Transactional(readOnly = true)
    public List<CartDetailDTO> getCartList(String email) {
        List<CartDetailDTO> cartDetailDTOList = new ArrayList<>();

        Member member = memberRepository.findByEmail(email);
        Cart cart = cartRepository.findByMemberId(member.getId());
        if (cart == null) {
            return cartDetailDTOList;
        }

        cartDetailDTOList =
                cartItemRepository.findCartDetailDTOList(cart.getId());
        return cartDetailDTOList;
    }


    /*현재 로그인 한 회원과 장바구니에 상품을 저장한 회원을 체크하여 같으면 true 다르면 false*/
    @Transactional(readOnly = true)
    public boolean validateCartItem(Long cartItemId, String email) {
        Member member = memberRepository.findByEmail(email);
        CartItem cartItem = cartItemRepository.findById(cartItemId).orElseThrow(EntityNotFoundException::new);
        Member savedMember = cartItem.getCart().getMember();
        if (!StringUtils.equals(member.getEmail(), savedMember.getEmail())) {
            return false;
        }
        return true;
    }

    public void updateCartItemCount(Long cartItemId, int count) {
        CartItem cartItem = cartItemRepository.findById(cartItemId).orElseThrow(EntityNotFoundException::new);
        cartItem.updateCount(count);
    }

    public void deleteCartItem(Long cartItemId) {
        CartItem cartItem = cartItemRepository.findById(cartItemId).orElseThrow(EntityNotFoundException::new);
        cartItemRepository.delete(cartItem);
    }

    @Transactional
    public Long orderCartItem(CartOrderDTO cartOrderDTO, String email) {
        CartItem cartItem = cartItemRepository.findById(cartOrderDTO.getCartItemId())
                .orElseThrow(() -> new EntityNotFoundException("Cart item not found for ID: " + cartOrderDTO.getCartItemId()));

        Member member = memberRepository.findByEmail(email);
        if (member == null) {
            throw new IllegalStateException("Member not found for email: " + email);
        }

        Order order = new Order();
        order.setMember(member);
        order.setOrderDate(LocalDateTime.now());
        order.setOrderStatus(OrderItem.OrderStatus.ORDER);

        int totalPrice = cartItem.getPrice() * cartOrderDTO.getCount();
        OrderItem orderItem = new OrderItem();
        orderItem.setItem(cartItem.getItem());
        orderItem.setCount(cartOrderDTO.getCount());
        orderItem.setOrderPrice(totalPrice);
        order.addOrderItem(orderItem);

        orderRepository.save(order);
        cartItemRepository.delete(cartItem); // 주문 후 장바구니 항목 삭제

        return order.getId();
    }


    @Transactional(readOnly = true)
    public int getTotalCartItemCount(String email) {
        Member member = memberRepository.findByEmail(email);
        Cart cart = cartRepository.findByMemberId(member.getId());
        if (cart == null) {
            return 0;
        }
        return cart.getCartItems().size();
    }


    @Transactional
    public void orderAndRemoveCartItems(List<CartOrderDTO> cartOrderDTOList, String email) {
        Member member = memberRepository.findByEmail(email);
        if (member == null) {
            throw new IllegalStateException("Member not found for email: " + email);
        }

        for (CartOrderDTO cartOrderDTO : cartOrderDTOList) {
            CartItem cartItem = cartItemRepository.findById(cartOrderDTO.getCartItemId())
                    .orElseThrow(() -> new EntityNotFoundException("Cart item not found for ID: " + cartOrderDTO.getCartItemId()));

            Order order = new Order();
            order.setMember(member);
            order.setOrderDate(LocalDateTime.now());
            order.setOrderStatus(OrderItem.OrderStatus.ORDER);

            int totalPrice = cartItem.getPrice() * cartOrderDTO.getCount();
            OrderItem orderItem = new OrderItem();
            orderItem.setItem(cartItem.getItem());
            orderItem.setCount(cartOrderDTO.getCount());
            orderItem.setOrderPrice(totalPrice);
            order.addOrderItem(orderItem);

            orderRepository.save(order);
            cartItemRepository.delete(cartItem); // 주문 후 장바구니 항목 삭제
        }
    }



}


