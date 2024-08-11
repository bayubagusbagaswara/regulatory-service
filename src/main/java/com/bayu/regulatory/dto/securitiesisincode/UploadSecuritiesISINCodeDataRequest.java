package com.bayu.regulatory.dto.securitiesisincode;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UploadSecuritiesISINCodeDataRequest {

    @NotBlank(message = "External Code must not be blank")
    @JsonProperty(value = "External Code 2")
    private String externalCode2;

    @NotBlank(message = "Currency must not be blank")
    @JsonProperty(value = "Currency")
    private String currency;

    @JsonProperty(value = "ISIN LKPBU")
    private String isinLKPBU;

    @JsonProperty(value = "ISIN LBABK")
    private String isinLBABK;

}
