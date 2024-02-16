package com.probase.fra.farmerspay.api.models.requests;


import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;


@Getter
@Setter
public class NewBankRequest {
    @NotBlank(message = "Your bank name must be provided")
    private String bankName;
    @NotBlank(message = "Your bank code must be provided")
    private String bankCode;
    @NotNull(message = "Specify if this bank is a Mobile money provider")
    private Boolean isMobileMoney;
}
