package com.checkmarx.teamcity.common.client.rest.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Created by: Dorg.
 * Date: 06/10/2016.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class CreateOSAScanResponse {

    private String scanId;

    public String getScanId() {
        return scanId;
    }

    public void setScanId(String scanId) {
        this.scanId = scanId;
    }
}
