package com.probase.fra.farmerspay.api.models.requests;


import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;


@Getter
@Setter
public class UpdateUserStatusRequest {

    @NotNull(message = "Incomplete request parameters. New status not provided")
    @Pattern(regexp = "ACTIVE|SUSPENDED|DELETED|^\\s$", flags = Pattern.Flag.UNICODE_CASE, message = "Must be one of the following: ACTIVE, SUSPENDED, DELETED")
    private String status;

    @NotNull(message = "Specify the user identifier as this is missing")
    private Long userId;
}
