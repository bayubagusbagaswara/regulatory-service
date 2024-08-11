package com.bayu.regulatory.service;

import com.bayu.regulatory.dto.RegulatoryDataChangeDTO;
import com.bayu.regulatory.dto.exchangerate.*;

import java.util.List;

public interface ExchangeRateService {

    boolean isExistsByCode(String code);

    ExchangeRateResponse create(CreateExchangeRateRequest createExchangeRateRequest, RegulatoryDataChangeDTO regulatoryDataChangeDTO);

    ExchangeRateResponse createApprove(ApproveExchangeRateRequest approveExchangeRateRequest, String approveIPAddress);

    ExchangeRateResponse updateById(UpdateExchangeRateRequest updateExchangeRateRequest, RegulatoryDataChangeDTO regulatoryDataChangeDTO);

    ExchangeRateResponse updateApprove(ApproveExchangeRateRequest approveExchangeRateRequest, String approveIPAddress);

    ExchangeRateResponse deleteById(DeleteExchangeRateRequest deleteExchangeRateRequest, RegulatoryDataChangeDTO regulatoryDataChangeDTO);

    ExchangeRateResponse deleteApprove(ApproveExchangeRateRequest approveExchangeRateRequest, String approveIPAddress);

    ExchangeRateDTO getById(Long id);

    ExchangeRateDTO getByCurrencyCode(String currencyCode);

    List<ExchangeRateDTO> getAll();

}
