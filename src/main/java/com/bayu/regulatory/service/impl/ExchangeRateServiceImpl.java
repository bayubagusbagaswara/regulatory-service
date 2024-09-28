package com.bayu.regulatory.service.impl;

import com.bayu.regulatory.dto.ErrorMessageDTO;
import com.bayu.regulatory.dto.RegulatoryDataChangeDTO;
import com.bayu.regulatory.dto.exchangerate.*;
import com.bayu.regulatory.exception.DataNotFoundException;
import com.bayu.regulatory.mapper.ExchangeRateMapper;
import com.bayu.regulatory.mapper.RegulatoryDataChangeMapper;
import com.bayu.regulatory.model.ExchangeRate;
import com.bayu.regulatory.model.RegulatoryDataChange;
import com.bayu.regulatory.repository.ExchangeRateRepository;
import com.bayu.regulatory.service.ExchangeRateService;
import com.bayu.regulatory.service.RegulatoryDataChangeService;
import com.bayu.regulatory.util.JsonUtil;
import com.bayu.regulatory.util.ValidationData;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.validation.Errors;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static com.bayu.regulatory.model.enumerator.ApprovalStatus.APPROVED;

@Service
@Slf4j
@RequiredArgsConstructor
public class ExchangeRateServiceImpl implements ExchangeRateService {

    private static final String ID_NOT_FOUND = "Exchange Rate not found with id: ";
    private static final String UNKNOWN_CURRENCY_CODE = "Unknown Currency Code";

    private final ExchangeRateRepository exchangeRateRepository;
    private final ValidationData validationData;
    private final ExchangeRateMapper exchangeRateMapper;
    private final RegulatoryDataChangeService regulatoryDataChangeService;
    private final RegulatoryDataChangeMapper dataChangeMapper;
    private final ObjectMapper objectMapper;

    @Override
    public boolean isExistsByCode(String code) {
        log.info("Check the existing exchange rate with the code: {}", code);
        return exchangeRateRepository.existsByCode(code);
    }

    @Override
    public ExchangeRateResponse create(CreateExchangeRateRequest createExchangeRateRequest, RegulatoryDataChangeDTO regulatoryDataChangeDTO) {
        log.info("Start create exchange rate with request: {}, {}", createExchangeRateRequest, regulatoryDataChangeDTO);
        regulatoryDataChangeDTO.setInputId(createExchangeRateRequest.getInputId());
        int totalDataSuccess = 0;
        int totalDataFailed = 0;
        List<ErrorMessageDTO> errorMessageDTOList = new ArrayList<>();
        List<String> validationErrors = new ArrayList<>();
        ExchangeRateDTO exchangeRateDTO = null;

        try {
//            Errors errors = validationData.validateObject(createExchangeRateRequest);
//            if (errors.hasErrors()) {
//                errors.getAllErrors().forEach(error -> validationErrors.add(error.getDefaultMessage()));
//            }

            exchangeRateDTO = exchangeRateMapper.fromCreateRequestToDTO(createExchangeRateRequest);

            validationCurrencyCodeAlreadyExists(exchangeRateDTO.getCurrencyCode(), validationErrors);

            if (!validationErrors.isEmpty()) {
                ErrorMessageDTO errorMessageDTO = new ErrorMessageDTO(
                        !exchangeRateDTO.getCurrencyCode().isEmpty() ? exchangeRateDTO.getCurrencyCode() : UNKNOWN_CURRENCY_CODE,
                        validationErrors);
                errorMessageDTOList.add(errorMessageDTO);
                totalDataFailed++;
            } else {
                regulatoryDataChangeDTO.setJsonDataAfter(
                        objectMapper.writeValueAsString(exchangeRateDTO)
                );
                RegulatoryDataChange regulatoryDataChange = dataChangeMapper.toModel(regulatoryDataChangeDTO);
                log.info("Regulatory data change: {}", regulatoryDataChange);
                regulatoryDataChangeService.createChangeActionAdd(regulatoryDataChange, ExchangeRate.class);
                totalDataSuccess++;
            }
        } catch (Exception e) {
            handleGeneralError(exchangeRateDTO, e, validationErrors, errorMessageDTOList);
            totalDataFailed++;
        }
        return new ExchangeRateResponse(totalDataSuccess, totalDataFailed, errorMessageDTOList);
    }

