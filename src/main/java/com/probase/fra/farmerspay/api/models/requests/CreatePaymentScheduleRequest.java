package com.probase.fra.farmerspay.api.models.requests;


import com.probase.fra.farmerspay.api.models.PaymentScheduleBreakdown;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
public class CreatePaymentScheduleRequest {

    @NotBlank(message = "Incomplete request parameters. The scheduled payment month must be provided")
    private String scheduleMonth;
    @NotNull(message = "Incomplete request parameters. The scheduled payment year must be provided")
    private Integer scheduleYear;
    @NotNull(message = "Incomplete request parameters. The payment schedule breakdown must be provided")
    @Size(min = 1, message = "Incomplete request parameters. The payment schedule breakdown must be provided")
    private List<PaymentScheduleBreakdownRequest> paymentScheduleBreakdownRequest;
}
