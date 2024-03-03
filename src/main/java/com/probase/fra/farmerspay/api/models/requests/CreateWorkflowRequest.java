package com.probase.fra.farmerspay.api.models.requests;


import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;


@Getter
@Setter
public class CreateWorkflowRequest {

    private List<CreateWorkflowRequestData> createWorkflowRequestData;


    
}
