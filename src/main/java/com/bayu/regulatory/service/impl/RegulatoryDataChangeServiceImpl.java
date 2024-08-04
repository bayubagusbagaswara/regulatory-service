package com.bayu.regulatory.service.impl;

import com.bayu.regulatory.dto.RegulatoryDataChangeDTO;
import com.bayu.regulatory.exception.DataNotFoundException;
import com.bayu.regulatory.exception.ParseValueException;
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

import static com.bayu.regulatory.model.enumerator.ApprovalStatus.*;
import static com.bayu.regulatory.model.enumerator.ChangeAction.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class RegulatoryDataChangeServiceImpl implements RegulatoryDataChangeService {

    private static final String ERROR_MESSAGE_NOT_FOUND_ID = "Data Change not found with id: ";

    private final RegulatoryDataChangeRepository regulatoryDataChangeRepository;

    @Override
    public RegulatoryDataChange getById(Long id) {
        log.info("Start regulatory get data change by id");
        return regulatoryDataChangeRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException(ERROR_MESSAGE_NOT_FOUND_ID + id));
    }

    @Override
    public List<RegulatoryDataChange> getAll() {
        log.info("Start get all regulatory data change");
        return regulatoryDataChangeRepository.findAll();
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
    public void setApprovalStatusIsRejected(RegulatoryDataChangeDTO dataChangeDTO, List<String> errorMessageList) {
        log.info("Start set approval status is Rejected");
        RegulatoryDataChange regulatoryDataChange = getById(dataChangeDTO.getId());

        regulatoryDataChange.setApprovalStatus(REJECTED);

        regulatoryDataChange.setApproveId(dataChangeDTO.getApproveId());
        regulatoryDataChange.setApproveIPAddress(dataChangeDTO.getApproveIPAddress());
        regulatoryDataChange.setApproveDate(dataChangeDTO.getApproveDate() == null ? LocalDateTime.now() : dataChangeDTO.getApproveDate());

        regulatoryDataChange.setJsonDataAfter(dataChangeDTO.getJsonDataAfter() == null ? "" : dataChangeDTO.getJsonDataAfter());
        regulatoryDataChange.setJsonDataBefore(dataChangeDTO.getJsonDataBefore() == null ? "" : dataChangeDTO.getJsonDataBefore());

        regulatoryDataChange.setEntityId(dataChangeDTO.getEntityId() == null ? "" : dataChangeDTO.getEntityId());
        regulatoryDataChange.setDescription(StringUtil.joinStrings(errorMessageList));

        RegulatoryDataChange save = regulatoryDataChangeRepository.save(regulatoryDataChange);
        log.info("Successfully set approval status Rejected with id: {}", save.getId());

    }

    @Override
    public void setApprovalStatusIsApproved(RegulatoryDataChangeDTO dataChangeDTO) {
        log.info("Start set approval status is Approved");
        RegulatoryDataChange regulatoryDataChange = getById(dataChangeDTO.getId());

        regulatoryDataChange.setApprovalStatus(APPROVED);

        regulatoryDataChange.setApproveId(dataChangeDTO.getApproveId());
        regulatoryDataChange.setApproveIPAddress(dataChangeDTO.getApproveIPAddress());
        regulatoryDataChange.setApproveDate(dataChangeDTO.getApproveDate() == null ? LocalDateTime.now() : dataChangeDTO.getApproveDate());

        regulatoryDataChange.setJsonDataAfter(dataChangeDTO.getJsonDataAfter() == null ? "" : dataChangeDTO.getJsonDataAfter());
        regulatoryDataChange.setJsonDataBefore(dataChangeDTO.getJsonDataBefore() == null ? "" : dataChangeDTO.getJsonDataBefore());

        regulatoryDataChange.setEntityId(dataChangeDTO.getEntityId());
        regulatoryDataChange.setDescription(dataChangeDTO.getDescription());

        RegulatoryDataChange save = regulatoryDataChangeRepository.save(regulatoryDataChange);
        log.info("Successfully set approval status Approved with id: {}", save.getId());
    }

    @Override
    public <T> void createChangeActionAdd(RegulatoryDataChangeDTO dataChangeDTO, Class<T> clazz) {
        RegulatoryDataChange regulatoryDataChange = RegulatoryDataChange.builder()
                .approvalStatus(ApprovalStatus.PENDING)
                .inputId(dataChangeDTO.getInputId())
                .inputDate(LocalDateTime.now())
                .inputIPAddress(dataChangeDTO.getInputIPAddress())

                .action(ADD)
                .entityId("")
                .entityClassName(clazz.getName())
                .tableName(TableNameResolver.getTableName(clazz))

                .jsonDataBefore("")
                .jsonDataAfter(dataChangeDTO.getJsonDataAfter())
                .description("")

                .methodHttp(dataChangeDTO.getMethodHttp())
                .endpoint(dataChangeDTO.getEndpoint())
                .isRequestBody(dataChangeDTO.isRequestBody())
                .isRequestParam(dataChangeDTO.isRequestParam())
                .isPathVariable(dataChangeDTO.isPathVariable())
                .menu(dataChangeDTO.getMenu())
                .build();

        RegulatoryDataChange save = regulatoryDataChangeRepository.save(regulatoryDataChange);
        log.info("Successfully save create change action Add with id: {}", save.getId());
    }

    @Override
    public <T> void createChangeActionEdit(RegulatoryDataChangeDTO dataChangeDTO, Class<T> clazz) {
        RegulatoryDataChange regulatoryDataChange = RegulatoryDataChange.builder()
                .approvalStatus(PENDING)

                .inputId(dataChangeDTO.getInputId())
                .inputDate(LocalDateTime.now())
                .inputIPAddress(dataChangeDTO.getInputIPAddress())

                .action(EDIT)
                .entityId(dataChangeDTO.getEntityId())
                .entityClassName(clazz.getName())
                .tableName(TableNameResolver.getTableName(clazz))

                .jsonDataBefore(dataChangeDTO.getJsonDataBefore())
                .jsonDataAfter(dataChangeDTO.getJsonDataAfter())
                .description("")

                .methodHttp(dataChangeDTO.getMethodHttp())
                .endpoint(dataChangeDTO.getEndpoint())
                .isRequestBody(dataChangeDTO.isRequestBody())
                .isRequestParam(dataChangeDTO.isRequestParam())
                .isPathVariable(dataChangeDTO.isPathVariable())
                .menu(dataChangeDTO.getMenu())
                .build();
        RegulatoryDataChange save = regulatoryDataChangeRepository.save(regulatoryDataChange);
        log.info("Successfully save create change action Edit with id: {}", save.getId());
    }

    @Override
    public <T> void createChangeActionDelete(RegulatoryDataChangeDTO dataChangeDTO, Class<T> clazz) {
        RegulatoryDataChange regulatoryDataChange = RegulatoryDataChange.builder()
                .approvalStatus(PENDING)

                .inputId(dataChangeDTO.getInputId())
                .inputDate(LocalDateTime.now())
                .inputIPAddress(dataChangeDTO.getInputIPAddress())

                .action(DELETE)
                .entityId(dataChangeDTO.getEntityId())
                .entityClassName(clazz.getName())
                .tableName(TableNameResolver.getTableName(clazz))

                .jsonDataBefore(dataChangeDTO.getJsonDataBefore())
                .jsonDataAfter(dataChangeDTO.getJsonDataAfter())
                .description("")

                .methodHttp(dataChangeDTO.getMethodHttp())
                .endpoint(dataChangeDTO.getEndpoint())
                .isRequestBody(dataChangeDTO.isRequestBody())
                .isRequestParam(dataChangeDTO.isRequestParam())
                .isPathVariable(dataChangeDTO.isPathVariable())
                .methodHttp(dataChangeDTO.getMenu())
                .build();

        RegulatoryDataChange save = regulatoryDataChangeRepository.save(regulatoryDataChange);
        log.info("Successfully save create change action Delete with id: {}", save.getId());
    }

    @Override
    public Boolean existByIdListAndStatus(List<Long> idList, Long idListSize, ApprovalStatus approvalStatus) {
        Boolean existsByIdListAndStatus = regulatoryDataChangeRepository.existsByIdListAndStatus(idList, idListSize, approvalStatus);
        log.info("Status exist by id list: {}", existsByIdListAndStatus);
        return existsByIdListAndStatus;
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
