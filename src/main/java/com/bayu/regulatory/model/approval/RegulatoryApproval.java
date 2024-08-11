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
import java.time.LocalDateTime;

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

    @Column(name = "approve_id")
    private String approveId;

    @Column(name = "approve_date")
    private LocalDateTime approveDate;

    @Column(name = "approve_ip_address")
    private String approveIPAddress;

    @Column(name = "input_id")
    private String inputId;

    @Column(name = "input_date")
    private LocalDateTime inputDate;

    @Column(name = "input_ip_address")
    private String inputIPAddress;


}
