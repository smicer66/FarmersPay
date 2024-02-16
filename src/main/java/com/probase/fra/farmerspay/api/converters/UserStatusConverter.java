package com.probase.fra.farmerspay.api.converters;


import com.probase.fra.farmerspay.api.enums.UserStatus;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Converter
public class UserStatusConverter implements AttributeConverter<UserStatus, String> {
    @Override
    public String convertToDatabaseColumn(UserStatus userStatus) {
        return userStatus.value;
    }

    @Override
    public UserStatus convertToEntityAttribute(String value) {
        Map<String, UserStatus> userStatusMap = Arrays.stream(UserStatus.values()).map(t -> {
            Object[] t1 = new Object[2];
            t1[0] = t.value;
            t1[1] = t;

            return t1;
        }).collect(Collectors.toMap(t -> (String)(t[0]), t -> (UserStatus)(t[1])));

        return userStatusMap.get(value);
    }
}
