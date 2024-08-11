package com.bayu.regulatory.mapper;

import com.bayu.regulatory.dto.securitiesisincode.SecuritiesISINCodeDTO;
import com.bayu.regulatory.model.SecuritiesISINCode;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface SecuritiesISINCodeMapper {

    @Mapping(source = "externalCode2", target = "externalCode")
    SecuritiesISINCodeDTO toDTO(SecuritiesISINCode securitiesISINCode);

}
