package com.bayu.regulatory.util;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Slf4j
@UtilityClass
public class CsvDataMapper {

    private static final String dateFormat1 = "dd/MM/yyyy";
    private static final String dateFormat2 = "MM/dd/yyyy";

//    public List<CustomerActivity> mapCsvCustomerActivity(List<String[]> rows) {
//        log.info("Start map csv file Customer Activity rows: {}", rows.size());
//        List<CustomerActivity> customerActivityList = new ArrayList<>();
//
//        for (String[] row : rows) {
//            try {
//                LocalDate localDate = parseToLocalDate(row[9], dateFormat1);
//                String monthName = localDate.getMonth().getDisplayName(TextStyle.FULL, Locale.ENGLISH);
//                Integer year = localDate.getYear();
//
//                CustomerActivity customerActivity = CustomerActivity.builder()
//                        .idNumber(row[0])
//                        .portfolioCode(row[1])
//                        .security(row[2])
//                        .tradeType(row[3])
//                        .units(cleanedAndParseToBigDecimal(row[4]))
//                        .unitNetPrice(cleanedAndParseToBigDecimal(row[5]))
//                        .totalExpenses(cleanedAndParseToBigDecimal(row[6]))
//                        .accrualInterest(parseToBigDecimal(row[7]))
//                        .settlementValue(cleanedAndParseToBigDecimal(row[8]))
//                        .settlementDate(localDate)
//                        .month(monthName)
//                        .year(year)
//
//                        .build();
//
//                customerActivityList.add(customerActivity);
//            } catch (Exception e) {
//                log.error("Failed to map row to Customer Activity: {}", (Object) row, e);
//                // Optionally continue to the next iteration without adding to the list
//            }
//        }
//
//        log.info("Finish map csv file Customer Activity size: {}", customerActivityList.size());
//        return customerActivityList;
//    }
//
//    public List<LKPBUValuation> mapCsvLKPBUValuation(List<String[]> rows) {
//        log.info("Start map csv file LKPBU Valuation rows: {}", rows.size());
//        List<LKPBUValuation> lkpbuValuationList = new ArrayList<>();
//
//        for (String[] row : rows) {
//            try {
//                LocalDate localDate = parseToLocalDate(row[0], dateFormat2);
//                String monthName = localDate.getMonth().getDisplayName(TextStyle.FULL, Locale.ENGLISH);
//                Integer year = localDate.getYear();
//
//                LKPBUValuation lkpbuValuation = LKPBUValuation.builder()
//                        .valuationDate(localDate)
//                        .month(monthName)
//                        .year(year)
//                        .portfolioCode(row[1])
//                        .security(row[2])
//                        .externalCode1(row[3])
//                        .securityCurrency(row[4])
//                        .longName(row[5])
//                        .unitHolding(parseToBigDecimal(row[6]))
//                        .totalValue(parseToBigDecimal(row[7]))
//                        .build();
//
//                lkpbuValuationList.add(lkpbuValuation);
//            } catch (Exception e) {
//                log.error("Failed to map row LKPBU Valuation: {}", (Object) row, e);
//            }
//        }
//
//        log.info("Finish map csv file LKPBU Valuation size: {}", lkpbuValuationList.size());
//        return lkpbuValuationList;
//    }
//
//    public List<Deposit> mapCsvDeposit(List<String[]> rows) {
//        List<Deposit> depositList = new ArrayList<>();
//
//        for (String[] row : rows) {
//            Deposit deposit = Deposit.builder().build();
//
//            depositList.add(deposit);
//        }
//
//        return depositList;
//    }
//
//    private LocalDate parseToLocalDate(String value, String dateFormat) {
//        try {
//            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(dateFormat);
//            return LocalDate.parse(value, dateTimeFormatter);
//        } catch (Exception e) {
//            log.error("Parse Date is failed: ", e.getMessage(), e);
//            throw new CsvHandleException("Parse Date is failed: " + e.getMessage());
//        }
//    }
//
//    private BigDecimal cleanedAndParseToBigDecimal(String value) {
//        try {
//            String replace = value.replace(".", "");
//            return new BigDecimal(replace);
//        } catch (Exception e) {
//            log.error("Parse Big Decimal is failed: ", e.getMessage(), e);
//            throw new CsvHandleException("Parse Big Decimal is failed: " + e.getMessage());
//        }
//    }
//
//    private BigDecimal parseToBigDecimal(String value) {
//        try {
//            return new BigDecimal(value);
//        } catch (Exception e) {
//            log.error("Parse Big Decimal is failed: ", e.getMessage(), e);
//            throw new CsvHandleException("Parse Big Decimal is failed: " + e.getMessage());
//        }
//    }

}
