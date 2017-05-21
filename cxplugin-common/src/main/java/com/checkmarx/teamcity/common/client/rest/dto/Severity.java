package com.checkmarx.teamcity.common.client.rest.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Created by zoharby on 09/01/2017.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Severity {
    Integer Id;
    String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getId() {
        return Id;
    }

    public void setId(Integer id) {
        Id = id;
    }
}
