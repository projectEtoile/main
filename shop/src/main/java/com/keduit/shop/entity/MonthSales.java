package com.keduit.shop.entity;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class MonthSales {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "month_sales_id")
    private Long id;


    @Column(nullable = false)
    private int salesAmount;  /*해당월전체 매출금액*/

    @Column(nullable = false)
    private LocalDate salesMonth;  /*매출발생한 월*/

    @Column(nullable = false)
    private LocalDate salesYear;  /*매출발생 년도*/

    @Column(nullable = false)
    private int categorySales;  /*카테고리별 매출*/


}
