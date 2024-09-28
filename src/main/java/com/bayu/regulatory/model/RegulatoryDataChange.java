package com.bayu.regulatory.model;

import com.bayu.regulatory.model.approval.RegulatoryApproval;
import com.bayu.regulatory.model.enumerator.ChangeAction;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "reg_data_change")
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class RegulatoryDataChange extends RegulatoryApproval {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "action")
    private ChangeAction action;

    @Column(name = "entity_class_name")
    private String entityClassName;

    @Column(name = "entity_id")
    private String entityId;

    @Column(name = "table_name")
    private String tableName;

    @Lob
    @Column(name = "json_data_before", columnDefinition = "TEXT")
    private String jsonDataBefore;

    @Lob
    @Column(name = "json_data_after", columnDefinition = "TEXT")
    private String jsonDataAfter;

    @Column(name = "description")
    private String description;

    @Column(name = "method")
    private String methodHttp;

    @Column(name = "endpoint")
    private String endpoint;

    @Column(name = "is_request_body")
    private Boolean isRequestBody;

    @Column(name = "is_request_param")
    private Boolean isRequestParam;

    @Column(name = "is_path_variable")
    private Boolean isPathVariable;

    @Column(name = "menu")
    private String menu;

}
