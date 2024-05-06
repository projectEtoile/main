package com.keduit.shop.entity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "etoile.search_rank")
@Getter
@Setter
public class SearchRank {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int number;

    private String keyword;

    // 생성자
    public SearchRank() {
    }

    // 생성자 오버로딩
    public SearchRank(int number, String keyword) {
        this.number = number;
        this.keyword = keyword;
    }
}