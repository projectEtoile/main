package com.keduit.shop.repository;


import com.keduit.shop.constant.ItemSellStatus;
import com.keduit.shop.dto.AdminItemSearchDTO;
import com.keduit.shop.dto.ItemSearchDTO;
import com.keduit.shop.dto.MainItemDTO;
import com.keduit.shop.dto.QMainItemDTO;
import com.keduit.shop.entity.Item;
import com.keduit.shop.entity.QItem;
import com.keduit.shop.entity.QItemImg;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.Wildcard;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.thymeleaf.util.StringUtils;

import javax.persistence.EntityManager;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

public class ItemRepositoryCustomImpl implements ItemRepositoryCustom {
    // 쿼리팩토리를 활용하기 위한 동적쿼리 메소드 저장소

    private JPAQueryFactory jpaQueryFactory;

    // 생성자를 통해 엔티티메니저를 받아옴. 동적쿼리를 생성하기 위해선 엔티티메니저가 필요하다.
    public ItemRepositoryCustomImpl(EntityManager em) {
        this.jpaQueryFactory = new JPAQueryFactory(em);
    }

    private List<String> level1List = Arrays.asList("Top", "Outer", "Pants", "Skirt/Dress", "Shoes");

    private BooleanExpression regDtsAfter(String searchDateType) {
        LocalDateTime dateTime = LocalDateTime.now();
        if (StringUtils.equals("all", searchDateType) || searchDateType == null) {
            // searchDAteType 이 all과 같거나 null값인경우! null을 리턴하자. 그럼 쿼리문을 생성하지 않는가?
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
        return QItem.item.regTime.after(dateTime);
        // QueryDSL에서 사용되는 조건 표현식
        // item 에서 regTime이 datetime의 값 이후인 겨우에만 검색하겠다는 조건 표현식.
        // 이것을 활용하여 조건을 추가할 수 있다. 어떻게 하는가?
        // 쿼리dsl은 이런식이다. 각 조건에 맞는 경우를 하나 하나 수집하여 최종 조건들로 쿼리를 날려 원하는값을 추출.
    }

    private BooleanExpression searchByLikeOrEq(String searchBy, String searchQuery) {
        if (StringUtils.equals("itemNm", searchBy)) { // 직접 url 입력시 Nm 이 NM으로 바뀌는 현상있음.
            return QItem.item.itemNm.like("%" + searchQuery + "%");
        } else if (StringUtils.equals("itemId", searchBy)) {

            if (searchQuery.length() == 0) {
                return null;
            }

            return QItem.item.id.eq(Long.parseLong(searchQuery));
        }
        // 여기서도 마찬가지. like 를 활용했다.
        // searchBy 가 상품 명인 경우 상품명에 쿼리가 포함되는지.
        // itemId 인 경우 쿼리가 포함되는지. 하지만 생각해보니 상품 코드같은 경우는 like를 사용하면 안될 것 같다
        // 그래서 바꿧다 eq. 같은 경우에만 으로.
        return null;
    }

    // 카테고리 별로 검색하기 위한 메서드!
    private BooleanExpression searchByLevel(String level1, String level2) {
        if (StringUtils.equals(level1, "null") || level1 == null) { // level1이 null이면 전체 검색
            System.out.println();
            System.out.println("여기가 문제인가?+++++++++++++++++++++++++11111");
            return null;
        } else if (level1 != null && StringUtils.equals(level2, "null")) { // level2가 null이면 상위 카테고리로만 검색
            System.out.println("여기가 문제인가?+++++++++++++++++++++++++2222");
            return QItem.item.level1.eq(level1);
        } else if (!StringUtils.equals(level1, "null") && !StringUtils.equals(level1, "null")) { // level1과 level2 모두 존재하면 하위 카테고리로 검색
            System.out.println("여기가 문제인가?++++++++++++++++++++++++3333333");
            return QItem.item.level2.eq(level2);
        }
        System.out.println("여기까지 오면 예외 처리해야 함.searchByLevel Impl.");
        return null;
    }
//        if(level1 == null){ // level1 조차 null이라면 전체 검색을 했으므로 조건x  이후 이부분 null 이 아닌 all 등 다른 벨류를 넣자
//            return null;
//        } else
//            if (level1 != null && level2 == null) { // level1 의 값만 존재한다면. 상위카테고리만 검색.
//            return QItem.item.level1.eq(level1); // level1 이 해당 날라온 값과 일치하는 경우
//
//        } else if (level2 != null){ // level2가 존재하는 경우. 이 경우엔 level2만 검색해도 됌.
//                System.out.println("level2 로직&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&");
//            return QItem.item.level2.eq(level2);
//
//        }
//
//        System.out.println("이게 작동되면 안됌. 로직 수정해야함.");
//
//        return null;

    private BooleanExpression searchSellStatuEq(ItemSellStatus searchSellStatus) {
        System.out.println("---searchSellStatus=====> " + searchSellStatus);
        return searchSellStatus == null ? null : QItem.item.itemSellStatus.eq(searchSellStatus);
        // 쿼리스트링 값이 널이면 널. 존재하면 입력된 설정값과 일치하는 것만 검색.
    }

    @Override
    public Page<Item> getAdminItemPage(AdminItemSearchDTO adminItemSearchDTO, Pageable pageable) {

        // PageImpl<result, pageable, total> 을 하기위해 파라미터 제작 시작!
        System.out.println("itemSearchDTO-----> " + adminItemSearchDTO);
        System.out.println("pageable------> " + pageable); // 여기선 몇번째 페이지인지, 사이즈는 몇인데, 정렬은 어떤 방식인지 나옴.
        List<Item> result = jpaQueryFactory
                .selectFrom(QItem.item)
                .where(regDtsAfter(adminItemSearchDTO.getSearchDateType()), // 날짜 타입 설정.
                        searchByLikeOrEq(adminItemSearchDTO.getSearchBy(), adminItemSearchDTO.getSearchQuery()),
                        // id 혹은 상품명 검색. 해당 쿼리문자열까지.
                        searchByLevel(adminItemSearchDTO.getLevel1(), adminItemSearchDTO.getLevel2()),
                        searchSellStatuEq(adminItemSearchDTO.getItemSellStatus()))
                // 카테고리 설정 값. 
                .orderBy(QItem.item.id.desc()) // 등록순.
                .offset(pageable.getOffset()) // 데이터를 가지고 올 시작 인덱스 즉.0이게됬다
                .limit(pageable.getPageSize()) // 한 번에 가지고 올 최대 갯수 즉 10이 되겠다. pageable 에서 설정한 값이 담김.
                .fetch();

        // 이제 total 값을 구해보자
        long total = jpaQueryFactory
                .select(Wildcard.count)
                .from(QItem.item)
                .where(regDtsAfter(adminItemSearchDTO.getSearchDateType()), // 날짜 타입 설정.
                        searchByLikeOrEq(adminItemSearchDTO.getSearchBy(), adminItemSearchDTO.getSearchQuery()),
                        // id 혹은 상품명 검색. 해당 쿼리문자열까지.
                        searchByLevel(adminItemSearchDTO.getLevel1(), adminItemSearchDTO.getLevel2()),
                        searchSellStatuEq(adminItemSearchDTO.getItemSellStatus()))
                .fetchOne(); // 하나의 결과값. 즉 몇개인지.

        // 여기서 굳이 토탈이 필요한 이유
        // PageImpl<> 함수를 호출하기 위해선 토탈값도 넣어야 하기때문에 PageImpl<>를 사용하기 위해선
        // 항상 검색값의 총 갯수인 total dsl 쿼리문도 짜야한다!!!

        return new PageImpl<>(result, pageable, total); // 이건 디폴트 메서드. Ctrl 클릭으로 확인해보자
    }

    @Override
    public Page<MainItemDTO> getMainItemPage(AdminItemSearchDTO searchDTO, Pageable pageable) {
        QItem item = QItem.item;
        QItemImg itemImg = QItemImg.itemImg;

        List<MainItemDTO> result = jpaQueryFactory
                .select(
                        new QMainItemDTO(
                                item.id,
                                item.itemNm,
                                item.brandNm,
                                item.itemText,
                                itemImg.imgUrl,
                                item.price,
                                item.discountRate.intValue(),
                                item.itemSellStatus)
                ).from(itemImg)
                .join(itemImg.item, item)

                .where(itemNmLike(searchDTO.getSearchQuery()))
                .orderBy(item.id.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        long total = jpaQueryFactory
                .select(Wildcard.count)
                .from(itemImg)
                .join(itemImg.item, item)
                .where(itemNmLike(searchDTO.getSearchQuery()))
                .fetchOne();
        return new PageImpl<>(result, pageable, total);
    }

    private BooleanExpression itemNmLike(String searchQuery) {
        return StringUtils.isEmpty(searchQuery) ? null :
                QItem.item.itemNm.like("%" + searchQuery + "%");
    }


    @Override
    public Page<Item> getItemPage(ItemSearchDTO itemSearchDTO, Pageable pageable) {

        List<Item> result = jpaQueryFactory
                .selectFrom(QItem.item)
                .where(itemSellStatus(),
                        searchBylike(itemSearchDTO.getSearchBy(), itemSearchDTO.getSearchQuery()),
                        level(itemSearchDTO.getLevel()),
                        saleItems(itemSearchDTO.getSaleItems())
                )
                .orderBy(QItem.item.id.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        long total = jpaQueryFactory
                .select(Wildcard.count)
                .from(QItem.item)
                .where(itemSellStatus(),
                        searchBylike(itemSearchDTO.getSearchBy(), itemSearchDTO.getSearchQuery()),
                        level(itemSearchDTO.getLevel()),
                        saleItems(itemSearchDTO.getSaleItems()
                        ))
                .fetchOne();

        return new PageImpl<>(result, pageable, total);
    }

    private BooleanExpression level(String level) {

        if (level.isEmpty()) {
            return null;
        }
        System.out.println("들어오긴함   level");
        if (level1List.contains(level)) {
            return QItem.item.level1.eq(level);
        }
        return QItem.item.level2.eq(level);
    }

    private BooleanExpression itemSellStatus() {
        return QItem.item.itemSellStatus.ne(ItemSellStatus.STOP_SALE);
    }

    private BooleanExpression saleItems(String saleItems) {

        if (saleItems.equals("saleItems")) {
            return QItem.item.discountRate.ne(1f);
        } else {
            return null;
        }

    }

    private BooleanExpression searchBylike(String searchBy, String searchQuery) {
        System.out.println("들어오긴함searchBylike");
        if (StringUtils.isEmpty(searchBy) || StringUtils.isEmpty(searchQuery)) {
            return Expressions.asBoolean(true).isTrue(); // 빈 BooleanExpression 반환
        }

        if (StringUtils.equals("itemNm", searchBy)) {
            return QItem.item.itemNm.like("%" + searchQuery + "%");
        }
        return Expressions.asBoolean(true).isTrue(); // 기본적으로 참인 BooleanExpression 반환
    }


}
