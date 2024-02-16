package com.probase.fra.farmerspay.api.models.responses;



import com.probase.fra.farmerspay.api.enums.FarmersPayResponseCode;
import com.probase.fra.farmerspay.api.enums.UserRole;
import com.probase.fra.farmerspay.api.models.Farm;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class AuthenticateResponse {

    private List<Farm> farmList;
    private String token;
    private String message;
    private UserRole userRole;
    private FarmersPayResponseCode status;




}
