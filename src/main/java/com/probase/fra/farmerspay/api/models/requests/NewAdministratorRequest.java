package com.probase.fra.farmerspay.api.models.requests;

import com.probase.fra.farmerspay.api.constraints.ValidPassword;
import com.probase.fra.farmerspay.api.enums.Gender;
import com.probase.fra.farmerspay.api.enums.UserRole;
import com.probase.fra.farmerspay.api.models.UserType;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;


@Getter
@Setter
public class NewAdministratorRequest {
    @NotBlank(message = "First name must be provided")
    private String firstName;
    @NotBlank(message = "Last name must be provided")
    private String lastName;
    private String otherName;
    @NotBlank(message = "Mobile number must be provided")
    private String mobileNumber;
    @NotBlank(message = "Email address must be provided")
    private String emailAddress;
    @NotBlank(message = "Date of birth must be provided")
    private LocalDate dateOfBirth;
    @NotBlank(message = "Gender must be provided")
    private Gender gender;
    @NotNull(message = "Missing role not specified")
    private Long userTypeId;

}
