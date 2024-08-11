package com.bayu.regulatory.dto.exchangerate;

import com.bayu.regulatory.dto.approval.InputIdentifierRequest;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateExchangeRateRequest extends InputIdentifierRequest {

    private Long id;

    @NotBlank(message = "Currency Code cannot be empty")
    private String code;

    @NotBlank(message = "Currency Name cannot be empty")
    private String name;

    @NotBlank(message = "Rate cannot be empty")
    private String rate;

}
