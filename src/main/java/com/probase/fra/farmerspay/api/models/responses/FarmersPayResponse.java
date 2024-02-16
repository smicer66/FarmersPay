package com.probase.fra.farmerspay.api.models.responses;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class FarmersPayResponse implements Serializable {

    private String responseCode;
    private String message;
    private Object responseData;
}
