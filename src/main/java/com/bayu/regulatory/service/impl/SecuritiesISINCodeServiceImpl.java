package com.bayu.regulatory.service.impl;

import com.bayu.regulatory.dto.ErrorMessageDTO;
import com.bayu.regulatory.dto.RegulatoryDataChangeDTO;
import com.bayu.regulatory.dto.securitiesisincode.*;
import com.bayu.regulatory.exception.DataNotFoundException;
import com.bayu.regulatory.mapper.RegulatoryDataChangeMapper;
import com.bayu.regulatory.mapper.SecuritiesISINCodeMapper;
import com.bayu.regulatory.model.RegulatoryDataChange;
import com.bayu.regulatory.model.SecuritiesISINCode;
import com.bayu.regulatory.repository.SecuritiesISINCodeRepository;
import com.bayu.regulatory.service.RegulatoryDataChangeService;
import com.bayu.regulatory.service.SecuritiesISINCodeService;
import com.bayu.regulatory.util.JsonUtil;
import com.bayu.regulatory.util.ValidationData;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Errors;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.bayu.regulatory.model.enumerator.ApprovalStatus.APPROVED;

@Service
@Slf4j
@RequiredArgsConstructor
public class SecuritiesISINCodeServiceImpl implements SecuritiesISINCodeService {

    private static final String UNKNOWN_EXTERNAL_CODE = "Unknown External Code";

    private final SecuritiesISINCodeRepository securitiesISINCodeRepository;
    private final ObjectMapper objectMapper;
    private final ValidationData validationData;
    private final RegulatoryDataChangeService regulatoryDataChangeService;
    private final RegulatoryDataChangeMapper dataChangeMapper;
    private final SecuritiesISINCodeMapper securitiesISINCodeMapper;

    public boolean isExternalCodeAlreadyExists(String externalCode) {
        return securitiesISINCodeRepository.existsByExternalCode2(externalCode);
    }

    @Transactional
    @Override
    public synchronized SecuritiesISINCodeResponse uploadData(UploadSecuritiesISINCodeListRequest uploadSecuritiesIssuerCodeListRequest, RegulatoryDataChangeDTO regulatoryDataChangeDTO) {
        log.info("Start upload data securities isin code: {}, {}", uploadSecuritiesIssuerCodeListRequest, regulatoryDataChangeDTO);
        String inputId = uploadSecuritiesIssuerCodeListRequest.getInputId();
        regulatoryDataChangeDTO.setInputId(inputId);
        int totalDataSuccess = 0;
        int totalDataFailed = 0;
        List<ErrorMessageDTO> errorMessageDTOList = new ArrayList<>();

        for (UploadSecuritiesISINCodeDataRequest isinCodeDataRequest : uploadSecuritiesIssuerCodeListRequest.getUploadSecuritiesISINCodeDataRequestList()) {
            List<String> validationErrors = new ArrayList<>();
            SecuritiesISINCodeDTO securitiesISINCodeDTO = null;
            try {
                Errors errors = validationData.validateObject(isinCodeDataRequest);
                if (errors.hasErrors()) {
                    errors.getAllErrors().forEach(error -> validationErrors.add(error.getDefaultMessage()));
                }

                securitiesISINCodeDTO = securitiesISINCodeMapper.fromUploadRequestToDTO(isinCodeDataRequest);

                if (!validationErrors.isEmpty()) {
                    ErrorMessageDTO errorMessageDTO = new ErrorMessageDTO(
                            !securitiesISINCodeDTO.getExternalCode().isEmpty() ? securitiesISINCodeDTO.getExternalCode() : UNKNOWN_EXTERNAL_CODE,
                            validationErrors);
                    errorMessageDTOList.add(errorMessageDTO);
                    totalDataFailed++;
                } else {
                    Optional<SecuritiesISINCode> securitiesISINCode = securitiesISINCodeRepository.findByExternalCode(isinCodeDataRequest.getExternalCode2());

                    if (securitiesISINCode.isPresent()) {
                        handleExistingISINCode(securitiesISINCode.get(), securitiesISINCodeDTO, regulatoryDataChangeDTO);
                    } else {
                        handleNewISINCode(securitiesISINCodeDTO, regulatoryDataChangeDTO);
                    }
                    totalDataSuccess++;
                }
            } catch (Exception e) {
                handleGeneralError(securitiesISINCodeDTO, e, validationErrors, errorMessageDTOList);
                totalDataFailed++;
            }
        }

        return new SecuritiesISINCodeResponse(totalDataSuccess, totalDataFailed, errorMessageDTOList);
    }

