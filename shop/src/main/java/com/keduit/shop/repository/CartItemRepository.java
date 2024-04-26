package com.keduit.shop.repository;

import com.keduit.shop.dto.CartDetailDTO;
import com.keduit.shop.entity.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    /* 카트 아이디, 상품 아이디, 사이즈를 기준으로 장바구니 상품 조회 */
    CartItem findByCartIdAndItemIdAndSize(Long cartId, Long itemId, String size);

    /* 장바구니 상세 정보를 사이즈 포함하여 조회 */
    @Query("select new com.keduit.shop.dto.CartDetailDTO(ci.id, i.itemNm, i.price, ci.count, im.imgUrl, ci.size) " +
            "from CartItem ci, ItemImg im " +
            "join ci.item i " +
            "where ci.cart.id = :cartId " +
            "and im.item.id = ci.item.id " +
            "and im.repimgYn = 'Y' " +
            "order by ci.regTime desc")
    List<CartDetailDTO> findCartDetailDTOList(Long cartId);
}

