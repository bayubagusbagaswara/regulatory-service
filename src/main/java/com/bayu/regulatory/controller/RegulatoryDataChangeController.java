package com.bayu.regulatory.controller;

import com.bayu.regulatory.dto.RegulatoryDataChangeDTO;
import com.bayu.regulatory.dto.ResponseDto;
import com.bayu.regulatory.service.RegulatoryDataChangeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(path = "/api/regulatory/data-change")
@Slf4j
@RequiredArgsConstructor
public class RegulatoryDataChangeController {

    private final RegulatoryDataChangeService regulatoryDataChangeService;

    @GetMapping(path = "/all")
    public ResponseEntity<ResponseDto<List<RegulatoryDataChangeDTO>>> getAllOrderByIdDesc() {
        List<RegulatoryDataChangeDTO> all = regulatoryDataChangeService.getAll();
        ResponseDto<List<RegulatoryDataChangeDTO>> response = ResponseDto.<List<RegulatoryDataChangeDTO>>builder()
                .code(String.valueOf(HttpStatus.OK.value()))
                .message(HttpStatus.OK.getReasonPhrase())
                .payload(all)
                .build();
        return ResponseEntity.ok(response);
    }

}
