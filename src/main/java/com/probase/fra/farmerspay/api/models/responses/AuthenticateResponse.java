package com.probase.fra.farmerspay.api.models.responses;



import com.probase.fra.farmerspay.api.enums.FarmersPayResponseCode;
import com.probase.fra.farmerspay.api.enums.UserRole;
import com.probase.fra.farmerspay.api.models.Farm;
import com.probase.fra.farmerspay.api.models.User;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;

@Getter
@Setter
public class AuthenticateResponse {

    private Map farmList;
    private String token;
    private String message;
    private UserRole userRole;
    private FarmersPayResponseCode status;
    private User user;




}
