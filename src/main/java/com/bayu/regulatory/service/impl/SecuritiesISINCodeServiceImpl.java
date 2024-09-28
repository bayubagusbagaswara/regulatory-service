package com.bayu.regulatory.service.impl;

import com.bayu.regulatory.dto.ErrorMessageDTO;
import com.bayu.regulatory.dto.RegulatoryDataChangeDTO;
import com.bayu.regulatory.dto.securitiesisincode.*;
import com.bayu.regulatory.dto.validation.AddValidationGroup;
import com.bayu.regulatory.dto.validation.UpdateValidationGroup;
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
import jakarta.validation.ConstraintViolation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static com.bayu.regulatory.model.enumerator.ApprovalStatus.APPROVED;

@Service
@Slf4j
@RequiredArgsConstructor
public class SecuritiesISINCodeServiceImpl implements SecuritiesISINCodeService {

    private static final String ID_NOT_FOUND = "Securities ISIN Code not found with id: ";
    private static final String UNKNOWN_EXTERNAL_CODE = "Unknown External Code";

    private final SecuritiesISINCodeRepository securitiesISINCodeRepository;
    private final ObjectMapper objectMapper;
    private final ValidationData validationData;
    private final RegulatoryDataChangeService regulatoryDataChangeService;
    private final RegulatoryDataChangeMapper dataChangeMapper;
    private final SecuritiesISINCodeMapper securitiesISINCodeMapper;

    @Override
    public boolean isExternalCodeAlreadyExists(String externalCode) {
        return securitiesISINCodeRepository.existsByExternalCode2(externalCode);
    }

