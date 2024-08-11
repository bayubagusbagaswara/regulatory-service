package com.bayu.regulatory.dto.exchangerate;

import com.bayu.regulatory.dto.approval.InputIdentifierRequest;
import jakarta.validation.constraints.NotBlank;
import lombok.*;


@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateExchangeRateRequest extends InputIdentifierRequest {

    @NotBlank(message = "Currency Code cannot be empty")
    private String currencyCode;

    @NotBlank(message = "Currency Name cannot be empty")
    private String currencyName;

    @NotBlank(message = "Rate Value cannot be empty")
    private String rate;

}
