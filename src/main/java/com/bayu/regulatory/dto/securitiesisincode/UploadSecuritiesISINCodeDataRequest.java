package com.bayu.regulatory.dto.securitiesisincode;

import com.bayu.regulatory.dto.validation.AddValidationGroup;
import com.bayu.regulatory.dto.validation.UpdateValidationGroup;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UploadSecuritiesISINCodeDataRequest {

    @JsonProperty(value = "External Code 2")
    @NotBlank(message = "External Code must not be blank", groups = {AddValidationGroup.class, UpdateValidationGroup.class})
    @Pattern(regexp = "^[a-zA-Z0-9]*$", message = "Code must contain only alphanumeric characters", groups = {AddValidationGroup.class, UpdateValidationGroup.class})
    private String externalCode2;

    @JsonProperty(value = "Currency")
    @NotBlank(message = "Currency must not be blank", groups = AddValidationGroup.class)
    @Pattern(regexp = "^[A-Za-z]+$", message = "Currency must contain only alphabetic characters", groups = {AddValidationGroup.class, UpdateValidationGroup.class})
    @Size(max = 3, message = "Currency must be at most 3 characters long", groups = {AddValidationGroup.class, UpdateValidationGroup.class})
    private String currency;

    @JsonProperty(value = "ISIN LKPBU")
    @NotBlank(message = "ISIN LKPBU must not be blank", groups = AddValidationGroup.class)
    private String isinLKPBU;

    @JsonProperty(value = "ISIN LBABK")
    @NotBlank(message = "ISIN LBABK must not be blank", groups = AddValidationGroup.class)
    private String isinLBABK;

}
