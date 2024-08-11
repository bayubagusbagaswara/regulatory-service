package com.bayu.regulatory.dto.securitiesisincode;

import com.bayu.regulatory.dto.approval.ApprovalIdentifierRequest;
import lombok.*;

@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApproveSecuritiesISINCodeRequest extends ApprovalIdentifierRequest {

    private Long dataChangeId;

}
