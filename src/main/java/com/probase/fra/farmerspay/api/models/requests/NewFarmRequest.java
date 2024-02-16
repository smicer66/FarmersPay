package com.probase.fra.farmerspay.api.models.requests;



import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;


@Getter
@Setter
public class NewFarmRequest {
    @NotBlank(message = "Your farm name must be provided")
    private String farmName;
    @NotBlank(message = "Your farm address must be provided")
    private String farmAddress;
    @NotNull(message = "Specify the district your farm is situated in")
    private Long farmDistrictId;
    @NotNull(message = "Specify the province your farm is situated in")
    private Long farmProvinceId;
}
