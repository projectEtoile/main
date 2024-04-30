package com.keduit.shop.repository;

import com.keduit.shop.dto.AdminMemberSearchDTO;
import com.keduit.shop.entity.Member;
import com.keduit.shop.entity.QMember;
import com.querydsl.core.types.dsl.Wildcard;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import javax.persistence.EntityManager;
import java.util.List;

public class MemberRepositoryCustomImpl implements MemberRepositoryCustom{

    private JPAQueryFactory jpaQueryFactory;

    public MemberRepositoryCustomImpl(EntityManager em){
        this.jpaQueryFactory = new JPAQueryFactory(em);
    }

    @Override
    public Page<Member> getAdminMemberPage(AdminMemberSearchDTO adminMemberSearchDTO, Pageable pageable) {

        System.out.println("adminMemberSearchDTO-----> " + adminMemberSearchDTO);

        List<Member> result = jpaQueryFactory
                .selectFrom(QMember.member)
                .where()
                .orderBy(QMember.member.id.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        long total = jpaQueryFactory
                .select(Wildcard.count)
                .from(QMember.member)
                .where()
                .fetchOne();

        System.out.println("#### 총 검색 수 : " +total );

        return new PageImpl<>(result, pageable, total);
    }
}
