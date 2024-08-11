package com.bayu.regulatory.dto.securitiesisincode;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Disini ada JsonProperty
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UploadSecuritiesISINCodeDataRequest {

    @JsonProperty(value = "External Code 2")
    private String externalCode2;

    @JsonProperty(value = "Currency")
    private String currency;

    @JsonProperty(value = "Issuer LBABK")
    private String issuerLBABK;

    @JsonProperty(value = "Issuer LKPBU")
    private String issuerLKPBU;

}