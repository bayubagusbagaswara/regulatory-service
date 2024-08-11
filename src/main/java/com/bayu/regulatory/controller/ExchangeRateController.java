package com.bayu.regulatory.controller;

import com.bayu.regulatory.dto.RegulatoryDataChangeDTO;
import com.bayu.regulatory.dto.ResponseDto;
import com.bayu.regulatory.dto.exchangerate.*;
import com.bayu.regulatory.service.ExchangeRateService;
import com.bayu.regulatory.util.ClientIPUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/api/regulatory/exchange-rate")
@Slf4j
@RequiredArgsConstructor
public class ExchangeRateController {

    private static final String BASE_URL_EXCHANGE_RATE = "/api/regulatory/exchange-rate";
    private static final String MENU_EXCHANGE_RATE = "Exchange Rate";

    private final ExchangeRateService exchangeRateService;

    /* Create Single Data */
    @PostMapping(path = "/create")
    public ResponseEntity<ResponseDto<ExchangeRateResponse>> create(@RequestBody CreateExchangeRateRequest createExchangeRateRequest, HttpServletRequest servletRequest) {
        String inputIPAddress = ClientIPUtil.getClientIP(servletRequest);
        RegulatoryDataChangeDTO regulatoryDataChangeDTO = RegulatoryDataChangeDTO.builder()
                .inputIPAddress(inputIPAddress)
                .methodHttp(HttpMethod.POST.name())
                .endpoint(BASE_URL_EXCHANGE_RATE + "/create/approve")
                .isRequestBody(true)
                .isRequestParam(false)
                .isPathVariable(false)
                .menu(MENU_EXCHANGE_RATE)
                .build();

        ExchangeRateResponse singleData = exchangeRateService.create(createExchangeRateRequest, regulatoryDataChangeDTO);
        ResponseDto<ExchangeRateResponse> response = ResponseDto.<ExchangeRateResponse>builder()
                .code(String.valueOf(HttpStatus.OK.value()))
                .message(HttpStatus.OK.getReasonPhrase())
                .payload(singleData)
                .build();
        return ResponseEntity.ok(response);
    }

    /* Approve Create Single Data */
    @PostMapping(path = "/create/approve")
    public ResponseEntity<ResponseDto<ExchangeRateResponse>> createApprove(@RequestBody ApproveExchangeRateRequest approveExchangeRateRequest, HttpServletRequest servletRequest) {
        String approveIPAddress = ClientIPUtil.getClientIP(servletRequest);
        ExchangeRateResponse singleApprove = exchangeRateService.createApprove(approveExchangeRateRequest, approveIPAddress);
        ResponseDto<ExchangeRateResponse> response = ResponseDto.<ExchangeRateResponse>builder()
                .code(String.valueOf(HttpStatus.OK.value()))
                .message(HttpStatus.OK.getReasonPhrase())
                .payload(singleApprove)
                .build();
        return ResponseEntity.ok(response);
    }

    /* Edit Single Data */
    @PutMapping(path = "/updateById")
    public ResponseEntity<ResponseDto<ExchangeRateResponse>> updateById(@RequestBody UpdateExchangeRateRequest updateExchangeRateRequest, HttpServletRequest servletRequest) {
        String inputIPAddress = ClientIPUtil.getClientIP(servletRequest);
        RegulatoryDataChangeDTO regulatoryDataChangeDTO = RegulatoryDataChangeDTO.builder()
                .inputIPAddress(inputIPAddress)
                .methodHttp(HttpMethod.PUT.name())
                .endpoint(BASE_URL_EXCHANGE_RATE + "/update/approve")
                .isRequestBody(true)
                .isRequestParam(false)
                .isPathVariable(false)
                .menu(MENU_EXCHANGE_RATE)
                .build();
        ExchangeRateResponse singleData = exchangeRateService.updateById(updateExchangeRateRequest, regulatoryDataChangeDTO);
        ResponseDto<ExchangeRateResponse> response = ResponseDto.<ExchangeRateResponse>builder()
                .code(String.valueOf(HttpStatus.OK.value()))
                .message(HttpStatus.OK.getReasonPhrase())
                .payload(singleData)
                .build();
        return ResponseEntity.ok(response);
    }

