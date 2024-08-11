package com.bayu.regulatory.dto.securitiesisincode;

import com.bayu.regulatory.dto.approval.InputIdentifierRequest;
import lombok.*;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UploadSecuritiesISINCodeListRequest extends InputIdentifierRequest {

    private List<UploadSecuritiesISINCodeDataRequest> uploadSecuritiesISINCodeDataRequestList;

}
