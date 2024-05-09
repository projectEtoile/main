package com.keduit.shop.entity;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Entity
@Table(name = "search_rank")
@Getter
@Setter
@ToString
public class SearchRank {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    private String keyword;

    // 생성자
    public SearchRank() {
    }

    // 생성자 오버로딩
    public SearchRank(Long id, String keyword) {
        this.id = id;
        this.keyword = keyword;
    }
}