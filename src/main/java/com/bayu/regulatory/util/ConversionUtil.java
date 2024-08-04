package com.bayu.regulatory.util;

import lombok.experimental.UtilityClass;

import java.math.BigDecimal;

@UtilityClass
public class ConversionUtil {

    public static BigDecimal stringToBigDecimal(String value) {
        return value != null ? new BigDecimal(value) : null;
    }

    public static String bigDecimalToString(BigDecimal value) {
        return value != null ? value.toPlainString() : null;
    }

}
