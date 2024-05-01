package com.keduit.shop.dto;

import com.keduit.shop.constant.ItemSellStatus;
import com.keduit.shop.entity.Item;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.modelmapper.ModelMapper;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ToString
public class ItemFormDTO {

    private Long id;

    @NotBlank(message = "상품명은 필수 입력입니다.")
    private String itemNm;

    @NotBlank(message = "브랜드명은 필수입니다.")
    private String brandNm;
    
    private String level1; // 폼 js에서 자동 설정
    
    @NotBlank(message = "카테고리 선택은 필수입니다")
    private String level2;

    @Enumerated(EnumType.STRING)
    private ItemSellStatus itemSellStatus;    // 상품 판매 현황

    private List<ItemImgDTO> itemImgDTOList = new ArrayList<>(); // 이미지 담아두는 공간.
    
     private List<Long> itemImgIds = new ArrayList<>(); // 아이탬 수정하기 때 필요한 리스트
    // 수정하기 페이지에서 기존 이미지들은 모두 삭제하고 다시 등록하는 로직이므로
    // 기존의 이미지들의 id를 리스트에 담았다. 모두 삭제됨.

    @NotNull(message = "가격은 필수 입력입니다.")
    private Integer price;

    @NotBlank(message = "소재는 필수 입력입니다.")
    private String material;

    private String itemText; // 상품 상세 설명 필수x

    @NotNull(message = "사이즈별 재고수량은 필수 입력입니다 -재고 없을 시 0 입력")
    private int stockS;

    @NotNull(message ="사이즈별 재고수량은 필수 입력입니다 -재고 없을 시 0 입력" )
    private int stockM;

    @NotNull(message = "사이즈별 재고수량은 필수 입력입니다 -재고 없을 시 0 입력")
    private int stockL;

    @NotNull(message = "사이즈별 재고수량은 필수 입력입니다 -재고 없을 시 0 입력")
    private int stockFree;

    private static ModelMapper modelMapper = new ModelMapper();

    // ItemFormDTO -> Item
    public Item createItem(){

        return modelMapper.map(this, Item.class);
    }

    //  Item -> ItemFormDTO
    public static ItemFormDTO of(Item item){

        return modelMapper.map(item, ItemFormDTO.class);
    }


}
