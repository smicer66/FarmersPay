package com.probase.fra.farmerspay.api.repository;

import com.probase.fra.farmerspay.api.models.PaymentSchedule;
import com.probase.fra.farmerspay.api.models.PaymentScheduleBreakdown;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PaymentScheduleBreakdownRepository extends JpaRepository<PaymentScheduleBreakdown, Long>
{


    @Query("SELECT ps FROM PaymentScheduleBreakdown ps WHERE ps.paymentScheduleId = :paymentScheduleId AND ps.deletedAt IS NULL")
    List<PaymentScheduleBreakdown> getPaymentScheduleBreakdownByPayScheduleId(Long paymentScheduleId, Pageable pageable);

    @Query("SELECT COUNT(*) as c FROM PaymentScheduleBreakdown ps WHERE ps.paymentScheduleId = :paymentScheduleId AND ps.deletedAt IS NULL")
    List<Integer> findPaymentScheduleBreakdownByScheduleIdCount(Long paymentScheduleId);
}
