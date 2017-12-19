package com.checkmarx.teamcity.agent.osa;

import java.io.IOException;

public class CxOSAException extends IOException {

    public CxOSAException() {
        super();
    }

    public CxOSAException(String message) {
        super(message);
    }

    public CxOSAException(String message, Throwable cause) {
        super(message, cause);
    }

    public CxOSAException(Throwable cause) {
        super(cause);
    }


}

