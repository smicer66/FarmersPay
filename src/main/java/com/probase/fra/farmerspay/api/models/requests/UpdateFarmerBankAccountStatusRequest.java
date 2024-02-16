package com.probase.fra.farmerspay.api.models.requests;


import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;


@Getter
@Setter
public class UpdateFarmerBankAccountStatusRequest {
    @NotNull(message = "Invalid bank account selected for an update")
    private Long farmBankAccountId;
    @NotBlank(message = "Specify the new bank account status")
    private String bankAccountStatus;
}
