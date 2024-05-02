package com.keduit.shop.service;

import com.keduit.shop.constant.OrderStatus;
import com.keduit.shop.dto.AdminOrderSearchDTO;
import com.keduit.shop.dto.OrderDTO;
import com.keduit.shop.dto.OrderHistDTO;
import com.keduit.shop.dto.OrderItemDTO;
import com.keduit.shop.entity.*;
import com.keduit.shop.repository.ItemImgRepository;
import com.keduit.shop.repository.ItemRepository;
import com.keduit.shop.repository.MemberRepository;
import com.keduit.shop.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.util.StringUtils;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class OrderService {

    private final ItemRepository itemRepository;
    private final MemberRepository memberRepository;
    private final OrderRepository orderRepository;
    private final ItemImgRepository itemImgRepository;

    /*주문등록*/
    public Long order(OrderDTO orderDTO, String email) {
        // 주문할 상품 조회
        Item item = itemRepository.findById(orderDTO.getItemId())
                .orElseThrow(EntityNotFoundException::new);

        // 현재 로그인한 회원의 정보 조회
        Member member = memberRepository.findByEmail(email);

        String selectedSize = orderDTO.getSize(); // 선택한 사이즈

        // 주문 상품 생성 및 재고 감소
        OrderItem orderItem = OrderItem.createOrderItem(item, orderDTO.getCount(), selectedSize); // 사이즈 전달
        List<OrderItem> orderItemList = new ArrayList<>();
        orderItemList.add(orderItem);

        // 주문 엔티티 생성 및 저장
        Order order = Order.createOrder(member, orderItemList);
        orderRepository.save(order);

        return order.getId();
    }








    @Transactional(readOnly = true)
    public Page<OrderHistDTO> getOrderList(String email, Pageable pageable) {
        /*주문내역가져오기*/
        List<Order> orders = orderRepository.findOrders(email, pageable);
        /*페이지처리를위해 가져옴*/
        Long totalCount = orderRepository.countOrder(email);

        List<OrderHistDTO> orderHistDTOs = new ArrayList<>();

        for (Order order : orders) {/*order에서 꺼냄*/
            OrderHistDTO orderHistDTO = new OrderHistDTO(order);
            List<OrderItem> orderItems = order.getOrderItems(); /*주문안에들어있는 상품들 쭉 가져옴*/
            for (OrderItem orderItem : orderItems) {
                ItemImg itemImg = itemImgRepository.findByItemIdAndRepimgYn(orderItem.getItem().getId(), "Y"); /*상품 id주면 이미지중에서 대표이미지 읽어오기*/
                OrderItemDTO orderItemDTO = new OrderItemDTO(orderItem, itemImg.getImgUrl(), orderItem.getSize()); /*대표이미지의 url이 필요함*/
                orderHistDTO.addOrderItemDTO(orderItemDTO); /*이렇게만들어진 orderItemDTO를 orderHistDTO에 담아줌*/
            }
            orderHistDTOs.add(orderHistDTO); /*그걸 list에 담아서 던져줌 읽어온 order의 갯수만큼 이작업을 반복함*/
        }
        return new PageImpl<>(orderHistDTOs, pageable, totalCount);
    }

    /*주문 취소 전 이메일로 사용자(로그인한,주문했던) 일치여부 확인*/
    @Transactional(readOnly = true)
    public boolean validateOrder(Long orderId, String email) {
        Member curMember = memberRepository.findByEmail(email);
        Order order = orderRepository.findById(orderId).orElseThrow(EntityNotFoundException::new);
        Member saveMember = order.getMember();
        if (!StringUtils.equals(curMember.getEmail(), saveMember.getEmail())) {
            return false;
        }
        return true;
    }

    public void cancelOrder(Long orderId) {
        Order order = orderRepository.findById(orderId).orElseThrow(EntityNotFoundException::new);
        order.cancelOrder();
    }

    public Long orders(List<OrderDTO> orderDTOList, String email) {
        Member member = memberRepository.findByEmail(email);
        List<OrderItem> orderItemList = new ArrayList<>();

        for (OrderDTO orderDTO : orderDTOList) {
            Item item = itemRepository.findById(orderDTO.getItemId())
                    .orElseThrow(() -> new EntityNotFoundException("상품을 찾을 수 없습니다."));

            // 올바른 사이즈 정보를 전달
            OrderItem orderItem = OrderItem.createOrderItem(item, orderDTO.getCount(), orderDTO.getSize()); // 이 부분 수정
            orderItemList.add(orderItem);
        }

        Order order = Order.createOrder(member, orderItemList);
        orderRepository.save(order);

        return order.getId();
    }


    public Page<Order> getAdminOrderPage(AdminOrderSearchDTO adminOrderSearchDTO, Pageable pageable) {
        return orderRepository.getAdminOrderPage(adminOrderSearchDTO, pageable);
    }

    @Transactional
    public void allChangeStatus(String currentState,String newState){

        OrderStatus newStatus1 = OrderStatus.valueOf(newState);
        OrderStatus currentState1 = OrderStatus.valueOf(currentState);
        // 이넘에 해당되는 문자열이 아닌경우 예외처리 하는게 좋음.

        List<Order> orderList = orderRepository.findByOrderStatus(currentState1);

        if(orderList == null){
            return;
        }

        for (Order order : orderList){
            order.setOrderStatus(newStatus1);
        }
        orderRepository.saveAll(orderList);
    }

    @Transactional(rollbackFor = Exception.class)
    public String changeStatus(String currentState, String newState, List<Long> checkedOrderIds) {

        OrderStatus currentState1 = OrderStatus.valueOf(currentState);
        OrderStatus newStatus1 = OrderStatus.valueOf(newState);


        for (Long orderId : checkedOrderIds) {
            Optional<Order> optionalOrder = orderRepository.findById(orderId);
            if(optionalOrder.isEmpty()){
                throw new IllegalArgumentException("해당 orderId의 order를 찾을 수 없습니다");
            }

            if(!optionalOrder.get().getOrderStatus().equals(currentState1)){
                throw new IllegalStateException(currentState + " 상태가 아닌 order가 존재합니다");
            }
            optionalOrder.get().setOrderStatus(newStatus1);
            orderRepository.save(optionalOrder.get());
            // 옵셔널에서 get을 하면 원래 상태인것인가?? 그렇다
        }
            return "성공!";
    }

}
