package com.bayu.regulatory.mapper;

import com.bayu.regulatory.dto.securitiesisincode.SecuritiesISINCodeDTO;
import com.bayu.regulatory.dto.securitiesisincode.UploadSecuritiesISINCodeDataRequest;
import com.bayu.regulatory.model.SecuritiesISINCode;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;

@Mapper(componentModel = "spring")
public interface SecuritiesISINCodeMapper {

    @Mapping(source = "id", target = "id")
    @Mapping(source = "externalCode2", target = "externalCode")
    SecuritiesISINCodeDTO toDTO(SecuritiesISINCode securitiesISINCode);

    @Mapping(source = "externalCode2", target = "externalCode", qualifiedByName = "nullToEmpty")
    @Mapping(source = "currency", target = "currency", qualifiedByName = "nullToEmpty")
    @Mapping(source = "isinLKPBU", target = "isinLKPBU", qualifiedByName = "nullToEmpty")
    @Mapping(source = "isinLBABK", target = "isinLBABK", qualifiedByName = "nullToEmpty")
    @Mapping(target = "id", ignore = true) // Ignoring the ID field
    SecuritiesISINCodeDTO fromUploadRequestToDTO(UploadSecuritiesISINCodeDataRequest dataRequest);

    @Named("nullToEmpty")
    default String nullToEmpty(String value) {
        return null == value ? "" : value;
    }

    List<SecuritiesISINCodeDTO> toDTOList(List<SecuritiesISINCode> all);
}
