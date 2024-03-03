package com.probase.fra.farmerspay.api.models.requests;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;



@Getter
@Setter
public class CreateWorkflowRequestData{
    @NotBlank(message = "Your farm name must be provided")
    private Long userId;
    @NotBlank(message = "Your farm address must be provided")
    private Integer level;
    @NotNull(message = "Specify the district your farm is situated in")
    private List<String> permission;

}
