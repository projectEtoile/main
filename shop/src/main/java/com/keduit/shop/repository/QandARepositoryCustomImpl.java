package com.keduit.shop.repository;

import com.keduit.shop.constant.ItemSellStatus;
import com.keduit.shop.dto.AdminQNASearchDTO;
import com.keduit.shop.entity.*;
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

public class QandARepositoryCustomImpl implements QandARepositoryCustom {

    private JPAQueryFactory jpaQueryFactory;

    public QandARepositoryCustomImpl(EntityManager em) {
        this.jpaQueryFactory = new JPAQueryFactory(em);
    }


    private BooleanExpression regDtsAfter(String searchDateType) {
        LocalDateTime dateTime = LocalDateTime.now();
        if (StringUtils.equals("all", searchDateType) || searchDateType == null) {
            return null;
        } else if (StringUtils.equals("1d", searchDateType)) {
            dateTime = dateTime.minusDays(1); // 지금 시간에서 -24시간. 이후에도 이런식으로 .
        } else if (StringUtils.equals("1w", searchDateType)) {
            dateTime = dateTime.minusWeeks(1);
        } else if (StringUtils.equals("1m", searchDateType)) {
            dateTime = dateTime.minusMonths(1);
        } else if (StringUtils.equals("6m", searchDateType)) {
            dateTime = dateTime.minusMonths(6);
        }
        return QQandA.qandA.regTime.after(dateTime);
    }

    private BooleanExpression searchByEq(String searchBy, String searchQuery) {
        if (StringUtils.equals("itemId", searchBy)) { // 직접 url 입력시 Nm 이 NM으로 바뀌는 현상있음.
            if (searchQuery.length() == 0) {
                return null;
            }
            return QQandA.qandA.item.id.eq(Long.parseLong(searchQuery));
        } else if (StringUtils.equals("memberId", searchBy)) {
            if (searchQuery.length() == 0) {
                return null;
            }
            return QQandA.qandA.member.id.eq(Long.parseLong(searchQuery));
        } else if (StringUtils.equals("email", searchBy)) {
            if (searchQuery.length() == 0) {
                return null;
            }
            return QQandA.qandA.member.email.eq(searchQuery);
        }

        return null;
    }

    private BooleanExpression qNaAnswerStatus(String answerStatus) {
        if (answerStatus == null) {
            return null;
        }
        if (answerStatus.equals("unfinished")) {
            return QQandA.qandA.answer.length().eq(0);
        } else if (answerStatus.equals("finished")) {
            return QQandA.qandA.answer.isNotEmpty();
        } else
            return null;
    }

    @Override
    public Page<QandA> getAdminQandAListPage(AdminQNASearchDTO adminQNASearchDTO, Pageable pageable) {

        List<QandA> result = jpaQueryFactory
                .selectFrom(QQandA.qandA)
                .where(regDtsAfter(adminQNASearchDTO.getSearchDateType()),
                        searchByEq(adminQNASearchDTO.getSearchBy(), adminQNASearchDTO.getSearchQuery()),
                        qNaAnswerStatus(adminQNASearchDTO.getAnswerStatus())
                )
                .orderBy(QQandA.qandA.id.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        long total = jpaQueryFactory
                .select(Wildcard.count)
                .from(QQandA.qandA)
                .where(regDtsAfter(adminQNASearchDTO.getSearchDateType()),
                        searchByEq(adminQNASearchDTO.getSearchBy(), adminQNASearchDTO.getSearchQuery()),
                        qNaAnswerStatus(adminQNASearchDTO.getAnswerStatus())
                )
                .fetchOne();


        return new PageImpl<>(result, pageable, total);
    }
}
