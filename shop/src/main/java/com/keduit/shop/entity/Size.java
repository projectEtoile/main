package com.keduit.shop.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Size extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "size_id") /*다른테이블에도 id가있으니까 구분하기위해 */
    private Long id;  /*상품 사이즈 코드*/

    @Column(nullable = false)
    private String S;  /*S사이즈*/

    @Column(nullable = false)
    private String M;  /*M사이즈*/

    @Column(nullable = false)
    private String L;  /*L사이즈*/

    @Column(nullable = false)
    private String FREE;  /*FREE사이즈*/

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private Item item;
}
/*이렇게 구성하면 사이즈관리좀더 용이*/
/*@Id
@GeneratedValue(strategy = GenerationType.IDENTITY)
@Column(name = "size_id")
private Long id;

@Column(nullable = false)
private String sizeName;  // 예: "S", "M", "L", "Free"

@OneToMany(mappedBy = "size", fetch = FetchType.LAZY)
private Set<ItemSize> itemSizes = new HashSet<>();*/
