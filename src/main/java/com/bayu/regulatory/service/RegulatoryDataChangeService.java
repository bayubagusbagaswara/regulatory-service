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

    void setApprovalStatusIsRejected(RegulatoryDataChange dataChange, List<String> errorMessageList);

    void setApprovalStatusIsApproved(RegulatoryDataChange dataChange);

    <T> void createChangeActionAdd(RegulatoryDataChange dataChange, Class<T> clazz);

    <T> void createChangeActionEdit(RegulatoryDataChange dataChange, Class<T> clazz);

    <T> void createChangeActionDelete(RegulatoryDataChange dataChange, Class<T> clazz);

    Boolean existByIdListAndStatus(List<Long> idList, Long idListSize, ApprovalStatus approvalStatus);

    boolean existById(Long id);

    List<RegulatoryDataChange> findByMenuAndApprovalStatus(String menu, ApprovalStatus approvalStatus);

    void reject(Long id);

    List<RegulatoryDataChange> getAllByApprovalStatus(String approvalStatus);

    String deleteById(Long id);

}
