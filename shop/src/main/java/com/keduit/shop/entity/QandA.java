package com.keduit.shop.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class QandA extends BaseTimeEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "qa_id") /*다른테이블에도 id가있으니까 구분하기위해 */
    private Long id;  /*큐앤에이 코드*/

    @Lob
    @Column(nullable = false, length = 3000)
    private String question;  /*질문*/

    @Lob
    @Column(nullable = false, length = 3000)
    private String answer; /*대답*/

    @Column(nullable = false)
    private String title;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;


}
