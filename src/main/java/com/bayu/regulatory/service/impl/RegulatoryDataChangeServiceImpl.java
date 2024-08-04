package com.bayu.regulatory.service.impl;

import com.bayu.regulatory.dto.RegulatoryDataChangeDTO;
import com.bayu.regulatory.model.RegulatoryDataChange;
import com.bayu.regulatory.model.enumerator.ApprovalStatus;
import com.bayu.regulatory.repository.RegulatoryDataChangeRepository;
import com.bayu.regulatory.service.RegulatoryDataChangeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class RegulatoryDataChangeServiceImpl implements RegulatoryDataChangeService {

    private final RegulatoryDataChangeRepository regulatoryDataChangeRepository;

    @Override
    public RegulatoryDataChange getById(Long id) {
        return null;
    }

    @Override
    public List<RegulatoryDataChange> getAll() {
        return List.of();
    }

    @Override
    public List<String> findAllMenu() {
        return List.of();
    }

    @Override
    public String deleteAll() {
        return "";
    }

    @Override
    public void setApprovalStatusIsRejected(RegulatoryDataChangeDTO dataChangeDTO, List<String> errorMessageList) {

    }

    @Override
    public void setApprovalStatusIsApproved(RegulatoryDataChangeDTO dataChangeDTO) {

    }

    @Override
    public <T> void createChangeActionAdd(RegulatoryDataChangeDTO dataChangeDTO, Class<T> clazz) {

    }

    @Override
    public <T> void createChangeActionEdit(RegulatoryDataChangeDTO dataChangeDTO, Class<T> clazz) {

    }

    @Override
    public <T> void createChangeActionDelete(RegulatoryDataChangeDTO dataChangeDTO, Class<T> clazz) {

    }

    @Override
    public Boolean existByIdListAndStatus(List<Long> idList, Long idListSize, ApprovalStatus approvalStatus) {
        return null;
    }

    @Override
    public boolean existById(Long id) {
        return false;
    }

    @Override
    public List<RegulatoryDataChange> findByMenuAndApprovalStatus(String menu, ApprovalStatus approvalStatus) {
        return List.of();
    }

    @Override
    public void reject(Long id) {

    }

    @Override
    public List<RegulatoryDataChange> getAllByApprovalStatus(String approvalStatus) {
        return List.of();
    }

    @Override
    public String deleteById(Long id) {
        return "";
    }

}
