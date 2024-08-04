package com.bayu.regulatory.util;

import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.exceptions.CsvException;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import java.io.FileReader;
import java.io.IOException;
import java.util.List;

@Slf4j
@UtilityClass
public class CsvReaderUtil {

    public static List<String[]> readCsvFile(String filePath) throws IOException, CsvException {
        try (CSVReader csvReader = new CSVReader(new FileReader(filePath))) {
            return csvReader.readAll();
        }
    }

    public static List<String[]> readCsvFileAndSkipFirstLine(String filePath) throws IOException, CsvException {
        try (CSVReader csvReader = new CSVReaderBuilder(new FileReader(filePath))
                .withSkipLines(1) // Skip the first line (header)
                .build()) {
            return csvReader.readAll();
        }
    }

}
