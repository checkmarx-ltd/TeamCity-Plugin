package com.checkmarx.teamcity.agent;

import java.net.MalformedURLException;

import org.slf4j.Logger;

import com.cx.restclient.CxClientDelegator;
import com.cx.restclient.configuration.CxScanConfig;
import com.cx.restclient.exception.CxClientException;
import com.cx.restclient.sast.utils.LegacyClient;

public class CommonClientFactory {

   public static LegacyClient getInstance(CxScanConfig config, Logger log)
            throws MalformedURLException, CxClientException {
        return new LegacyClient(config, log) {
        };
    }
}
