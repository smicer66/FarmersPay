package com.probase.fra.farmerspay.api.models;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.math.BigDecimal;
import java.time.LocalDateTime;


@Entity
@Getter
@Setter
@Table(name = "payment_schedule_breakdowns")
@JsonIgnoreProperties(value = { "hibernateLazyInitializer", "handler" })
public class PaymentScheduleBreakdown {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable= false)
    private Long farmGroupId;
    @Column(nullable= false)
    private String farmGroupName;
    @Column(nullable= false)
    private BigDecimal amountToPay;
    @Column(nullable= false)
    private Long paymentScheduleId;
    @Column(nullable= false)
    private LocalDateTime createdAt;
    @Column(nullable= true)
    private LocalDateTime deletedAt;
    @Column(nullable= false)
    private LocalDateTime updatedAt;
}
