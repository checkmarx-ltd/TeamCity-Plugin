package com.checkmarx.teamcity.common;

import java.io.IOException;


/**
 * CxAbortException is used for errors that aborts the current operation
 * Corresponds to AbortException in the Jenkins plugin
 */

public final class CxAbortException extends IOException {
    public CxAbortException(String message) {
        super(message);
    }
}
