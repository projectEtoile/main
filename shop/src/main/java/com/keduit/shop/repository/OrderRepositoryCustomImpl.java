package com.keduit.shop.repository;

import com.keduit.shop.dto.AdminOrderSearchDTO;
import com.keduit.shop.entity.Order;
import com.keduit.shop.entity.QOrder;
import com.querydsl.core.types.dsl.Wildcard;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import javax.persistence.EntityManager;
import java.util.List;

public class OrderRepositoryCustomImpl implements OrderRepositoryCustom{

    private JPAQueryFactory jpaQueryFactory;
    public OrderRepositoryCustomImpl(EntityManager em){
        this.jpaQueryFactory = new JPAQueryFactory(em);
    }

    @Override
    public Page<Order> getAdminOrderPage(AdminOrderSearchDTO adminOrderSearchDTO, Pageable pageable) {

        List<Order> result = jpaQueryFactory
                .selectFrom(QOrder.order)
                .where()
                .orderBy(QOrder.order.id.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        long total = jpaQueryFactory
                .select(Wildcard.count)
                .from(QOrder.order)
                .where()
                .fetchOne();

        return new PageImpl<>(result, pageable, total);
    }
}
