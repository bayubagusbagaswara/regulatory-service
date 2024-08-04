package com.bayu.regulatory.util;

import com.bayu.regulatory.dto.ContextDate;
import com.bayu.regulatory.exception.ContextDateException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.TextStyle;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

@Component
@Slf4j
public class DateUtil {

    private static final String MONTH_TEST = "June";
    private static final Integer YEAR_TEST = 2024;

    @Value("${spring.jackson.time-zone}")
    private String timeZone;

    private static Locale getLocaleEN() {
        return Locale.ENGLISH;
    }

    private Map<String, String> getMonthNow(Instant instantNow) {
        ZonedDateTime zonedDateTime = instantNow.atZone(ZoneId.systemDefault());
        LocalDate currentDate = zonedDateTime.toLocalDate();

        String monthName = currentDate.getMonth().getDisplayName(TextStyle.FULL, getLocaleEN());
        int monthValue = currentDate.getMonthValue();
        String formattedMonthValue = (monthValue < 10) ? "0" + monthValue : String.valueOf(monthValue);
        int year = currentDate.getYear();

        Map<String, String> monthYear = new HashMap<>();
        monthYear.put("monthName", monthName);
        monthYear.put("monthValue", formattedMonthValue);
        monthYear.put("year", String.valueOf(year));
        return monthYear;
    }

    private Map<String, String> getMonthMinus1(Instant instantNow) {
        ZonedDateTime zonedDateTime = instantNow.atZone(ZoneId.systemDefault());

        LocalDate currentDate = zonedDateTime.toLocalDate();

        int currentMonth = currentDate.getMonthValue();
        int currentYear = currentDate.getYear();

        int newMonth = currentMonth - 1;
        if (newMonth == 0) {
            newMonth = 12;
            currentYear--;
        }

        LocalDate previousMonthYear = LocalDate.of(currentYear, newMonth, 1);
        String monthName = previousMonthYear.getMonth().getDisplayName(TextStyle.FULL, getLocaleEN());
        int monthValue = previousMonthYear.getMonth().getValue();
        String formattedMonthValue = (monthValue < 10) ? "0" + monthValue : String.valueOf(monthValue);
        int year = previousMonthYear.getYear();

        Map<String, String> monthYear = new HashMap<>();
        monthYear.put("monthName", monthName);
        monthYear.put("monthValue", formattedMonthValue);
        monthYear.put("year", String.valueOf(year));
        return monthYear;
    }

    public ContextDate buildContextDate(Instant instantNow) {
        try {
            /* get Month Minus 1 */
            Map<String, String> monthMinus1 = getMonthMinus1(instantNow);
            String monthNameMinus1 = monthMinus1.get("monthName");
            String monthNameMinus1Value = monthMinus1.get("monthValue");
            Integer yearMinus1 = Integer.parseInt(monthMinus1.get("year"));

            /* get Month Now */
            Map<String, String> monthNow = getMonthNow(instantNow);
            String monthNameNow = monthNow.get("monthName");
            String monthNameNowValue = monthNow.get("monthValue");
            Integer yearNow = Integer.parseInt(monthNow.get("year"));

            return ContextDate.builder()
                    .instantNow(instantNow)
                    .monthNameMinus1(monthNameMinus1)
                    .monthNameMinus1Value(monthNameMinus1Value)
                    .yearMinus1(yearMinus1)
                    .monthNameNow(monthNameNow)
                    .monthNameNowValue(monthNameNowValue)
                    .yearNow(yearNow)
                    .build();
        } catch (Exception e) {
            log.error("Error when get context date: {}", e.getMessage(), e);
            log.error("Unexpected error when getting context date: {}", e.getMessage(), e);
            throw new ContextDateException("An unexpected error occurred while building the context date.", e);
        }
    }

}
