package com.keduit.shop.repository;

import com.keduit.shop.constant.OrderStatus;
import com.keduit.shop.dto.AdminOrderSearchDTO;
import com.keduit.shop.entity.Order;
import com.keduit.shop.entity.QOrder;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Wildcard;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.thymeleaf.util.StringUtils;

import javax.persistence.EntityManager;
import java.time.LocalDateTime;
import java.util.List;

public class OrderRepositoryCustomImpl implements OrderRepositoryCustom{

    private JPAQueryFactory jpaQueryFactory;
    public OrderRepositoryCustomImpl(EntityManager em){
        this.jpaQueryFactory = new JPAQueryFactory(em);
    }
    private BooleanExpression regDtsAfter(String searchDateType) {
        LocalDateTime dateTime = LocalDateTime.now();
        if (StringUtils.equals("all", searchDateType) || searchDateType == null) {
            return null;
        } else if (StringUtils.equals("1d", searchDateType)) {
            dateTime = dateTime.minusDays(1);
        } else if (StringUtils.equals("1w", searchDateType)) {
            dateTime = dateTime.minusWeeks(1);
        } else if (StringUtils.equals("1m", searchDateType)) {
            dateTime = dateTime.minusMonths(1);
        } else if (StringUtils.equals("6m", searchDateType)) {
            dateTime = dateTime.minusMonths(6);
        }
        return QOrder.order.regTime.after(dateTime);
    }
    private BooleanExpression searchByEq(String searchBy, String searchQuery) {
        if (StringUtils.equals("email", searchBy)) {

            if(searchQuery.length() == 0 ){
                return null;
            }
            return QOrder.order.member.email.eq(searchQuery);

        } else if (StringUtils.equals("itemId", searchBy)) {

            if(searchQuery.length() == 0 ){
                return null;
            }
            return QOrder.order.orderItems.any().item.id.eq(Long.parseLong(searchQuery));
        }
        return null;
    }

    private BooleanExpression searchOrderStatuEq(OrderStatus orderStatus){
        return orderStatus == null? null: QOrder.order.orderStatus.eq(orderStatus);
    }


    @Override
    public Page<Order> getAdminOrderPage(AdminOrderSearchDTO adminOrderSearchDTO, Pageable pageable) {

        List<Order> result = jpaQueryFactory
                .selectFrom(QOrder.order)
                .where(regDtsAfter(adminOrderSearchDTO.getSearchDateType()),
                        searchByEq(adminOrderSearchDTO.getSearchBy(), adminOrderSearchDTO.getSearchQuery()),
                        searchOrderStatuEq(adminOrderSearchDTO.getOrderStatus()))
                .orderBy(QOrder.order.id.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        long total = jpaQueryFactory
                .select(Wildcard.count)
                .from(QOrder.order)
                .where(regDtsAfter(adminOrderSearchDTO.getSearchDateType()),
                        searchByEq(adminOrderSearchDTO.getSearchBy(), adminOrderSearchDTO.getSearchQuery()),
                        searchOrderStatuEq(adminOrderSearchDTO.getOrderStatus()))
                .fetchOne();

        return new PageImpl<>(result, pageable, total);
    }
}
