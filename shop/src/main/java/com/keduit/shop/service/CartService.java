package com.keduit.shop.service;

import com.keduit.shop.dto.CartDetailDTO;
import com.keduit.shop.dto.CartItemDTO;
import com.keduit.shop.dto.CartOrderDTO;
import com.keduit.shop.dto.OrderDTO;
import com.keduit.shop.entity.Cart;
import com.keduit.shop.entity.CartItem;
import com.keduit.shop.entity.Item;
import com.keduit.shop.entity.Member;
import com.keduit.shop.repository.CartItemRepository;
import com.keduit.shop.repository.CartRepository;
import com.keduit.shop.repository.ItemRepository;
import com.keduit.shop.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.util.StringUtils;

import javax.persistence.EntityNotFoundException;
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

    /*장바구니 추가하기*/
    public Long addCart(CartItemDTO cartItemDTO, String email) {
        Item item = itemRepository.findById(cartItemDTO.getItemId()).orElseThrow(EntityNotFoundException::new);
        Member member = memberRepository.findByEmail(email);

        Cart cart = cartRepository.findByMemberId(member.getId());
        if (cart == null) {
            cart = Cart.createCart(member);
            cartRepository.save(cart);
        }

        CartItem existingCartItem = cartItemRepository.findByCartIdAndItemIdAndSize(
                cart.getId(), item.getId(), cartItemDTO.getSize()
        );

        if (existingCartItem != null) {
            existingCartItem.addCount(cartItemDTO.getCount());
            cartItemRepository.save(existingCartItem); // 수량 업데이트 후 저장
            return existingCartItem.getId();
        } else {
            CartItem newCartItem = CartItem.createCartItem(
                    cart, item, cartItemDTO.getCount(), cartItemDTO.getSize() // 사이즈 추가
            );
            cartItemRepository.save(newCartItem); // 새 카트 아이템 저장
            return newCartItem.getId();
        }
    }




    /*목록읽어오기*/
    @Transactional(readOnly = true)
    public List<CartDetailDTO> getCartList(String email) {
        Member member = memberRepository.findByEmail(email);

        if (member == null) {
            // 로그 추가
            System.out.println("Member not found for email: " + email);
            return new ArrayList<>(); // 멤버를 찾지 못하면 빈 리스트 반환
        }

        Cart cart = cartRepository.findByMemberId(member.getId());

        if (cart == null) {
            // 로그 추가
            System.out.println("Cart not found for member id: " + member.getId());
            return new ArrayList<>(); // 카트가 없으면 빈 리스트 반환
        }

        List<CartDetailDTO> cartDetailDTOList = cartItemRepository.findCartDetailDTOList(cart.getId());

        if (cartDetailDTOList == null || cartDetailDTOList.isEmpty()) {
            // 로그 추가
            System.out.println("CartDetailDTO list is empty for cart id: " + cart.getId());
            return new ArrayList<>(); // 결과가 없거나 null인 경우 빈 리스트 반환
        }

        return cartDetailDTOList; // 결과가 있는 경우 반환
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

    public Long orderCartItem(List<CartOrderDTO> cartOrderDTOList, String email) {

        List<OrderDTO> orderDTOList = new ArrayList<>();
        for(CartOrderDTO cartOrderDto :cartOrderDTOList){
            CartItem cartItem = cartItemRepository.findById(cartOrderDto.getCartItemId())
                    .orElseThrow(EntityNotFoundException::new);
            OrderDTO orderDTO = new OrderDTO();
            orderDTO.setItemId(cartItem.getItem().getId());
            orderDTO.setCount(cartItem.getCount());
            orderDTOList.add(orderDTO);
        }
//     주문 로직을 호출하여 처리
        Long orderId = orderService.orders(orderDTOList, email);

//    주문이 완료된 상품을 장바구니에서 삭제하기
        for (CartOrderDTO cartOrderDTO: cartOrderDTOList){
            CartItem cartItem = cartItemRepository.findById(cartOrderDTO.getCartItemId())
                    .orElseThrow(EntityNotFoundException::new);
            cartItemRepository.delete(cartItem);
        }

        return orderId;
    }

}

