package com.checkmarx.teamcity.common;

import java.net.MalformedURLException;
import java.net.URL;


/**
 * CxUrlValidation is a simple class used to validate URLs
 */

public class CxUrlValidation {
    static public void validate(final String spec) throws MalformedURLException {
        URL url = new URL(spec);
        if (url.getPath().length() > 0) {
            throw new MalformedURLException("must not contain path");
        }
    }
}
