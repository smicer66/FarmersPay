package com.probase.fra.farmerspay.api.models.requests;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;


@Getter
@Setter
public class PaymentScheduleBreakdownRequest {
    @Column(nullable= false)
    private Long farmGroupId;
    @Column(nullable= false)
    private BigDecimal amountToPay;
}
