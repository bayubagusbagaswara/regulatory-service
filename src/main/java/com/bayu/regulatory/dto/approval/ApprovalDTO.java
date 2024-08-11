package com.bayu.regulatory.dto.approval;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public abstract class ApprovalDTO {

    private String approvalStatus;
    private String inputerId;
    private String inputerIPAddress;
    private String inputDate;
    private String approverId;
    private String approverIPAddress;
    private String approveDate;

}
