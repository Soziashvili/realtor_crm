package ru.realtorcrm.core_crm.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.realtorcrm.core_crm.entity.Building;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface BuildingRepository extends JpaRepository<Building, Long> {

    @Query("select exists (select b from Building b where b.cadastre = :cadastre)")
    boolean isCadastreExists(String cadastre);

    @Query("select b from Building b where b.lastContactDate = :thresholdDate or b.lastContactDate is null")
    List<Building> findByLastContactDateBeforeOrLastContactDateIsNull(LocalDate thresholdDate);
}
