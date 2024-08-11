package com.bayu.regulatory.service.impl;

import com.bayu.regulatory.dto.RegulatoryDataChangeDTO;
import com.bayu.regulatory.dto.securitiesisincode.*;
import com.bayu.regulatory.repository.SecuritiesISINCodeRepository;
import com.bayu.regulatory.service.SecuritiesISINCodeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class SecuritiesISINCodeServiceImpl implements SecuritiesISINCodeService {

    private final SecuritiesISINCodeRepository securitiesISINCodeRepository;

    @Override
    public SecuritiesISINCodeResponse uploadData(UploadSecuritiesISINCodeListRequest uploadSecuritiesIssuerCodeListRequest, RegulatoryDataChangeDTO regulatoryDataChangeDTO) {
        return null;
    }

    @Override
    public SecuritiesISINCodeResponse createApprove(ApproveSecuritiesISINCodeRequest approveSecuritiesISINCodeRequest, String approveIPAddress) {
        return null;
    }

    @Override
    public SecuritiesISINCodeResponse updateApprove(ApproveSecuritiesISINCodeRequest approveSecuritiesISINCodeRequest, String approveIPAddress) {
        return null;
    }

    @Override
    public SecuritiesISINCodeResponse deleteById(DeleteSecuritiesISINCodeRequest deleteSecuritiesISINCodeRequest, RegulatoryDataChangeDTO regulatoryDataChangeDTO) {
        return null;
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
        return List.of();
    }
}
