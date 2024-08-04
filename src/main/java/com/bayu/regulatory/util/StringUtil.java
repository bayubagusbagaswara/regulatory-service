package com.bayu.regulatory.util;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@UtilityClass
@Slf4j
public class StringUtil {

    public static String processString(String value) {
        String processedValue = !value.isEmpty() ? value : "";
        processedValue = processedValue.trim();
        return processedValue;
    }

    public static String removeWhitespaceBetweenCharacters(String input) {
        return input.replaceAll("\\s+", "").trim();
    }

    public static String replaceBlanksWithUnderscores(String input) {
        return input.replaceAll("\\s", "_");
    }

    public static String joinStrings(List<String> stringList) {
        return String.join(", ", stringList);
    }

}
