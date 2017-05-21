package com.checkmarx.teamcity.common.client.rest.dto;

/**
 * Created by: Dorg.
 * Date: 06/10/2016.
 */
public enum OSAScanStatusEnum {

    NOT_STARTED(0, "NotStarted"),
    IN_PROGRESS(1, "In progress"),
    SUCCEEDED(2, "Succeeded"),
    FAILED(3 ,"Failed");

    private int num;
    private String uiValue;

    public int getNum() {
        return num;
    }

    public String uiValue() {
        return uiValue;
    }

    OSAScanStatusEnum(int i, String uiValue) {
        this.num = i;
        this.uiValue = uiValue;
    }
}
