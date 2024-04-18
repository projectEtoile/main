package com.keduit.shop.dto;

import com.keduit.shop.entity.ItemImg;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.modelmapper.ModelMapper;

@Getter
@Setter
@ToString
public class ItemImgDTO {

    private Long id;
    private String imgName;
    private String oriImgName; // 최초로 등록 시 가지고있던 name
    private String imgUrl;

    // 멤버 변수로 modelmapper 객체를 추가함.
    private static ModelMapper modelMapper = new ModelMapper();

    public static ItemImgDTO of(ItemImg itemImg){

        return modelMapper.map(itemImg, ItemImgDTO.class);

    }
}
