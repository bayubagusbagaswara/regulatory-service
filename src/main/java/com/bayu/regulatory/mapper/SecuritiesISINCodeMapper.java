package com.bayu.regulatory.mapper;

import com.bayu.regulatory.dto.securitiesisincode.SecuritiesISINCodeDTO;
import com.bayu.regulatory.model.SecuritiesISINCode;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface SecuritiesISINCodeMapper {

    SecuritiesISINCodeDTO toDTO(SecuritiesISINCode securitiesISINCode);

}
