package com.bayu.regulatory.dto.exchangerate;

import com.bayu.regulatory.dto.approval.InputIdentifierRequest;
import lombok.*;

@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DeleteExchangeRateRequest extends InputIdentifierRequest {

    private Long id;

}
