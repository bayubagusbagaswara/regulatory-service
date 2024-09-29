package com.bayu.regulatory.controller;

import com.bayu.regulatory.dto.ResponseDto;
import com.bayu.regulatory.model.LKPBUSampleData;
import com.bayu.regulatory.repository.LKPBUSampleDataRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping(path = "/api/regulatory/lkpbu-sample")
@Slf4j
@RequiredArgsConstructor
public class LKPBUSampleDataController {

    private final LKPBUSampleDataRepository sampleDataRepository;

    @GetMapping
    public ResponseEntity<ResponseDto<String>> createSampleData() {
        sampleDataRepository.saveAll(dataList());
        ResponseDto<String> response = ResponseDto.<String>builder()
                .code(String.valueOf(HttpStatus.OK.value()))
                .message(HttpStatus.OK.getReasonPhrase())
                .payload("Success")
                .build();
        return ResponseEntity.ok(response);
    }

    private List<LKPBUSampleData> dataList() {
        return Arrays.asList(
                new LKPBUSampleData("August", 2024, "11(TIME DEPOSIT)", "IDR", new BigDecimal("12000")),
                new LKPBUSampleData("August", 2024, "1 (EQUITY)", "IDR", new BigDecimal("12000")),
                new LKPBUSampleData("August", 2024, "2 (GOVERNMENT BANK)", "IDR", new BigDecimal("12000")),
                new LKPBUSampleData("August", 2024, "11(TIME DEPOSIT)", "USD", new BigDecimal("123.8")),
                new LKPBUSampleData("August", 2024, "2 (GOVERNMENT BANK)", "USD", new BigDecimal("552.5")),
                new LKPBUSampleData("August", 2024, "2 (GOVERNMENT BANK)", "IDR", new BigDecimal("12000")),
                new LKPBUSampleData("August", 2024, "11(TIME DEPOSIT)", "IDR", new BigDecimal("12000")),
                new LKPBUSampleData("August", 2024, "11(TIME DEPOSIT)", "USD", new BigDecimal("155.3"))
        );
    }

}
