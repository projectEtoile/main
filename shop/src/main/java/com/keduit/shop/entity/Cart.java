package com.keduit.shop.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor /*디폴트생성자*/
public class Cart extends BaseTimeEntity{
    @Id
    @Column(name = "cart_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /*회원 테이블과 일대일 매핑*/
    /*fetchType.EAGER : 즉시 로딩 전략 cart를 읽을때 member를 바로 읽어옴*/
    /*@OneToOne과 @ManyToOne은 EAGER 전략이 디폴트임*/
    /*@OneToMany와 @ManyToMany는 LAZY전략(지연로딩):쓸때 가져옴*/
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id") /*외래키(FK)지정,name = "member_id" : 외래키의 이름 */
    private Member member;

    public static Cart createCart(Member member){
        Cart cart = new Cart();
        cart.setMember(member);
        return cart;
    }
}
