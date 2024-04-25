package com.keduit.shop.entity;
import javax.persistence.*;

@Entity
@Table(name = "etoile.search_rank")
public class SearchRank {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int number;

    private String keyword;

    // 생성자, 게터 및 세터
}