package com.checkmarx.teamcity.common.client;

import com.checkmarx.teamcity.common.client.exception.CxClientException;
import com.checkmarx.teamcity.common.client.rest.dto.OSAScanStatus;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

 /* Created by: Dorg.
 * Date: 28/09/2016.
 * */

public class OSAConsoleScanWaitHandler implements ScanWaitHandler<OSAScanStatus> {

    private static Logger log = LoggerFactory.getLogger(OSAConsoleScanWaitHandler.class);

    private long startTime;
    private long scanTimeoutInMin;

    public void onTimeout(OSAScanStatus scanStatus) throws CxClientException {

        String status =  scanStatus.getState() == null ? "Unknown" : scanStatus.getState().getName();

        String reason = "";
        if(scanStatus.getState() != null && !StringUtils.isEmpty(scanStatus.getState().getFailureReason())) {
            reason = "Reason: " + scanStatus.getState().getFailureReason();
        }

        throw new CxClientException("OSA scan has reached the time limit ("+scanTimeoutInMin+" minutes). status: ["+ status +"]. " +reason );
    }

    public void setLogger(Logger log) {
        OSAConsoleScanWaitHandler.log = log;
    }

    public void onFail(OSAScanStatus scanStatus) throws CxClientException {
        String reason = "";
        if(scanStatus.getState() != null && !StringUtils.isEmpty(scanStatus.getState().getFailureReason())) {
            reason = "Reason: " + scanStatus.getState().getFailureReason();
        }

        throw new CxClientException("OSA scan cannot be completed. status ["+scanStatus.getState().getName()+"]. "  + reason);
    }

    public void onIdle(OSAScanStatus scanStatus) {

        long hours = (System.currentTimeMillis() - startTime) / 3600000;
        long minutes = ((System.currentTimeMillis() - startTime) % 3600000) / 60000;
        long seconds = ((System.currentTimeMillis() - startTime) % 60000) / 1000;

        String hoursStr = (hours < 10)?("0" + Long.toString(hours)):(Long.toString(hours));
        String minutesStr = (minutes < 10)?("0" + Long.toString(minutes)):(Long.toString(minutes));
        String secondsStr = (seconds < 10)?("0" + Long.toString(seconds)):(Long.toString(seconds));

        log.info("Waiting for OSA scan results. " +
                "Elapsed time: " + hoursStr + ":" + minutesStr + ":" + secondsStr + ". " +
                "Status: " + scanStatus.getState().getName());

    }

    public void onSuccess(OSAScanStatus scanStatus) {
        log.info("OSA scan finished.");
    }

    public void onStart(long startTime, long scanTimeoutInMin) {
        this.startTime = startTime;
        this.scanTimeoutInMin = scanTimeoutInMin;
    }
}
