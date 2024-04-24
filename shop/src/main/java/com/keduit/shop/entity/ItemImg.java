package com.keduit.shop.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Entity
@Table(name="item_img")
@Getter
@Setter
@ToString
public class ItemImg extends BaseTimeEntity{

  @Id
  @Column(name="item_img_id")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String imgName;   // 이미지 파일명
  private String oriImgName;   // 원본 이미지 파일명
  private String imgUrl;
  private String repimgYn;     // 대표 이미지 여부

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name="item_id")
  private Item item;

  //  이미지 정보를 업데이트하는 메소드
  public void updateItemImg(String oriImgName, String imgName, String imgUrl){
    this.oriImgName = oriImgName;
    this.imgName = imgName;
    this.imgUrl = imgUrl;
  }
}
