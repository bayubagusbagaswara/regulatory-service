package com.bayu.regulatory.dto.securitiesisincode;

import com.bayu.regulatory.dto.approval.InputIdentifierRequest;
import lombok.*;

/**
 * untuk anotasi validasi ditaruh disini
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateSecuritiesISINCodeRequest extends InputIdentifierRequest {

    private String externalCode;

    private String currency;

    private String isinLKPBU;

    private String isinLBABK;

}
