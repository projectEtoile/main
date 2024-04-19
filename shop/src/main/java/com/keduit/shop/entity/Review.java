package com.keduit.shop.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Review extends BaseTimeEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "review_id") /*다른테이블에도 id가있으니까 구분하기위해 */
    private Long id;  /*리뷰 코드*/

    @Lob
    @Column(nullable = false, length = 3000)
    private String comment;  /*리뷰 내용*/

    @Column(nullable = false)
    private int rating; /*평점*/

    @Column(nullable = false)
    private String size; /*사이즈*/


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private Item item;

    // 평점 1부터 5까지 유효성검사 메서드
    public boolean isValidRating() {
        return rating >= 1 && rating <= 5;
    }

}
