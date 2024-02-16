package com.probase.fra.farmerspay.api.models.requests;

import com.probase.fra.farmerspay.api.constraints.ValidPassword;
import com.probase.fra.farmerspay.api.converters.UserStatusConverter;
import com.probase.fra.farmerspay.api.enums.Gender;
import com.probase.fra.farmerspay.api.enums.UserRole;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;


@Getter
@Setter
public class RegisterRequest {
    @NotBlank(message = "First name must be provided")
    private String firstName;
    @NotBlank(message = "Last name must be provided")
    private String lastName;
    private String otherNames;
    @NotBlank(message = "Mobile number must be provided")
    private String mobileNumber;
    @NotBlank(message = "Password must be provided")
    @ValidPassword
    private String password;
    @NotBlank(message = "Gender must be provided")
    private Gender gender;
    @NotBlank(message = "Missing role not specified")
    private UserRole userRole;

    @NotBlank(message = "Date of birth must be provided")
    private LocalDate dateOfBirth;
}
