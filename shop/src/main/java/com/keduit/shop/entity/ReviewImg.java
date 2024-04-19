package com.keduit.shop.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ReviewImg extends BaseTimeEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "review_img_id") /*다른테이블에도 id가있으니까 구분하기위해 */
    private Long id;  /*리뷰 사진 코드*/

    private String photoUrl;
}
