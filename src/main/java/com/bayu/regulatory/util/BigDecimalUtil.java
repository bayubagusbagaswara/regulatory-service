package com.bayu.regulatory.util;

import com.bayu.regulatory.exception.ParseValueException;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;

@Slf4j
@UtilityClass
public class BigDecimalUtil {

    public static BigDecimal parseBigDecimal(String value) {
        try {
            String normalized;
            if (null == value || value.trim().isEmpty()) {
                normalized = "0";
            } else {
                normalized = value;
            }
            return new BigDecimal(normalized);
        } catch (Exception e) {
            log.error("Parse BigDecimal is failed: {}", e.getMessage(), e);
            throw new ParseValueException("Parse BigDecimal is failed: " + e.getMessage());
        }
    }

}
