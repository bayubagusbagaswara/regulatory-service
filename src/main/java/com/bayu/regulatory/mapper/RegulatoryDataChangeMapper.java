package com.bayu.regulatory.mapper;

import com.bayu.regulatory.dto.RegulatoryDataChangeDTO;
import com.bayu.regulatory.model.RegulatoryDataChange;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface RegulatoryDataChangeMapper {

    RegulatoryDataChangeDTO toDTO(RegulatoryDataChange regulatoryDataChange);

    RegulatoryDataChange toModel(RegulatoryDataChangeDTO regulatoryDataChangeDTO);

    List<RegulatoryDataChangeDTO> toDTOList(List<RegulatoryDataChange> regulatoryDataChanges);

}
