package com.bayu.regulatory.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Table(name = "reg_lkpbu_sample_data")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LKPBUSampleData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "month")
    private String month;

    @Column(name = "year")
    private Integer year;

    @Column(name = "type_effect")
    private String typeEffect;

    @Column(name = "currency")
    private String currency;

    @Column(name = "amount")
    private BigDecimal amount;

    public LKPBUSampleData(String month, Integer year, String typeEffect, String currency, BigDecimal amount) {
        this.month = month;
        this.year = year;
        this.typeEffect = typeEffect;
        this.currency = currency;
        this.amount = amount;
    }

}