    @Override
    public SecuritiesISINCodeResponse createApprove(ApproveSecuritiesISINCodeRequest approveSecuritiesISINCodeRequest, String approveIPAddress) {
        log.info("Start create approve Securities ISIN Code: {}, {}", approveSecuritiesISINCodeRequest, approveIPAddress);
        String approveId = approveSecuritiesISINCodeRequest.getApproveId();
        int totalDataSuccess = 0;
        int totalDataFailed = 0;
        List<ErrorMessageDTO> errorMessageDTOList = new ArrayList<>();
        List<String> validationErrors = new ArrayList<>();
        SecuritiesISINCodeDTO securitiesISINCodeDTO = null;

        try {
            RegulatoryDataChange dataChange = regulatoryDataChangeService.getById(approveSecuritiesISINCodeRequest.getDataChangeId());
            securitiesISINCodeDTO = objectMapper.readValue(dataChange.getJsonDataAfter(), SecuritiesISINCodeDTO.class);

            /* validate whether the code already exists */
            validationExternalCodeAlreadyExists(securitiesISINCodeDTO.getExternalCode(), validationErrors);

            if (!validationErrors.isEmpty()) {
                regulatoryDataChangeService.setApprovalStatusIsRejected(dataChange, validationErrors);
                totalDataFailed++;
            } else {
                LocalDateTime approveDate = LocalDateTime.now();

                SecuritiesISINCode securitiesISINCode = SecuritiesISINCode.builder()
                        .approvalStatus(APPROVED)
                        .approveId(approveId)
                        .approveDate(approveDate)
                        .approveIPAddress(approveIPAddress)
                        .inputId(dataChange.getInputId())
                        .inputDate(dataChange.getInputDate())
                        .inputIPAddress(dataChange.getInputIPAddress())
                        .externalCode2(securitiesISINCodeDTO.getExternalCode())
                        .currency(securitiesISINCodeDTO.getCurrency())
                        .isinLKPBU(securitiesISINCodeDTO.getIsinLKPBU())
                        .isinLBABK(securitiesISINCodeDTO.getIsinLBABK())
                        .build();
                SecuritiesISINCode save = securitiesISINCodeRepository.save(securitiesISINCode);

                dataChange.setApproveId(approveId);
                dataChange.setApproveIPAddress(approveIPAddress);
                dataChange.setEntityId(save.getId().toString());
                dataChange.setJsonDataAfter(
                        JsonUtil.cleanedEntityDataFromApprovalData(
                                objectMapper.writeValueAsString(save)
                        )
                );
                dataChange.setDescription("Success create approve with id: " + save.getId());
                regulatoryDataChangeService.setApprovalStatusIsApproved(dataChange);
                totalDataSuccess++;
            }
        } catch (Exception e) {
            handleGeneralError(securitiesISINCodeDTO, e, validationErrors, errorMessageDTOList);
            totalDataFailed++;
        }
        return new SecuritiesISINCodeResponse(totalDataSuccess, totalDataFailed, errorMessageDTOList);
    }

