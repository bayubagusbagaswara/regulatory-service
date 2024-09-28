package com.bayu.regulatory.mapper;

import com.bayu.regulatory.dto.exchangerate.CreateExchangeRateRequest;
import com.bayu.regulatory.dto.exchangerate.ExchangeRateDTO;
import com.bayu.regulatory.dto.exchangerate.UpdateExchangeRateRequest;
import com.bayu.regulatory.model.ExchangeRate;
import com.bayu.regulatory.util.ConversionUtil;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.math.BigDecimal;

@Mapper(componentModel = "spring")
public interface ExchangeRateMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(source = "currencyCode", target = "currencyCode", qualifiedByName = "nullToEmpty")
    @Mapping(source = "currencyName", target = "currencyName", qualifiedByName = "nullToEmpty")
    @Mapping(source = "rate", target = "rate", qualifiedByName = "nullToEmpty")
    ExchangeRateDTO fromCreateRequestToDTO(CreateExchangeRateRequest createExchangeRateRequest);

    @Mapping(source = "id", target = "id")
    @Mapping(source = "code", target = "currencyCode")
    @Mapping(source = "name", target = "currencyName")
    @Mapping(source = "rate", target = "rate", qualifiedByName = "bigDecimalToString")
    ExchangeRateDTO toDTO(ExchangeRate exchangeRate);

    @Named("stringToBigDecimal")
    default BigDecimal stringToBigDecimal(String value) {
        return ConversionUtil.stringToBigDecimal(value);
    }

    @Named("bigDecimalToString")
    default String bigDecimalToString(BigDecimal value) {
        return ConversionUtil.bigDecimalToString(value);
    }

    @Named("nullToEmpty")
    default String nullToEmpty(String value) {
        return null == value ? "" : value;
    }

    @Mapping(target = "id", ignore = true)
    @Mapping(source = "currencyCode", target = "code")
    @Mapping(source = "currencyName", target = "name")
    @Mapping(source = "rate", target = "rate", qualifiedByName = "stringToBigDecimal")
    ExchangeRate toModel(ExchangeRateDTO exchangeRateDTO);

    @Mapping(target = "id", ignore = true)
    @Mapping(source = "code", target = "currencyCode", qualifiedByName = "nullToEmpty")
    @Mapping(source = "name", target = "currencyName", qualifiedByName = "nullToEmpty")
    @Mapping(source = "rate", target = "rate", qualifiedByName = "nullToEmpty")
    ExchangeRateDTO fromUpdateRequestToDTO(UpdateExchangeRateRequest updateExchangeRateRequest);

}
