package com.bayu.regulatory.dto.securitiesisincode;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SecuritiesISINCodeDTO {

    private String externalCode;

    private String currency;

    private String isinLKPBU;

    private String isinLBABK;

}
