package com.keduit.shop.repository;

import com.keduit.shop.constant.OrderStatus;
import com.keduit.shop.dto.AdminOrderSearchDTO;
import com.keduit.shop.entity.Member;
import com.keduit.shop.entity.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long>,OrderRepositoryCustom {

    /*JPQL은 @Query에 기술하는데 select 대상이 객체임 Order*/
    @Query("select o from Order o " +
            "where o.member.email = :email " +
            "order by o.orderDate desc")
    List<Order> findOrders(@Param("email") String email, Pageable pageable);

    @Query("select count(o) from Order o where o.member.email = :email")
    Long countOrder(@Param("email") String email);

    List<Order> findByOrderStatus(OrderStatus orderStatus);

    List<Order> findByMemberAndOrderStatus(Member member, OrderStatus orderStatus);

/*    @Query(value = "SELECT oi.item_id as itemId, SUM(oi.count) as totalCount FROM order_item oi GROUP BY oi.item_id ORDER BY totalCount DESC LIMIT 10", nativeQuery = true)
    List<Map<String, Object>> findTop10ItemsOrderedByCount();*/

    @Query(value = "SELECT oi.item_id as itemId, SUM(oi.count) as totalCount " +
            "FROM order_item oi " +
            "JOIN orders o ON oi.order_id = o.order_id " +
            "WHERE o.order_status = 'DELIVERED' " +
            "GROUP BY oi.item_id " +
            "ORDER BY totalCount DESC " +
            "LIMIT 10",
            nativeQuery = true)
    List<Map<String, Object>> findTop10ItemsOrderedByCount();

    @Query(value = "SELECT oi.item_id as itemId " +
            "FROM order_item oi " +
            "JOIN orders o ON oi.order_id = o.order_id " +
            "WHERE o.order_status = 'DELIVERED' " +
            "GROUP BY oi.item_id " +
            "ORDER BY COUNT(oi.item_id) DESC " +
            "LIMIT 10",
            nativeQuery = true)
    List<Long> findTop10ItemIdsOrderedByCount();

    @Query("SELECT SUM(oi.count * i.price) " +
            "FROM Order o " +
            "JOIN o.orderItems oi " +
            "JOIN oi.item i " +
            "WHERE o.orderStatus = 'DELIVERED' " +
            "AND i.level1 = :level1")
    Optional<Integer> getTotalPriceByLevel1(@Param("level1") String level1);

}
