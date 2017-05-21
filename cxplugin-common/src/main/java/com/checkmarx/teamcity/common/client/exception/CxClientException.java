package com.checkmarx.teamcity.common.client.exception;

/**
 * Created by: Dorg.
 * Date: 15/09/2016.
 */
public class CxClientException extends Exception {

    public CxClientException() {
        super();
    }

    public CxClientException(String message) {
        super(message);
    }

    public CxClientException(String message, Throwable cause) {
        super(message, cause);
    }

    public CxClientException(Throwable cause) {
        super(cause);
    }


}
