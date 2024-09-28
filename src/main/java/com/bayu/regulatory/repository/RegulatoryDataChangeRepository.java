package com.bayu.regulatory.repository;

import com.bayu.regulatory.model.RegulatoryDataChange;
import com.bayu.regulatory.model.enumerator.ApprovalStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RegulatoryDataChangeRepository extends JpaRepository<RegulatoryDataChange, Long> {

    @Query(value = "SELECT CASE WHEN COUNT(*) = :listSize THEN 1 ELSE 0 END " +
            "FROM reg_data_change WHERE id IN :idList", nativeQuery = true)
    Boolean existsByIdList(@Param("idList") List<Long> idList, @Param("listSize") Integer listSize);

    List<RegulatoryDataChange> findByIdIn(List<Long> idList);

    List<RegulatoryDataChange> findByMenuAndApprovalStatus(String menu, ApprovalStatus approvalStatus);

    List<RegulatoryDataChange> findAllByApprovalStatus(String approvalStatus);

    @Query(value = "SELECT DISTINCT menu FROM reg_data_change WHERE ISNULL(menu, '') != '' ORDER BY menu ASC", nativeQuery = true)
    List<String> findAllMenu();

}