    @Override
    public SecuritiesISINCodeResponse updateApprove(ApproveSecuritiesISINCodeRequest approveSecuritiesISINCodeRequest, String approveIPAddress) {
        log.info("Start update approve Securities ISIN Code: {}, {}", approveSecuritiesISINCodeRequest, approveIPAddress);
        String approveId = approveSecuritiesISINCodeRequest.getApproveId();
        int totalDataSuccess = 0;
        int totalDataFailed = 0;
        List<ErrorMessageDTO> errorMessageDTOList = new ArrayList<>();
        List<String> validationErrors = new ArrayList<>();
        SecuritiesISINCodeDTO securitiesISINCodeDTO = null;
        try {
            RegulatoryDataChange dataChange = regulatoryDataChangeService.getById(approveSecuritiesISINCodeRequest.getDataChangeId());

            securitiesISINCodeDTO = objectMapper.readValue(dataChange.getJsonDataAfter(), SecuritiesISINCodeDTO.class);
            log.info("Update Approve: {}", securitiesISINCodeDTO);

            SecuritiesISINCode securitiesISINCode = securitiesISINCodeRepository.findById(Long.valueOf(dataChange.getEntityId()))
                    .orElseThrow(() -> new DataNotFoundException("Securities ISIN Code not found with id: " + dataChange.getEntityId()));

            // do update
            LocalDateTime approveDate = LocalDateTime.now();

            if (!securitiesISINCodeDTO.getCurrency().isEmpty()) {
                securitiesISINCode.setCurrency(securitiesISINCodeDTO.getCurrency());
            }

            if (!securitiesISINCodeDTO.getIsinLKPBU().isEmpty()) {
                securitiesISINCode.setIsinLKPBU(securitiesISINCodeDTO.getIsinLKPBU());
            }

            if (!securitiesISINCodeDTO.getIsinLBABK().isEmpty()) {
                securitiesISINCode.setIsinLBABK(securitiesISINCodeDTO.getIsinLBABK());
            }

            securitiesISINCode.setApprovalStatus(APPROVED);
            securitiesISINCode.setApproveId(approveId);
            securitiesISINCode.setApproveIPAddress(approveIPAddress);
            securitiesISINCode.setApproveDate(approveDate);
            securitiesISINCode.setInputId(dataChange.getInputId());
            securitiesISINCode.setInputIPAddress(dataChange.getInputIPAddress());
            securitiesISINCode.setInputDate(dataChange.getInputDate());

            SecuritiesISINCode save = securitiesISINCodeRepository.save(securitiesISINCode);

            dataChange.setApproveId(approveId);
            dataChange.setApproveIPAddress(approveIPAddress);
            dataChange.setJsonDataAfter(
                    JsonUtil.cleanedEntityDataFromApprovalData(
                            objectMapper.writeValueAsString(save)
                    )
            );
            dataChange.setDescription("Success update approve with id: " + save.getId());
            regulatoryDataChangeService.setApprovalStatusIsApproved(dataChange);
            totalDataSuccess++;
        } catch (Exception e) {
            handleGeneralError(securitiesISINCodeDTO, e, validationErrors, errorMessageDTOList);
            totalDataFailed++;

        }
        return new SecuritiesISINCodeResponse(totalDataSuccess, totalDataFailed, errorMessageDTOList);
    }

    @Override
    public SecuritiesISINCodeResponse deleteById(DeleteSecuritiesISINCodeRequest deleteSecuritiesISINCodeRequest, RegulatoryDataChangeDTO regulatoryDataChangeDTO) {
        log.info("Start delete Securities ISIN Code by id: {}, {}", deleteSecuritiesISINCodeRequest, regulatoryDataChangeDTO);
        regulatoryDataChangeDTO.setInputId(deleteSecuritiesISINCodeRequest.getInputId());
        int totalDataSuccess = 0;
        int totalDataFailed = 0;
        List<ErrorMessageDTO> errorMessageDTOList = new ArrayList<>();
        List<String> validationErrors = new ArrayList<>();
        SecuritiesISINCodeDTO securitiesISINCodeDTO = null;

        try {
            SecuritiesISINCode securitiesISINCode = securitiesISINCodeRepository.findById(deleteSecuritiesISINCodeRequest.getId())
                    .orElseThrow(() -> new DataNotFoundException("Securities ISIN Code not found with id: " + deleteSecuritiesISINCodeRequest.getInputId()));

            securitiesISINCodeDTO = securitiesISINCodeMapper.toDTO(securitiesISINCode);

            regulatoryDataChangeDTO.setEntityId(securitiesISINCode.getId().toString());
            regulatoryDataChangeDTO.setJsonDataBefore(
                    JsonUtil.cleanedEntityDataFromApprovalData(
                            objectMapper.writeValueAsString(securitiesISINCode)
                    )
            );
            regulatoryDataChangeDTO.setJsonDataAfter("");
            RegulatoryDataChange regulatoryDataChange = dataChangeMapper.toModel(regulatoryDataChangeDTO);
            regulatoryDataChangeService.createChangeActionDelete(regulatoryDataChange, SecuritiesISINCode.class);
            totalDataSuccess++;
        } catch (Exception e) {
            handleGeneralError(securitiesISINCodeDTO, e, validationErrors, errorMessageDTOList);
            totalDataFailed++;
        }
        return new SecuritiesISINCodeResponse(totalDataSuccess, totalDataFailed, errorMessageDTOList);
    }

