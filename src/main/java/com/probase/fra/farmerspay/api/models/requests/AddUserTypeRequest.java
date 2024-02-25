package com.probase.fra.farmerspay.api.models.requests;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class AddUserTypeRequest {

    private List<String> userTypes;
}
