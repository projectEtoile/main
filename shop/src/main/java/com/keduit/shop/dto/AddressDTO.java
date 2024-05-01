package com.keduit.shop.dto;

import com.keduit.shop.entity.Address;
import com.keduit.shop.entity.Item;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.modelmapper.ModelMapper;

@Getter
@Setter
@ToString
public class AddressDTO {
    private Long id;

    private String postcode;

    private String roadAddress;

    private String jibunAddress;

    private String detailAddress;

    private String extraAddress;

    private Boolean selectAddress;

    private static ModelMapper modelMapper = new ModelMapper();

    // DTO -> entity
    public Address createItem(){

        return modelMapper.map(this, Address.class);
    }

    //  entity -> DTO
    public static AddressDTO of(Address address){

        return modelMapper.map(address, AddressDTO.class);
    }

}
