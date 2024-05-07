package com.keduit.shop.repository;

import com.keduit.shop.dto.AdminMemberSearchDTO;
import com.keduit.shop.entity.Member;
import com.keduit.shop.entity.QMember;
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

public class MemberRepositoryCustomImpl implements MemberRepositoryCustom{

    private JPAQueryFactory jpaQueryFactory;

    public MemberRepositoryCustomImpl(EntityManager em){
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
        return QMember.member.regTime.after(dateTime);
    }

    private BooleanExpression searchByEq(String searchBy, String searchQuery) {
        if (StringUtils.equals("name", searchBy)) { // 직접 url 입력시 Nm 이 NM으로 바뀌는 현상있음.
            if(searchQuery.length() == 0 ){
                return null;
            }
            return QMember.member.name.eq(searchQuery);
        } else if (StringUtils.equals("memberId", searchBy)) {
            if(searchQuery.length() == 0 ){
                return null;
            }
            return QMember.member.id.eq(Long.parseLong(searchQuery));
        }else if (StringUtils.equals("email", searchBy)) {
            if(searchQuery.length() == 0 ){
                return null;
            }
            return QMember.member.email.eq(searchQuery);
        }

        return null;
    }


    @Override
    public Page<Member> getAdminMemberPage(AdminMemberSearchDTO adminMemberSearchDTO, Pageable pageable) {


        List<Member> result = jpaQueryFactory
                .selectFrom(QMember.member)
                .where(regDtsAfter(adminMemberSearchDTO.getSearchDateType()),
                        searchByEq(adminMemberSearchDTO.getSearchBy(), adminMemberSearchDTO.getSearchQuery()))
                .orderBy(QMember.member.id.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        long total = jpaQueryFactory
                .select(Wildcard.count)
                .from(QMember.member)
                .where(regDtsAfter(adminMemberSearchDTO.getSearchDateType()),
                        searchByEq(adminMemberSearchDTO.getSearchBy(), adminMemberSearchDTO.getSearchQuery()))
                .fetchOne();


        return new PageImpl<>(result, pageable, total);
    }
}
