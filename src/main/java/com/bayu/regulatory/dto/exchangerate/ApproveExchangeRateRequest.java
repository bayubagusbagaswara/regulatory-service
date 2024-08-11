package com.bayu.regulatory.dto.exchangerate;

import com.bayu.regulatory.dto.approval.ApprovalIdentifierRequest;
import lombok.*;

@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApproveExchangeRateRequest extends ApprovalIdentifierRequest {

    private Long dataChangeId;

}
