package com.bayu.regulatory.controller;

import com.bayu.regulatory.dto.RegulatoryDataChangeDTO;
import com.bayu.regulatory.dto.ResponseDto;
import com.bayu.regulatory.dto.securitiesisincode.*;
import com.bayu.regulatory.service.SecuritiesISINCodeService;
import com.bayu.regulatory.util.ClientIPUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Maintenance Kode ISIN Efek
 */
@RestController
@RequestMapping(path = "/api/regulatory/isin-code")
@Slf4j
@RequiredArgsConstructor
public class SecuritiesISINCodeController {

    private static final String BASE_URL_SECURITIES_ISIN_CODE = "/api/regulatory/isin-code";
    private static final String MENU_ISIN_CODE = "Kode ISIN Efek";

    private final SecuritiesISINCodeService securitiesIsinCodeService;

    /* Create List Data */
    @PostMapping(path = "/upload-data")
    public ResponseEntity<ResponseDto<SecuritiesISINCodeResponse>> uploadData(@RequestBody UploadSecuritiesISINCodeListRequest uploadSecuritiesIssuerCodeListRequest, HttpServletRequest servletRequest) {
        String inputIPAddress = ClientIPUtil.getClientIP(servletRequest);
        RegulatoryDataChangeDTO regulatoryDataChangeDTO = RegulatoryDataChangeDTO.builder()
                .inputIPAddress(inputIPAddress)
                .methodHttp(HttpMethod.POST.name())
                .isRequestBody(true)
                .isRequestParam(false)
                .isPathVariable(false)
                .menu(MENU_ISIN_CODE)
                .build();
        SecuritiesISINCodeResponse listData = securitiesIsinCodeService.uploadData(uploadSecuritiesIssuerCodeListRequest, regulatoryDataChangeDTO);
        ResponseDto<SecuritiesISINCodeResponse> response = ResponseDto.<SecuritiesISINCodeResponse>builder()
                .code(String.valueOf(HttpStatus.OK.value()))
                .message(HttpStatus.OK.getReasonPhrase())
                .payload(listData)
                .build();
        return ResponseEntity.ok(response);
    }

    /* Create Single Approve */
    @PostMapping(path = "/create/approve")
    public ResponseEntity<ResponseDto<SecuritiesISINCodeResponse>> createApprove(@RequestBody ApproveSecuritiesISINCodeRequest approveSecuritiesISINCodeRequest, HttpServletRequest servletRequest) {
        String clientIP = ClientIPUtil.getClientIP(servletRequest);
        SecuritiesISINCodeResponse singleApprove = securitiesIsinCodeService.createApprove(approveSecuritiesISINCodeRequest, clientIP);
        ResponseDto<SecuritiesISINCodeResponse> response = ResponseDto.<SecuritiesISINCodeResponse>builder()
                .code(String.valueOf(HttpStatus.OK.value()))
                .message(HttpStatus.OK.getReasonPhrase())
                .payload(singleApprove)
                .build();
        return ResponseEntity.ok(response);
    }

    /* Update Single Approve */
    @PutMapping(path = "/update/approve")
    public ResponseEntity<ResponseDto<SecuritiesISINCodeResponse>> updateApprove(@RequestBody ApproveSecuritiesISINCodeRequest approveSecuritiesISINCodeRequest, HttpServletRequest servletRequest) {
        String approveIPAddress = ClientIPUtil.getClientIP(servletRequest);
        SecuritiesISINCodeResponse singleApprove = securitiesIsinCodeService.updateApprove(approveSecuritiesISINCodeRequest, approveIPAddress);
        ResponseDto<SecuritiesISINCodeResponse> response = ResponseDto.<SecuritiesISINCodeResponse>builder()
                .code(String.valueOf(HttpStatus.OK.value()))
                .message(HttpStatus.OK.getReasonPhrase())
                .payload(singleApprove)
                .build();
        return ResponseEntity.ok(response);
    }

