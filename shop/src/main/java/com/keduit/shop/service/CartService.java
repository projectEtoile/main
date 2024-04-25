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
        /*장바구니에 넣을 상품 조회*/
        Item item = itemRepository.findById(cartItemDTO.getItemId()).orElseThrow(EntityNotFoundException::new);
        /*로그인 한 회원 엔티티 조회*/
        Member member = memberRepository.findByEmail(email);
        /*현재 회원의 장바구니가 있는지 조회*/
        Cart cart = cartRepository.findByMemberId(member.getId());
        /*카트(장바구니)가있는지없는지확인 없으면 생성하고 있으면 수량증가*/
        if (cart == null) {
            cart = Cart.createCart(member);
            cartRepository.save(cart);
        }
        /*장바구니에 상품이 들어있는지 확인 후 있으면 수량add 없으면 장바구니상품추가(save)*/
        CartItem saveCartItem = cartItemRepository.findByCartIdAndItemId(cart.getId(), item.getId());/*장바구니에 상품이있는지없는지확인*/
        if (saveCartItem != null) {
            saveCartItem.addCount(cartItemDTO.getCount()); /*이미 장바구니에 상품이 있으면 수량만증가*/
            return saveCartItem.getId();
        } else {
            CartItem cartItem = CartItem.createCartItem(cart, item, cartItemDTO.getCount());/*없으면 생성*/
            cartItemRepository.save(cartItem);
            return cartItem.getId();
        }
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

