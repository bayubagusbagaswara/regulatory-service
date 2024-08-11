package com.bayu.regulatory.dto;

import com.bayu.regulatory.model.enumerator.ApprovalStatus;
import com.bayu.regulatory.model.enumerator.ChangeAction;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RegulatoryDataChangeDTO {

    private Long id;

    private ApprovalStatus approvalStatus;

    private String inputId;
    private LocalDateTime inputDate;
    private String inputIPAddress;

    private String approveId;
    private LocalDateTime approveDate;
    private String approveIPAddress;

    private ChangeAction action;

    private String entityClassName;

    private String entityId;

    private String tableName;

    private String jsonDataBefore;

    private String jsonDataAfter;

    private String description;

    private String methodHttp;

    private String endpoint;

    private Boolean isRequestBody;

    private Boolean isRequestParam;

    private Boolean isPathVariable;

    private String menu;

    public RegulatoryDataChangeDTO(String inputIPAddress, String methodHttp, String endpoint, Boolean isRequestBody, Boolean isRequestParam, Boolean isPathVariable, String menu) {
        this.inputIPAddress = inputIPAddress;
        this.methodHttp = methodHttp;
        this.endpoint = endpoint;
        this.isRequestBody = isRequestBody;
        this.isRequestParam = isRequestParam;
        this.isPathVariable = isPathVariable;
        this.menu = menu;
    }
}
