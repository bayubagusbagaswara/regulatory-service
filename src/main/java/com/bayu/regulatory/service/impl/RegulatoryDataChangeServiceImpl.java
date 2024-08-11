package com.bayu.regulatory.service.impl;

import com.bayu.regulatory.dto.RegulatoryDataChangeDTO;
import com.bayu.regulatory.exception.DataNotFoundException;
import com.bayu.regulatory.exception.ParseValueException;
import com.bayu.regulatory.mapper.RegulatoryDataChangeMapper;
import com.bayu.regulatory.model.RegulatoryDataChange;
import com.bayu.regulatory.model.enumerator.ApprovalStatus;
import com.bayu.regulatory.repository.RegulatoryDataChangeRepository;
import com.bayu.regulatory.service.RegulatoryDataChangeService;
import com.bayu.regulatory.util.StringUtil;
import com.bayu.regulatory.util.TableNameResolver;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.bayu.regulatory.model.enumerator.ApprovalStatus.*;
import static com.bayu.regulatory.model.enumerator.ChangeAction.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class RegulatoryDataChangeServiceImpl implements RegulatoryDataChangeService {

    private static final String ERROR_MESSAGE_NOT_FOUND_ID = "Data Change not found with id: ";

    private final RegulatoryDataChangeRepository regulatoryDataChangeRepository;
    private final RegulatoryDataChangeMapper regulatoryDataChangeMapper;

    @Override
    public RegulatoryDataChange getById(Long id) {
        log.info("Start regulatory get data change by id");
        return regulatoryDataChangeRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException(ERROR_MESSAGE_NOT_FOUND_ID + id));
    }

    @Override
    public List<RegulatoryDataChangeDTO> getAll() {
        log.info("Start get all regulatory data change");
        List<RegulatoryDataChange> collect = regulatoryDataChangeRepository.findAll().stream()
                .sorted((e1, e2) -> e2.getId().compareTo(e1.getId()))  // Descending order by id
                .toList();
        return regulatoryDataChangeMapper.toDTOList(collect);
    }

    @Override
    public List<String> findAllMenu() {
        log.info("Start get all menu regulatory data change");
        return regulatoryDataChangeRepository.findAllMenu();
    }

    @Override
    public String deleteAll() {
        log.info("Start delete all regulatory data change");
        regulatoryDataChangeRepository.deleteAll();
        return "Successfully delete all regulatory data change from table";
    }

    @Override
    public synchronized void setApprovalStatusIsRejected(RegulatoryDataChange dataChange, List<String> errorMessageList) {
        log.info("Start set approval status is Rejected");
        RegulatoryDataChange regulatoryDataChange = getById(dataChange.getId());

        regulatoryDataChange.setApprovalStatus(REJECTED);

        regulatoryDataChange.setApproveId(dataChange.getApproveId());
        regulatoryDataChange.setApproveIPAddress(dataChange.getApproveIPAddress());
        regulatoryDataChange.setApproveDate(
                Optional.ofNullable(dataChange.getApproveDate()).orElse(LocalDateTime.now())
        );
        regulatoryDataChange.setJsonDataAfter(
                Optional.ofNullable(dataChange.getJsonDataAfter()).orElse("")
        );
        regulatoryDataChange.setJsonDataBefore(
                Optional.ofNullable(dataChange.getJsonDataBefore()).orElse("")
        );
        regulatoryDataChange.setEntityId(
                Optional.ofNullable(dataChange.getEntityId()).orElse("")
        );
        regulatoryDataChange.setDescription(StringUtil.joinStrings(errorMessageList));

        RegulatoryDataChange save = regulatoryDataChangeRepository.save(regulatoryDataChange);
        log.info("Successfully set approval status Rejected with id: {}", save.getId());

    }

    @Override
    public void setApprovalStatusIsApproved(RegulatoryDataChange dataChange) {
        log.info("Start set approval status is Approved");
        RegulatoryDataChange regulatoryDataChange = getById(dataChange.getId());

        regulatoryDataChange.setApprovalStatus(APPROVED);

        regulatoryDataChange.setApproveId(dataChange.getApproveId());
        regulatoryDataChange.setApproveIPAddress(dataChange.getApproveIPAddress());
        regulatoryDataChange.setApproveDate(
                Optional.ofNullable(dataChange.getApproveDate()).orElse(LocalDateTime.now())
        );
        regulatoryDataChange.setJsonDataAfter(
                Optional.ofNullable(dataChange.getJsonDataAfter()).orElse("")
        );
        regulatoryDataChange.setJsonDataBefore(
                Optional.ofNullable(dataChange.getJsonDataBefore()).orElse("")
        );
        regulatoryDataChange.setEntityId(dataChange.getEntityId());
        regulatoryDataChange.setDescription(dataChange.getDescription());

        RegulatoryDataChange save = regulatoryDataChangeRepository.save(regulatoryDataChange);
        log.info("Successfully set approval status Approved with id: {}", save.getId());
    }

    @Override
    public <T> void createChangeActionAdd(RegulatoryDataChange dataChange, Class<T> clazz) {
        // Property of Approval
        dataChange.setApprovalStatus(PENDING);
        dataChange.setInputDate(LocalDateTime.now());

        // Property of Data Change
        dataChange.setAction(ADD);
        dataChange.setEntityId("");
        dataChange.setEntityClassName(clazz.getName());
        dataChange.setTableName(TableNameResolver.getTableName(clazz));
        dataChange.setJsonDataBefore("");
        dataChange.setDescription("");

        RegulatoryDataChange save = regulatoryDataChangeRepository.save(dataChange);
        log.info("Successfully save create change action Add with id: {}", save.getId());
    }

    @Override
    public <T> void createChangeActionEdit(RegulatoryDataChange dataChange, Class<T> clazz) {
        log.info("Start create change action Edit: {}", dataChange);
        // Properties of Approval
        dataChange.setApprovalStatus(PENDING);
        dataChange.setInputDate(LocalDateTime.now());

        // Properties of Data Change
        dataChange.setAction(EDIT);
        dataChange.setEntityClassName(clazz.getName());
        dataChange.setTableName(TableNameResolver.getTableName(clazz));
        dataChange.setDescription("");

        RegulatoryDataChange save = regulatoryDataChangeRepository.save(dataChange);
        log.info("Successfully save create change action Edit with id: {}", save.getId());
    }

    @Override
    public <T> void createChangeActionDelete(RegulatoryDataChange dataChange, Class<T> clazz) {
        log.info("Start create change action Delete: {}", dataChange);
        // Properties of Approval
        dataChange.setApprovalStatus(PENDING);
        dataChange.setInputDate(LocalDateTime.now());

        // Properties of Data Change
        dataChange.setAction(DELETE);
        dataChange.setEntityClassName(clazz.getName());
        dataChange.setTableName(TableNameResolver.getTableName(clazz));
        dataChange.setDescription("");

        RegulatoryDataChange save = regulatoryDataChangeRepository.save(dataChange);
        log.info("Successfully save create change action Delete with id: {}", save.getId());
    }

    @Override
    public Boolean existByIdListAndStatus(List<Long> idList, Long idListSize, ApprovalStatus approvalStatus) {
//        Boolean existsByIdListAndStatus = regulatoryDataChangeRepository.existsByIdListAndStatus(idList, idListSize, approvalStatus);
//        log.info("Status exist by id list: {}", existsByIdListAndStatus);
//        return existsByIdListAndStatus;
        return false;
    }

    @Override
    public boolean existById(Long id) {
        boolean existsById = regulatoryDataChangeRepository.existsById(id);
        log.info("Exist by id: {}", existsById);
        return existsById;
    }

    @Override
    public List<RegulatoryDataChange> findByMenuAndApprovalStatus(String menu, ApprovalStatus approvalStatus) {
        log.info("Start get all regulatory data change by menu and approval status");
        List<RegulatoryDataChange> menuAndApprovalStatus = regulatoryDataChangeRepository.findByMenuAndApprovalStatus(menu, approvalStatus);
        log.info("Size get all regulatory data change: {}", menuAndApprovalStatus.size());
        return menuAndApprovalStatus;
    }

    @Override
    public void reject(Long id) {
        log.info("Start reject process for regulatory data change by id: {}", id);
        RegulatoryDataChange regulatoryDataChange = getById(id);
        regulatoryDataChange.setApprovalStatus(REJECTED);
        RegulatoryDataChange save = regulatoryDataChangeRepository.save(regulatoryDataChange);
        log.info("Successfully update regulatory data change approval status is Rejected by id: {}", save.getId());
    }

    @Override
    public List<RegulatoryDataChange> getAllByApprovalStatus(String approvalStatus) {
        log.info("Start get all regulatory data change by approval status: {}", approvalStatus);
        String approvalStatusEnum = "";
        if (PENDING.name().equalsIgnoreCase(approvalStatus)) {
            approvalStatusEnum  = PENDING.name();
        } else if (APPROVED.name().equalsIgnoreCase(approvalStatus)) {
            approvalStatusEnum = APPROVED.name();
        } else if (REJECTED.name().equalsIgnoreCase(approvalStatus)) {
            approvalStatusEnum = REJECTED.name();
        }

        if (approvalStatusEnum.isEmpty()) {
            throw new ParseValueException("Approval status enum not matching: " + approvalStatusEnum);
        }

        return regulatoryDataChangeRepository.findAllByApprovalStatus(approvalStatusEnum);
    }

    @Override
    public String deleteById(Long id) {
        log.info("Start delete regulatory data change by id: {}", id);
        RegulatoryDataChange regulatoryDataChange = getById(id);
        regulatoryDataChangeRepository.delete(regulatoryDataChange);
        return "Successfully delete regulatory data change with id: " + id;
    }

}
