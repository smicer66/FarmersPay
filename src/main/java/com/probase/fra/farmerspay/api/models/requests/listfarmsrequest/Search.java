package com.probase.fra.farmerspay.api.models.requests.listfarmsrequest;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Search{
    private String value;
    private String regex;
}
