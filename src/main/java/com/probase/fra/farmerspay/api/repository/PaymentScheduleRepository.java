package com.probase.fra.farmerspay.api.repository;

import com.probase.fra.farmerspay.api.models.FarmGroup;
import com.probase.fra.farmerspay.api.models.PaymentSchedule;
import com.probase.fra.farmerspay.api.models.WorkflowUser;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PaymentScheduleRepository extends JpaRepository<PaymentSchedule, Long>
{

    @Query("SELECT ps FROM PaymentSchedule ps WHERE ps.deletedAt IS NULL")
    List<PaymentSchedule> findAllPaymentSchedule(Pageable pageable);

    @Query("SELECT COUNT(*) as c FROM PaymentSchedule ps WHERE ps.deletedAt IS NULL")
    List<Integer> findAllPaymentScheduleCount();

}