    @Override
    public ExchangeRateResponse createApprove(ApproveExchangeRateRequest approveExchangeRateRequest, String approveIPAddress) {
        log.info("Start create approve exchange rate: {}, {}", approveExchangeRateRequest, approveIPAddress);
        String approveId = approveExchangeRateRequest.getApproveId();
        int totalDataSuccess = 0;
        int totalDataFailed = 0;
        List<ErrorMessageDTO> errorMessageDTOList = new ArrayList<>();
        List<String> validationErrors = new ArrayList<>();
        ExchangeRateDTO exchangeRateDTO = null;

        try {
            RegulatoryDataChange dataChange = regulatoryDataChangeService.getById(approveExchangeRateRequest.getDataChangeId());
            exchangeRateDTO = objectMapper.readValue(dataChange.getJsonDataAfter(), ExchangeRateDTO.class);

            validationCurrencyCodeAlreadyExists(exchangeRateDTO.getCurrencyCode(), validationErrors);

            if (!validationErrors.isEmpty()) {
                regulatoryDataChangeService.setApprovalStatusIsRejected(dataChange, validationErrors);
                totalDataFailed++;
            } else {
                LocalDateTime approveDate = LocalDateTime.now();

                ExchangeRate exchangeRate = exchangeRateMapper.toModel(exchangeRateDTO);
                exchangeRate.setApprovalStatus(APPROVED);
                exchangeRate.setApproveId(approveId);
                exchangeRate.setApproveDate(approveDate);
                exchangeRate.setInputId(dataChange.getInputId());
                exchangeRate.setInputDate(dataChange.getInputDate());
                exchangeRate.setInputIPAddress(dataChange.getInputIPAddress());
                ExchangeRate save = exchangeRateRepository.save(exchangeRate);

                dataChange.setApproveId(approveId);
                dataChange.setApproveIPAddress(approveIPAddress);
                dataChange.setEntityId(save.getId().toString());
                dataChange.setJsonDataAfter(
                        objectMapper.writeValueAsString(exchangeRateMapper.toDTO(save))
                );
                dataChange.setDescription("Success create approve with id: " + save.getId());
                regulatoryDataChangeService.setApprovalStatusIsApproved(dataChange);
                totalDataSuccess++;
            }
        } catch (Exception e) {
            handleGeneralError(exchangeRateDTO, e, validationErrors, errorMessageDTOList);
            totalDataFailed++;
        }
        return new ExchangeRateResponse(totalDataSuccess, totalDataFailed, errorMessageDTOList);
    }

    @Override
    public ExchangeRateResponse updateById(UpdateExchangeRateRequest updateExchangeRateRequest, RegulatoryDataChangeDTO regulatoryDataChangeDTO) {
        log.info("Start update exchange rate by id: {}, {}", updateExchangeRateRequest, regulatoryDataChangeDTO);
        String inputId = updateExchangeRateRequest.getInputId();
        regulatoryDataChangeDTO.setInputId(inputId);
        int totalDataSuccess = 0;
        int totalDataFailed = 0;
        List<ErrorMessageDTO> errorMessageDTOList = new ArrayList<>();
        List<String> validationErrors = new ArrayList<>();
        ExchangeRateDTO exchangeRateDTO = null;

        try {
//            Errors errors = validationData.validateObject(updateExchangeRateRequest);
//            if (errors.hasErrors()) {
//                errors.getAllErrors().forEach(error -> validationErrors.add(error.getDefaultMessage()));
//            }

            exchangeRateDTO = exchangeRateMapper.fromUpdateRequestToDTO(updateExchangeRateRequest);

            ExchangeRate exchangeRate = exchangeRateRepository.findById(updateExchangeRateRequest.getId())
                    .orElseThrow(() -> new DataNotFoundException(ID_NOT_FOUND + updateExchangeRateRequest.getId()));

            if (!validationErrors.isEmpty()) {
                ErrorMessageDTO errorMessageDTO = new ErrorMessageDTO(
                        !exchangeRateDTO.getCurrencyCode().isEmpty() ? exchangeRateDTO.getCurrencyCode() : UNKNOWN_CURRENCY_CODE,
                        validationErrors);
                errorMessageDTOList.add(errorMessageDTO);
                totalDataFailed++;
            } else {
                regulatoryDataChangeDTO.setEntityId(exchangeRate.getId().toString());
                regulatoryDataChangeDTO.setJsonDataBefore(JsonUtil.cleanedEntityDataFromApprovalData(
                        objectMapper.writeValueAsString(exchangeRate)
                ));

                ExchangeRateDTO temp = ExchangeRateDTO.builder()
                        .currencyCode(!exchangeRateDTO.getCurrencyCode().isEmpty() ? exchangeRateDTO.getCurrencyCode() : exchangeRate.getCode())
                        .currencyName(!exchangeRateDTO.getCurrencyName().isEmpty() ? exchangeRateDTO.getCurrencyName() : exchangeRate.getName())
                        .rate(!exchangeRateDTO.getRate().isEmpty() ? exchangeRateDTO.getRate() : exchangeRate.getRate().toPlainString())
                        .build();
                log.info("Temp: {}", temp);
                regulatoryDataChangeDTO.setJsonDataAfter(JsonUtil.cleanedId(
                        objectMapper.writeValueAsString(temp))
                );
                RegulatoryDataChange regulatoryDataChange = dataChangeMapper.toModel(regulatoryDataChangeDTO);
                log.info("Regulatory data change edit: {}", regulatoryDataChange);
                regulatoryDataChangeService.createChangeActionEdit(regulatoryDataChange, ExchangeRate.class);
                totalDataSuccess++;
            }
        } catch (Exception e) {
            handleGeneralError(exchangeRateDTO, e, validationErrors, errorMessageDTOList);
            totalDataFailed++;
        }
        return new ExchangeRateResponse(totalDataSuccess, totalDataFailed, errorMessageDTOList);
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

    private void validationCurrencyCodeAlreadyExists(String currencyCode, List<String> validationErrors) {
        if (isExistsByCode(currencyCode)) {
            validationErrors.add("Exchange Rate is already taken with code: " + currencyCode);
        }
    }

    private void handleGeneralError(ExchangeRateDTO exchangeRateDTO, Exception e, List<String> validationErrors, List<ErrorMessageDTO> errorMessageDTOList) {
        log.error("An unexpected error occurred: {}", e.getMessage(), e);
        validationErrors.add(e.getMessage());
        errorMessageDTOList.add(
                new ErrorMessageDTO(exchangeRateDTO != null && !exchangeRateDTO.getCurrencyCode().isEmpty() ? exchangeRateDTO.getCurrencyCode() : UNKNOWN_CURRENCY_CODE, validationErrors)
        );
    }

}
