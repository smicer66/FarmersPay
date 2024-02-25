package com.probase.fra.farmerspay.api.models;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FarmDTO{
    private Farm farm;
    private String provinceName;
    private String districtName;

    public FarmDTO(Farm farm, String provinceName, String districtName)
    {
        this.farm = farm;
        this.provinceName = provinceName;
        this.districtName = districtName;
    }


}
