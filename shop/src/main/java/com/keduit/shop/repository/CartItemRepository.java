package com.keduit.shop.repository;

import com.keduit.shop.dto.CartDetailDTO;
import com.keduit.shop.entity.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    /*카트 아이디와 상품 아이디를 주고 장바구니 상품을 읽어옴*/
    CartItem findByCartIdAndItemId(Long cartId, Long itemId);

    /*CartDetailDTO의 생성자를 이용하여 DTO를반환 : 패키지명을 포함한 DTO의 이름을 기술 이러면 반환을함*/
    /*주의사항 : 생성자의 파라미터 순서는 꼭 지켜야 함*/
    /*JPQL에서 이런방법도있음 연관관계 지연 로딩(LAZY)로 설정한 경우에 엔티티에 매핑된 다른 엔티티를 조회할때 추가적으로 쿼리문이 실행되는데 이때
     * 성능 최적화를 위해 DTO의 생성자를 이용하여 반환값으로 DTO객체를 생성할수있다.*/
    /*읽어온내용을 cartdetaildto생성자에 바로 넣는거임 */
    @Query("select new com.keduit.shop.dto.CartDetailDTO(ci.id,i.itemNm,i.price,ci.count,im.imgUrl) " +
            "from CartItem ci, ItemImg im " +
            "join ci.item i " +
            "where ci.cart.id = :cartId " +
            "and im.item.id = ci.item.id " +
            "and im.repimgYn = 'Y' " +
            "order by ci.regTime desc"
    )
    /*1. 파라미터 1개
     * 2. 파라미터 이름과 매핑명이 동일할때
     * 3. JPA 2.0 이상
     * 위의 세가지 조건이 만족하면 @Param은 생략 가능*/
    List<CartDetailDTO> findCartDetailDTOList(Long cartId); /*@Param("cartId") Long cartId 이렇게썼었음*/
}
