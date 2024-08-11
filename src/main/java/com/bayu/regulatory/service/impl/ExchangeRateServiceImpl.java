package com.bayu.regulatory.service.impl;

import com.bayu.regulatory.dto.RegulatoryDataChangeDTO;
import com.bayu.regulatory.dto.exchangerate.*;
import com.bayu.regulatory.repository.ExchangeRateRepository;
import com.bayu.regulatory.service.ExchangeRateService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class ExchangeRateServiceImpl implements ExchangeRateService {

    private final ExchangeRateRepository exchangeRateRepository;

    @Override
    public boolean isExistsByCode(String code) {
        return false;
    }

    @Override
    public ExchangeRateResponse create(CreateExchangeRateRequest createExchangeRateRequest, RegulatoryDataChangeDTO regulatoryDataChangeDTO) {
        return null;
    }

    @Override
    public ExchangeRateResponse createApprove(ApproveExchangeRateRequest approveExchangeRateRequest, String approveIPAddress) {
        return null;
    }

    @Override
    public ExchangeRateResponse updateById(UpdateExchangeRateRequest updateExchangeRateRequest, RegulatoryDataChangeDTO regulatoryDataChangeDTO) {
        return null;
    }

    @Override
    public ExchangeRateResponse updateApprove(ApproveExchangeRateRequest approveExchangeRateRequest, String approveIPAddress) {
        return null;
    }

    @Override
    public ExchangeRateResponse deleteById(DeleteExchangeRateRequest deleteExchangeRateRequest, RegulatoryDataChangeDTO regulatoryDataChangeDTO) {
        return null;
    }

    @Override
    public ExchangeRateResponse deleteApprove(ApproveExchangeRateRequest approveExchangeRateRequest, String approveIPAddress) {
        return null;
    }

    @Override
    public ExchangeRateDTO getById(Long id) {
        return null;
    }

    @Override
    public ExchangeRateDTO getByCurrencyCode(String currencyCode) {
        return null;
    }

    @Override
    public List<ExchangeRateDTO> getAll() {
        return List.of();
    }
}
