package com.bayu.regulatory.dto.securitiesisincode;

import com.bayu.regulatory.dto.approval.InputIdentifierRequest;
import lombok.*;

@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DeleteSecuritiesISINCodeRequest extends InputIdentifierRequest {

    private Long id;

}
