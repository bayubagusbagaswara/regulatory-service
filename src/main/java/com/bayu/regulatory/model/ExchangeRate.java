package com.bayu.regulatory.model;

import com.bayu.regulatory.model.approval.RegulatoryApproval;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "reg_exchange_rate")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExchangeRate extends RegulatoryApproval {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "code", nullable = false)
    private String code;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "rate", nullable = false)
    private BigDecimal rate;

}
