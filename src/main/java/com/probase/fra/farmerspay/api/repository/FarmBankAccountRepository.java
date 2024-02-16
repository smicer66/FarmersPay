package com.probase.fra.farmerspay.api.repository;

import com.probase.fra.farmerspay.api.models.Farm;
import com.probase.fra.farmerspay.api.models.FarmBankAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface FarmBankAccountRepository extends JpaRepository<FarmBankAccount, Long> {

    @Query("SELECT f FROM FarmBankAccount f WHERE f.farmId = :farmId")
    List<FarmBankAccount> findFarmBankAccountByFarmId(@Param("farmId") Long farmId);

    @Query("SELECT f FROM FarmBankAccount f WHERE f.farmId = :farmId AND farmerUserId = :userId")
    List<FarmBankAccount> findFarmBankAccountByFarmIdAndUserId(Long farmId, Long userId);
}