    @Override
    public SecuritiesISINCodeResponse deleteApprove(ApproveSecuritiesISINCodeRequest approveSecuritiesISINCodeRequest, String approveIPAddress) {
        return null;
    }

    @Override
    public SecuritiesISINCodeDTO getById(Long id) {
        return null;
    }

    @Override
    public SecuritiesISINCodeDTO getByExternalCode(String externalCode) {
        return null;
    }

    @Override
    public List<SecuritiesISINCodeDTO> getAll() {
        List<SecuritiesISINCode> all = securitiesISINCodeRepository.findAll();
        return securitiesISINCodeMapper.toDTOList(all);
    }

    private void handleGeneralError(SecuritiesISINCodeDTO dto, Exception e, List<String> validationErrors, List<ErrorMessageDTO> errorMessageDTOList) {
        log.error("An unexpected error occurred: {}", e.getMessage(), e);
        validationErrors.add(e.getMessage());
        errorMessageDTOList.add(
                new ErrorMessageDTO(dto != null && !dto.getExternalCode().isEmpty() ? dto.getExternalCode() : UNKNOWN_EXTERNAL_CODE, validationErrors)
        );
    }

    private void validationExternalCodeAlreadyExists(String externalCode, List<String> errorMessages) {
        if (isExternalCodeAlreadyExists(externalCode)) {
            errorMessages.add("Securities ISIN Code is already taken with external code: " + externalCode);
        }
    }

    private void handleNewISINCode(SecuritiesISINCodeDTO securitiesISINCodeDTO, RegulatoryDataChangeDTO regulatoryDataChangeDTO) throws JsonProcessingException {
        regulatoryDataChangeDTO.setJsonDataAfter(objectMapper.writeValueAsString(securitiesISINCodeDTO));
        RegulatoryDataChange regulatoryDataChange = dataChangeMapper.toModel(regulatoryDataChangeDTO);
        log.info("Regulatory Data Change add: {}", regulatoryDataChange);
        regulatoryDataChangeService.createChangeActionAdd(regulatoryDataChange, SecuritiesISINCode.class);
    }

    private void handleExistingISINCode(SecuritiesISINCode securitiesISINCodeEntity, SecuritiesISINCodeDTO securitiesISINCodeDTO, RegulatoryDataChangeDTO regulatoryDataChangeDTO) throws JsonProcessingException {
        regulatoryDataChangeDTO.setEntityId(securitiesISINCodeEntity.getId().toString());
        regulatoryDataChangeDTO.setJsonDataBefore(JsonUtil.cleanedEntityDataFromApprovalData(
                objectMapper.writeValueAsString(securitiesISINCodeEntity)
        ));

        SecuritiesISINCodeDTO temp = SecuritiesISINCodeDTO.builder()
                .externalCode(securitiesISINCodeEntity.getExternalCode2())
                .currency(!securitiesISINCodeDTO.getCurrency().isEmpty() ? securitiesISINCodeDTO.getCurrency() : securitiesISINCodeEntity.getCurrency())
                .isinLKPBU(!securitiesISINCodeDTO.getIsinLKPBU().isEmpty() ? securitiesISINCodeDTO.getIsinLKPBU() : securitiesISINCodeEntity.getIsinLKPBU())
                .isinLBABK(!securitiesISINCodeDTO.getIsinLBABK().isEmpty() ? securitiesISINCodeDTO.getIsinLBABK() : securitiesISINCodeEntity.getIsinLBABK())
                .build();

        log.info("Temp: {}", temp);

        regulatoryDataChangeDTO.setJsonDataAfter(JsonUtil.cleanedId(objectMapper.writeValueAsString(temp)));

        RegulatoryDataChange regulatoryDataChange = dataChangeMapper.toModel(regulatoryDataChangeDTO);
        log.info("Regulatory data change edit: {}", regulatoryDataChange);
        regulatoryDataChangeService.createChangeActionEdit(regulatoryDataChange, SecuritiesISINCode.class);
    }

}
