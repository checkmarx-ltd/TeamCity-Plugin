package com.checkmarx.teamcity.common.client.dto;

import com.cx.restclient.osa.dto.OSAResults;
import com.cx.restclient.sast.dto.SASTResults;

public class ScanResults{

    private SASTResults sastResults;
    private OSAResults osaResults;

    private Exception sastCreateException = null;
    private Exception sastWaitException = null;
    private Exception osaCreateException = null;
    private Exception osaWaitException = null;

    public ScanResults() {
    }

    public ScanResults(SASTResults sastResults, OSAResults osaResults) {
        this.sastResults = sastResults;
        this.osaResults = osaResults;
    }

    public SASTResults getSastResults() {
        return sastResults;
    }

    public void setSastResults(SASTResults sastResults) {
        this.sastResults = sastResults;
    }

    public OSAResults getOsaResults() {
        return osaResults;
    }

    public void setOsaResults(OSAResults osaResults) {
        this.osaResults = osaResults;
    }

    public Exception getSastCreateException() {
        return sastCreateException;
    }

    public void setSastCreateException(Exception sastCreateException) {
        this.sastCreateException = sastCreateException;
    }

    public Exception getSastWaitException() {
        return sastWaitException;
    }

    public void setSastWaitException(Exception sastWaitException) {
        this.sastWaitException = sastWaitException;
    }

    public Exception getOsaCreateException() {
        return osaCreateException;
    }

    public void setOsaCreateException(Exception osaCreateException) {
        this.osaCreateException = osaCreateException;
    }

    public Exception getOsaWaitException() {
        return osaWaitException;
    }

    public void setOsaWaitException(Exception osaWaitException) {
        this.osaWaitException = osaWaitException;
    }
}