    @Transactional
    @Override
    public synchronized SecuritiesISINCodeResponse uploadData(UploadSecuritiesISINCodeListRequest uploadSecuritiesIssuerCodeListRequest, RegulatoryDataChangeDTO regulatoryDataChangeDTO) {
        log.info("Start upload data securities ISIN code: {}, {}", uploadSecuritiesIssuerCodeListRequest, regulatoryDataChangeDTO);
        String inputId = uploadSecuritiesIssuerCodeListRequest.getInputId();
        regulatoryDataChangeDTO.setInputId(inputId);
        int totalDataSuccess = 0;
        int totalDataFailed = 0;
        List<ErrorMessageDTO> errorMessageDTOList = new ArrayList<>();

        for (UploadSecuritiesISINCodeDataRequest securitiesISINCodeDataRequest : uploadSecuritiesIssuerCodeListRequest.getUploadSecuritiesISINCodeDataRequestList()) {
            List<String> validationErrors = new ArrayList<>();
            SecuritiesISINCodeDTO securitiesISINCodeDTO = null;

            try {
                Result result = getViolationResult(securitiesISINCodeDataRequest);

                if (!result.violations().isEmpty()) {
                    createErrorViolation(securitiesISINCodeDataRequest, result, validationErrors, errorMessageDTOList);
                    totalDataFailed++;
                } else {
                    securitiesISINCodeDTO = securitiesISINCodeMapper.fromUploadRequestToDTO(securitiesISINCodeDataRequest);

                    if (result.existingISINCode().isPresent()) {
                        log.info("EDIT: {}", securitiesISINCodeDTO);
                        handleExistingISINCode(result.existingISINCode().get(), securitiesISINCodeDTO, regulatoryDataChangeDTO);
                    } else {
                        log.info("ADD: {}", securitiesISINCodeDTO);
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

    private static void createErrorViolation(UploadSecuritiesISINCodeDataRequest securitiesISINCodeDataRequest, Result result, List<String> validationErrors, List<ErrorMessageDTO> errorMessageDTOList) {
        for (ConstraintViolation<Object> violation : result.violations()) {
            validationErrors.add(violation.getMessage());
        }
        errorMessageDTOList.add(new ErrorMessageDTO(securitiesISINCodeDataRequest.getExternalCode2(), validationErrors));
    }

    private Result getViolationResult(UploadSecuritiesISINCodeDataRequest securitiesISINCodeDataRequest) {
        trimRequestData(securitiesISINCodeDataRequest);
        Set<ConstraintViolation<Object>> violations;
        Optional<SecuritiesISINCode> existingISINCode = securitiesISINCodeRepository.findByExternalCode(securitiesISINCodeDataRequest.getExternalCode2());
        if (existingISINCode.isPresent()) {
            SecuritiesISINCode securitiesISINCode = existingISINCode.get();
            populateSecuritiesISINCodeDataRequest(securitiesISINCodeDataRequest, securitiesISINCode);
            violations = validationData.validateObject(securitiesISINCodeDataRequest, UpdateValidationGroup.class);
        } else {
            violations = validationData.validateObject(securitiesISINCodeDataRequest, AddValidationGroup.class);
        }
        return new Result(violations, existingISINCode);
    }

    private record Result(Set<ConstraintViolation<Object>> violations, Optional<SecuritiesISINCode> existingISINCode) {
    }

    @Transactional
    @Override
    public synchronized SecuritiesISINCodeResponse createApprove(ApproveSecuritiesISINCodeRequest approveSecuritiesISINCodeRequest, String approveIPAddress) {
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
                dataChange.setApproveDate(approveDate);
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

    @Transactional
    @Override
    public synchronized SecuritiesISINCodeResponse updateApprove(ApproveSecuritiesISINCodeRequest approveSecuritiesISINCodeRequest, String approveIPAddress) {
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

            SecuritiesISINCode securitiesISINCode = securitiesISINCodeRepository.findById(Long.valueOf(dataChange.getEntityId()))
                    .orElseThrow(() -> new DataNotFoundException(ID_NOT_FOUND + dataChange.getEntityId()));

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

    @Transactional
    @Override
    public synchronized SecuritiesISINCodeResponse deleteById(DeleteSecuritiesISINCodeRequest deleteSecuritiesISINCodeRequest, RegulatoryDataChangeDTO regulatoryDataChangeDTO) {
        log.info("Start delete Securities ISIN Code by id: {}, {}", deleteSecuritiesISINCodeRequest, regulatoryDataChangeDTO);
        regulatoryDataChangeDTO.setInputId(deleteSecuritiesISINCodeRequest.getInputId());
        int totalDataSuccess = 0;
        int totalDataFailed = 0;
        List<ErrorMessageDTO> errorMessageDTOList = new ArrayList<>();
        List<String> validationErrors = new ArrayList<>();
        SecuritiesISINCodeDTO securitiesISINCodeDTO = null;

        try {
            SecuritiesISINCode securitiesISINCode = securitiesISINCodeRepository.findById(deleteSecuritiesISINCodeRequest.getId())
                    .orElseThrow(() -> new DataNotFoundException(ID_NOT_FOUND + deleteSecuritiesISINCodeRequest.getInputId()));

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

    @Transactional
    @Override
    public synchronized SecuritiesISINCodeResponse deleteApprove(ApproveSecuritiesISINCodeRequest approveSecuritiesISINCodeRequest, String approveIPAddress) {
        log.info("Delete approve Securities ISIN Code: {}, {}", approveSecuritiesISINCodeRequest, approveIPAddress);
        String approveId = approveSecuritiesISINCodeRequest.getApproveId();
        int totalDataSuccess = 0;
        int totalDataFailed = 0;
        List<ErrorMessageDTO> errorMessageDTOList = new ArrayList<>();
        List<String> validationErrors = new ArrayList<>();
        SecuritiesISINCodeDTO securitiesISINCodeDTO = null;

        try {
            RegulatoryDataChange dataChange = regulatoryDataChangeService.getById(approveSecuritiesISINCodeRequest.getDataChangeId());

            SecuritiesISINCode securitiesISINCode = securitiesISINCodeRepository.findById(Long.valueOf(dataChange.getEntityId()))
                    .orElseThrow(() -> new DataNotFoundException(ID_NOT_FOUND + dataChange.getEntityId()));

            securitiesISINCodeDTO = securitiesISINCodeMapper.toDTO(securitiesISINCode);

            dataChange.setApproveId(approveId);
            dataChange.setApproveIPAddress(approveIPAddress);
            dataChange.setDescription("Success delete approve with id: " + securitiesISINCode.getId());

            regulatoryDataChangeService.setApprovalStatusIsApproved(dataChange);

            /* delete entity */
            securitiesISINCodeRepository.delete(securitiesISINCode);
            totalDataSuccess++;
        } catch (Exception e) {
            handleGeneralError(securitiesISINCodeDTO, e, validationErrors, errorMessageDTOList);
            totalDataFailed++;
        }
        return new SecuritiesISINCodeResponse(totalDataSuccess, totalDataFailed, errorMessageDTOList);
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

        regulatoryDataChangeDTO.setJsonDataAfter(JsonUtil.cleanedId(objectMapper.writeValueAsString(temp)));
        log.info("Json data after: {}", regulatoryDataChangeDTO.getJsonDataAfter());

        RegulatoryDataChange regulatoryDataChange = dataChangeMapper.toModel(regulatoryDataChangeDTO);
        regulatoryDataChangeService.createChangeActionEdit(regulatoryDataChange, SecuritiesISINCode.class);
    }

    private void trimRequestData(UploadSecuritiesISINCodeDataRequest request) {
        if (request.getExternalCode2() != null) {
            request.setExternalCode2(request.getExternalCode2().trim());
        }
        if (request.getCurrency() != null) {
            request.setCurrency(request.getCurrency().trim());
        }
        if (request.getIsinLKPBU() != null) {
            request.setIsinLKPBU(request.getIsinLKPBU().trim());
        }
        if (request.getIsinLBABK() != null) {
            request.setIsinLBABK(request.getIsinLBABK().trim());
        }
    }

    private void populateSecuritiesISINCodeDataRequest(UploadSecuritiesISINCodeDataRequest securitiesISINCodeDataRequest, SecuritiesISINCode securitiesISINCode) {
        if (securitiesISINCodeDataRequest.getCurrency() == null || securitiesISINCodeDataRequest.getCurrency().isEmpty()) {
            securitiesISINCodeDataRequest.setCurrency(securitiesISINCode.getCurrency());
        }
        if (securitiesISINCodeDataRequest.getIsinLKPBU() == null || securitiesISINCodeDataRequest.getIsinLKPBU().isEmpty()) {
            securitiesISINCodeDataRequest.setIsinLKPBU(securitiesISINCode.getIsinLKPBU());
        }
        if (securitiesISINCodeDataRequest.getIsinLBABK() == null || securitiesISINCodeDataRequest.getIsinLBABK().isEmpty()) {
            securitiesISINCodeDataRequest.setIsinLBABK(securitiesISINCode.getIsinLBABK());
        }
    }

}
