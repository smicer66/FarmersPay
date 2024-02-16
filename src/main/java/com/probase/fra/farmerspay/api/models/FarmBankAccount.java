package com.probase.fra.farmerspay.api.models;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.probase.fra.farmerspay.api.deserializers.TimestampDeserializer;
import com.probase.fra.farmerspay.api.enums.FarmBankAccountStatus;
import com.probase.fra.farmerspay.api.enums.FarmStatus;
import com.probase.fra.farmerspay.api.serializer.JsonDateTimeSerializer;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;


@Entity
@Getter
@Setter
@Table(name = "farm_bank_accounts")
public class FarmBankAccount implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(nullable= false)
    private Long farmId;
    @Column(nullable= false)
    private String bankAccountName;
    @Column(nullable= true)
    private String bankAccountNumber;
    @Column(nullable= false)
    private Long bankId;

    @JsonSerialize(using = JsonDateTimeSerializer.class)
    @JsonDeserialize(using = TimestampDeserializer.class)
    @Column(nullable= false)
    private LocalDateTime createdAt;

    @JsonSerialize(using = JsonDateTimeSerializer.class)
    @JsonDeserialize(using = TimestampDeserializer.class)
    @Column(nullable= true)
    private LocalDateTime deletedAt;

    @JsonSerialize(using = JsonDateTimeSerializer.class)
    @JsonDeserialize(using = TimestampDeserializer.class)
    @Column(nullable= false)
    private LocalDateTime updatedAt;

    @Column(nullable= false)
    @Enumerated(EnumType.STRING)
//    @Convert(converter = FarmerStatusConverter.class)
    private FarmBankAccountStatus farmBankAccountStatus;
    @Column(nullable= false)
    private Long farmerUserId;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}
