package com.probase.fra.farmerspay.api.models.requests;


import com.probase.fra.farmerspay.api.models.requests.listfarmsrequest.Search;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
public class ListFarmsRequest {
    private String draw;
    private Map<String, String> search;






}
