package com.bayu.regulatory.repository;

import com.bayu.regulatory.model.LKPBUSampleData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LKPBUSampleDataRepository extends JpaRepository<LKPBUSampleData, Long> {

    @Query(value = "FROM LKPBUSampleData l WHERE l.month = :monthName and l.year = :year")
    List<LKPBUSampleData> findAllByMonthAndYear(
            @Param("monthName") String monthName,
            @Param("year") Integer year
    );

}
