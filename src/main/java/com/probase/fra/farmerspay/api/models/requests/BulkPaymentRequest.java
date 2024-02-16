package com.probase.fra.farmerspay.api.models.requests;


import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Getter
@Setter
public class BulkPaymentRequest {
    @NotNull(message = "The amount to pay all farmers must be provided")
    @DecimalMin(value = "0.10", message = "Please Enter a valid Amount. Minimum amount acceptable is 0.10")
    private BigDecimal amount;


    @NotBlank(message = "Provide a description of this bulk payment")
    private String paymentDescription;
}