    /* Delete Single Data */
    @DeleteMapping(path = "/deleteById")
    public ResponseEntity<ResponseDto<SecuritiesISINCodeResponse>> deleteById(@RequestBody DeleteSecuritiesISINCodeRequest deleteSecuritiesISINCodeRequest, HttpServletRequest servletRequest) {
        String inputIPAddress = ClientIPUtil.getClientIP(servletRequest);
        RegulatoryDataChangeDTO regulatoryDataChangeDTO = RegulatoryDataChangeDTO.builder()
                .inputIPAddress(inputIPAddress)
                .methodHttp(HttpMethod.DELETE.name())
                .endpoint(BASE_URL_SECURITIES_ISIN_CODE + "/delete/approve")
                .isRequestBody(true)
                .isRequestParam(false)
                .isPathVariable(false)
                .build();
        SecuritiesISINCodeResponse singleData = securitiesIsinCodeService.deleteById(deleteSecuritiesISINCodeRequest, regulatoryDataChangeDTO);
        ResponseDto<SecuritiesISINCodeResponse> response = ResponseDto.<SecuritiesISINCodeResponse>builder()
                .code(String.valueOf(HttpStatus.OK.value()))
                .message(HttpStatus.OK.getReasonPhrase())
                .payload(singleData)
                .build();
        return ResponseEntity.ok(response);
    }

    /* Delete Single Approve */
    @DeleteMapping(path = "/delete/approve")
    public ResponseEntity<ResponseDto<SecuritiesISINCodeResponse>> deleteApprove(@RequestBody ApproveSecuritiesISINCodeRequest approveSecuritiesISINCodeRequest, HttpServletRequest servletRequest) {
        String approveIPAddress = ClientIPUtil.getClientIP(servletRequest);
        SecuritiesISINCodeResponse singleApprove = securitiesIsinCodeService.deleteApprove(approveSecuritiesISINCodeRequest, approveIPAddress);
        ResponseDto<SecuritiesISINCodeResponse> response = ResponseDto.<SecuritiesISINCodeResponse>builder()
                .code(String.valueOf(HttpStatus.OK.value()))
                .message(HttpStatus.OK.getReasonPhrase())
                .payload(singleApprove)
                .build();
        return ResponseEntity.ok(response);
    }

    /* get by id */
    @GetMapping(path = "/{id}")
    public ResponseEntity<ResponseDto<SecuritiesISINCodeDTO>> getById(@PathVariable("id") Long id) {
        SecuritiesISINCodeDTO securitiesIsinCodeDTO = securitiesIsinCodeService.getById(id);
        ResponseDto<SecuritiesISINCodeDTO> response = ResponseDto.<SecuritiesISINCodeDTO>builder()
                .code(String.valueOf(HttpStatus.OK.value()))
                .message(HttpStatus.OK.getReasonPhrase())
                .payload(securitiesIsinCodeDTO)
                .build();
        return ResponseEntity.ok(response);
    }

    /* get by external code */
    @GetMapping(path = "/external-code")
    public ResponseEntity<ResponseDto<SecuritiesISINCodeDTO>> getByExternalCode(@RequestParam("externalCode") String externalCode) {
        SecuritiesISINCodeDTO securitiesIsinCodeDTO = securitiesIsinCodeService.getByExternalCode(externalCode);
        ResponseDto<SecuritiesISINCodeDTO> response = ResponseDto.<SecuritiesISINCodeDTO>builder()
                .code(String.valueOf(HttpStatus.OK.value()))
                .message(HttpStatus.OK.getReasonPhrase())
                .payload(securitiesIsinCodeDTO)
                .build();
        return ResponseEntity.ok(response);
    }

    /* get all */
    @GetMapping(path = "/all")
    public ResponseEntity<ResponseDto<List<SecuritiesISINCodeDTO>>> getAll() {
        List<SecuritiesISINCodeDTO> securitiesIsinCodeDTOList = securitiesIsinCodeService.getAll();
        ResponseDto<List<SecuritiesISINCodeDTO>> response = ResponseDto.<List<SecuritiesISINCodeDTO>>builder()
                .code(String.valueOf(HttpStatus.OK.value()))
                .message(HttpStatus.OK.getReasonPhrase())
                .payload(securitiesIsinCodeDTOList)
                .build();
        return ResponseEntity.ok(response);
    }

}
