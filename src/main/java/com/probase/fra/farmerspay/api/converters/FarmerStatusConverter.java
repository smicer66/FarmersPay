package com.probase.fra.farmerspay.api.converters;


import com.probase.fra.farmerspay.api.enums.FarmStatus;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

@Converter
public class FarmerStatusConverter implements AttributeConverter<FarmStatus, String> {
    @Override
    public String convertToDatabaseColumn(FarmStatus farmerStatus) {
        return farmerStatus.value;
    }

    @Override
    public FarmStatus convertToEntityAttribute(String value) {
        Map<String, FarmStatus> userStatusMap = Arrays.stream(FarmStatus.values()).map(t -> {
            Object[] t1 = new Object[2];
            t1[0] = t.value;
            t1[1] = t;

            return t1;
        }).collect(Collectors.toMap(t -> (String)(t[0]), t -> (FarmStatus)(t[1])));

        return userStatusMap.get(value);
    }
}
