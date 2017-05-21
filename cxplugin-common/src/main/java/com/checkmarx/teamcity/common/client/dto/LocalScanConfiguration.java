package com.checkmarx.teamcity.common.client.dto;

/**
 * Created by: Dorg.
 * Date: 15/09/2016.
 */
public class LocalScanConfiguration extends BaseScanConfiguration {

    private byte[] zippedSources;
    private String fileName;

    public byte[] getZippedSources() {
        return zippedSources;
    }

    public void setZippedSources(byte[] zippedSources) {
        this.zippedSources = zippedSources;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
}
