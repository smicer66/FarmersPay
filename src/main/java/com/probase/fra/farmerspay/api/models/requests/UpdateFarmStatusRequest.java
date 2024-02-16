package com.probase.fra.farmerspay.api.models.requests;


import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;


@Getter
@Setter
public class UpdateFarmStatusRequest {

    @NotNull(message = "Incomplete request parameters. Farm status not provided")
    @Pattern(regexp = "ACTIVE|APPROVED|SOLD|LIQUIDATED|DELETED|^\\s$", flags = Pattern.Flag.UNICODE_CASE, message = "Must be one of the following: ACTIVE, APPROVED, SOLD, LIQUIDATED, DELETED")
    private String status;

    @NotNull(message = "Specify the farm identifier as this is missing")
    private Long farmId;
}
