package com.probase.fra.farmerspay.api.models;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDTO {
    private User user;
    private String userType;

    public UserDTO(User user, String userType)
    {
        this.user = user;
        this.userType = userType;
    }


}
