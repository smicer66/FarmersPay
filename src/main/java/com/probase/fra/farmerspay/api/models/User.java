package com.probase.fra.farmerspay.api.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.probase.fra.farmerspay.api.converters.UserStatusConverter;
import com.probase.fra.farmerspay.api.deserializers.DateDeserializer;
import com.probase.fra.farmerspay.api.deserializers.TimestampDeserializer;
import com.probase.fra.farmerspay.api.enums.Gender;
import com.probase.fra.farmerspay.api.enums.UserRole;
import com.probase.fra.farmerspay.api.enums.UserStatus;
import com.probase.fra.farmerspay.api.serializer.JsonDateSerializer;
import com.probase.fra.farmerspay.api.serializer.JsonDateTimeSerializer;
import lombok.Getter;
import lombok.Setter;


import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;


@Entity
@Getter
@Setter
@Table(name = "users")
@JsonIgnoreProperties(value = { "password", "hibernateLazyInitializer", "handler" })
public class User implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(nullable= false)
    private String firstName;
    @Column(nullable= false)
    private String lastName;
    @Column(nullable= true)
    private String otherNames;
    @Column(nullable= false)
    private String mobileNumber;
    @Column(nullable= false)
    private String username;
    @Column(nullable= false)
    private String password;
    @Column(nullable= false)
    @Enumerated(EnumType.STRING)
    private Gender gender;
    @Column(nullable= false)
    @Enumerated(EnumType.STRING)
    private UserRole userRole;

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

    @JsonSerialize(using = JsonDateSerializer.class)
    @JsonDeserialize(using = DateDeserializer.class)
    @Column(nullable= true)
    private LocalDate dateOfBirth;
    @Column(nullable= true)
    private String otp;

    @Column(nullable= false)
    @Enumerated(EnumType.STRING)
//    @Convert(converter = UserStatusConverter.class)
    private UserStatus userStatus;

    @Column(nullable= true)
    private Long userTypeId;


}
