package com.bayu.regulatory.dto.securitiesisincode;

import com.bayu.regulatory.dto.approval.InputIdentifierRequest;
import lombok.*;

@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateSecuritiesISINCodeRequest extends InputIdentifierRequest {

    private Long id;

    private String externalCode;

    private String currency;

    private String isinLKPBU;

    private String isinLBABK;

}