    /* Approve Edit Single Data */
    @PutMapping(path = "/update/approve")
    public ResponseEntity<ResponseDto<ExchangeRateResponse>> updateApprove(@RequestBody ApproveExchangeRateRequest approveExchangeRateRequest, HttpServletRequest servletRequest) {
        String approveIPAddress = ClientIPUtil.getClientIP(servletRequest);
        ExchangeRateResponse updateSingleApprove = exchangeRateService.updateApprove(approveExchangeRateRequest, approveIPAddress);
        ResponseDto<ExchangeRateResponse> response = ResponseDto.<ExchangeRateResponse>builder()
                .code(String.valueOf(HttpStatus.OK.value()))
                .message(HttpStatus.OK.getReasonPhrase())
                .payload(updateSingleApprove)
                .build();
        return ResponseEntity.ok(response);
    }

    /* Delete Single Data */
    @DeleteMapping(path = "/deleteById")
    public ResponseEntity<ResponseDto<ExchangeRateResponse>> deleteById(@RequestBody DeleteExchangeRateRequest deleteExchangeRateRequest, HttpServletRequest servletRequest) {
        String inputIPAddress = ClientIPUtil.getClientIP(servletRequest);
        RegulatoryDataChangeDTO regulatoryDataChangeDTO = RegulatoryDataChangeDTO.builder()
                .inputIPAddress(inputIPAddress)
                .methodHttp(HttpMethod.DELETE.name())
                .endpoint(BASE_URL_EXCHANGE_RATE + "/delete/approve")
                .isRequestBody(true)
                .isRequestParam(false)
                .isPathVariable(false)
                .methodHttp(MENU_EXCHANGE_RATE)
                .build();
        ExchangeRateResponse deleteData = exchangeRateService.deleteById(deleteExchangeRateRequest, regulatoryDataChangeDTO);
        ResponseDto<ExchangeRateResponse> response = ResponseDto.<ExchangeRateResponse>builder()
                .code(String.valueOf(HttpStatus.OK.value()))
                .message(HttpStatus.OK.getReasonPhrase())
                .payload(deleteData)
                .build();
        return ResponseEntity.ok(response);
    }

    /* Approve Delete Single Data */
    @DeleteMapping(path = "/delete/approve")
    public ResponseEntity<ResponseDto<ExchangeRateResponse>> deleteApprove(@RequestBody ApproveExchangeRateRequest approveExchangeRateRequest, HttpServletRequest servletRequest) {
        String approveIPAddress = ClientIPUtil.getClientIP(servletRequest);
        ExchangeRateResponse deleteSingleApprove = exchangeRateService.deleteApprove(approveExchangeRateRequest, approveIPAddress);
        ResponseDto<ExchangeRateResponse> response = ResponseDto.<ExchangeRateResponse>builder()
                .code(String.valueOf(HttpStatus.OK.value()))
                .message(HttpStatus.OK.getReasonPhrase())
                .payload(deleteSingleApprove)
                .build();
        return ResponseEntity.ok(response);
    }

    @GetMapping(path = "/code")
    public ResponseEntity<ResponseDto<ExchangeRateDTO>> getByCurrencyCode(@RequestParam("code") String currencyCode) {
        ExchangeRateDTO exchangeRateDTO = exchangeRateService.getByCurrencyCode(currencyCode);
        ResponseDto<ExchangeRateDTO> response = ResponseDto.<ExchangeRateDTO>builder()
                .code(String.valueOf(HttpStatus.OK.value()))
                .message(HttpStatus.OK.getReasonPhrase())
                .payload(exchangeRateDTO)
                .build();
        return ResponseEntity.ok(response);
    }

    @GetMapping(path = "/all")
    public ResponseEntity<ResponseDto<List<ExchangeRateDTO>>> getAll() {
        List<ExchangeRateDTO> all = exchangeRateService.getAll();
        ResponseDto<List<ExchangeRateDTO>> response = ResponseDto.<List<ExchangeRateDTO>>builder()
                .code(String.valueOf(HttpStatus.OK.value()))
                .message(HttpStatus.OK.getReasonPhrase())
                .payload(all)
                .build();
        return ResponseEntity.ok(response);
    }

}
