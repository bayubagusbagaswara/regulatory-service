package com.bayu.regulatory.dto.exchangerate;

import com.bayu.regulatory.dto.approval.InputIdentifierRequest;
import lombok.*;

@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateExchangeRateRequest extends InputIdentifierRequest {

    // ID wajib diisi
    private Long id;

    private String code;

    private String name;

    private String rate;

}
