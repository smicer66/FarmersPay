package com.probase.fra.farmerspay.api.models.requests;


import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;


@Getter
@Setter
public class UpdateFarmRequest {
    @NotNull(message = "Farm identifier not found")
    private Long farmId;
    @NotBlank(message = "Your farm name must be provided")
    private String farmName;
    @NotBlank(message = "Your farm address must be provided")
    private String farmAddress;
    @NotNull(message = "Specify the district your farm is situated in")
    private Long farmDistrictId;
    @NotNull(message = "Specify the province your farm is situated in")
    private Long farmProvinceId;
}
