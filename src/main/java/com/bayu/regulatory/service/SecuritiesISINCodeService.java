package com.bayu.regulatory.service;

import com.bayu.regulatory.dto.RegulatoryDataChangeDTO;
import com.bayu.regulatory.dto.securitiesisincode.*;

import java.util.List;

public interface SecuritiesISINCodeService {

    boolean isExternalCodeAlreadyExists(String externalCode);

    SecuritiesISINCodeResponse uploadData(UploadSecuritiesISINCodeListRequest uploadSecuritiesIssuerCodeListRequest, RegulatoryDataChangeDTO regulatoryDataChangeDTO);

    SecuritiesISINCodeResponse createApprove(ApproveSecuritiesISINCodeRequest approveSecuritiesISINCodeRequest, String approveIPAddress);

    SecuritiesISINCodeResponse updateApprove(ApproveSecuritiesISINCodeRequest approveSecuritiesISINCodeRequest, String approveIPAddress);

    SecuritiesISINCodeResponse deleteById(DeleteSecuritiesISINCodeRequest deleteSecuritiesISINCodeRequest, RegulatoryDataChangeDTO regulatoryDataChangeDTO);

    SecuritiesISINCodeResponse deleteApprove(ApproveSecuritiesISINCodeRequest approveSecuritiesISINCodeRequest, String approveIPAddress);

    SecuritiesISINCodeDTO getById(Long id);

    SecuritiesISINCodeDTO getByExternalCode(String externalCode);

    List<SecuritiesISINCodeDTO> getAll();

}
