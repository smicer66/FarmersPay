package com.probase.fra.farmerspay.api.models.requests;


import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;


@Getter
@Setter
public class AddFarmerBankAccountRequest {
    @NotNull(message = "Your farm name must be provided")
    private Long farmId;
    @NotBlank(message = "Your farm address must be provided")
    private String bankAccountName;
    @NotBlank(message = "Your farm address must be provided")
    private String bankAccountNumber;
    @NotNull(message = "Specify the district your farm is situated in")
    private Long bankId;
}
