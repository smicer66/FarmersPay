package com.probase.fra.farmerspay.api.repository;

import com.probase.fra.farmerspay.api.models.Bank;
import com.probase.fra.farmerspay.api.models.Farm;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BankRepository extends JpaRepository<Bank, Long> {

    @Query("SELECT f FROM Bank f WHERE f.deletedAt IS NULL AND f.mobileMoney = :isMobileMoney")
    List<Bank> findMobileMoneyBanks(@Param("isMobileMoney") Boolean isMobileMoney);

    @Query("SELECT f FROM Bank f WHERE f.deletedAt IS NULL ORDER BY id ASC")
    List<Bank> findAllByPages(Pageable pageable);

}
