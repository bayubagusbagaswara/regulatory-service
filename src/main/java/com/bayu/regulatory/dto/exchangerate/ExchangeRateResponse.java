package com.bayu.regulatory.dto.exchangerate;

import com.bayu.regulatory.dto.ErrorMessageDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExchangeRateResponse {

    private Integer totalDataSuccess;

    private Integer totalDataFailed;

    private List<ErrorMessageDTO> errorMessageDTOList;

}
