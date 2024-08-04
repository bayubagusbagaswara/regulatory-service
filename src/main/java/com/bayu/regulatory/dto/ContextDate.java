package com.bayu.regulatory.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ContextDate {

    private Instant instantNow;

    private String monthNameMinus1;

    private String monthNameMinus1Value;

    private Integer yearMinus1;

    private String monthNameNow;

    private String monthNameNowValue;

    private Integer yearNow;

}
