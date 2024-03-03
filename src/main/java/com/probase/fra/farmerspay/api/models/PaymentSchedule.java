package com.probase.fra.farmerspay.api.models;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.probase.fra.farmerspay.api.deserializers.TimestampDeserializer;
import com.probase.fra.farmerspay.api.enums.ArtefactType;
import com.probase.fra.farmerspay.api.enums.PaymentScheduleStatus;
import com.probase.fra.farmerspay.api.enums.UserRole;
import com.probase.fra.farmerspay.api.serializer.JsonDateTimeSerializer;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;


@Entity
@Getter
@Setter
@Table(name = "payment_schedules")
public class PaymentSchedule implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable= false)
    private String scheduleMonth;
    @Column(nullable= false)
    private Integer scheduleYear;

    @Column(nullable= false)
    private Long createdByUserId;
    @Column(nullable= false)
    private String createdByUserName;
    @Column(nullable= false)
    @Enumerated(EnumType.STRING)
    private PaymentScheduleStatus paymentScheduleStatus;
    @Column(nullable= false)
    @JsonSerialize(using = JsonDateTimeSerializer.class)
    @JsonDeserialize(using = TimestampDeserializer.class)
    private LocalDateTime createdAt;
    @Column(nullable= true)
    private LocalDateTime deletedAt;
    @Column(nullable= false)
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}
