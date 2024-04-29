package com.keduit.shop.entity;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "address_id")
    private Long id;


    private Integer postalCode;
    private String roadName;
    private String localAddress;

    private String detailedAddress;

    private  String dong;

    private Boolean check;

}
