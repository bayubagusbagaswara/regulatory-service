package com.bayu.regulatory.service;

import com.bayu.regulatory.dto.RegulatoryDataChangeDTO;
import com.bayu.regulatory.model.RegulatoryDataChange;
import com.bayu.regulatory.model.enumerator.ApprovalStatus;

import java.util.List;

public interface RegulatoryDataChangeService {

    RegulatoryDataChange getById(Long id);

    List<RegulatoryDataChange> getAll();

    List<String> findAllMenu();

    String deleteAll();

    void setApprovalStatusIsRejected(RegulatoryDataChangeDTO dataChangeDTO, List<String> errorMessageList);

    void setApprovalStatusIsApproved(RegulatoryDataChangeDTO dataChangeDTO);

    <T> void createChangeActionAdd(RegulatoryDataChangeDTO dataChangeDTO, Class<T> clazz);

    <T> void createChangeActionEdit(RegulatoryDataChangeDTO dataChangeDTO, Class<T> clazz);

    <T> void createChangeActionDelete(RegulatoryDataChangeDTO dataChangeDTO, Class<T> clazz);

    Boolean existByIdListAndStatus(List<Long> idList, Long idListSize, ApprovalStatus approvalStatus);

    boolean existById(Long id);

    List<RegulatoryDataChange> findByMenuAndApprovalStatus(String menu, ApprovalStatus approvalStatus);

    void reject(Long id);

    List<RegulatoryDataChange> getAllByApprovalStatus(String approvalStatus);

    String deleteById(Long id);

}
