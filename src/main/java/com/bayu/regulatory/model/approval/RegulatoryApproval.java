package com.bayu.regulatory.model.approval;

import com.bayu.regulatory.model.enumerator.ApprovalStatus;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.MappedSuperclass;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.io.Serial;
import java.io.Serializable;

@Data
@MappedSuperclass
@SuperBuilder
@NoArgsConstructor
public class RegulatoryApproval implements Serializable {

    @Serial
    private static final long serialVersionUID = -4324309746163254637L;

    @Enumerated(EnumType.STRING)
    @Column(name = "approval_status")
    private ApprovalStatus approvalStatus;


}
