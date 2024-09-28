package com.bayu.regulatory.model;

import com.bayu.regulatory.model.approval.RegulatoryApproval;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "reg_isin_code")
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class SecuritiesISINCode extends RegulatoryApproval {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "external_code_2")
    private String externalCode2;

    @Column(name = "currency")
    private String currency;

    @Column(name = "isin_lkpbu")
    private String isinLKPBU;

    @Column(name = "isin_lbabk")
    private String isinLBABK;

}
