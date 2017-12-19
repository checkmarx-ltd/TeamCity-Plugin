package com.checkmarx.teamcity.common.client.rest;


import com.checkmarx.teamcity.common.client.dto.LoginRequest;
import com.checkmarx.teamcity.common.client.dto.OSAFile;
import com.checkmarx.teamcity.common.client.exception.CxClientException;
import com.checkmarx.teamcity.common.client.rest.dto.*;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import org.apache.commons.io.IOUtils;
import org.apache.http.*;
import org.apache.http.client.CookieStore;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.HttpClientUtils;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.protocol.HttpContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.List;

/**
 * Created by: Dorg & Galn.
 * Date: 16/06/2016.
 */
public class CxRestClient {

    private static Logger log = LoggerFactory.getLogger(CxRestClient.class);

    private final String username;
    private final String password;
    private String ROOT_PATH = "{hostName}/CxRestAPI/";

    public static final String OSA_SCAN_PROJECT_PATH = "osa/scans";
    public static final String OSA_SCAN_STATUS_PATH = "osa/scans/{scanId}";
    public static final String OSA_SCAN_SUMMARY_PATH = "osa/reports";
    public static final String OSA_SCAN_LIBRARIES_PATH = "/osa/libraries";
    public static final String OSA_SCAN_VULNERABILITIES_PATH = "/osa/vulnerabilities";
    private static final String AUTHENTICATION_PATH = "auth/login";
    public static final String CSRF_TOKEN_HEADER = "CXCSRFToken";
    public static final String SCAN_ID_QUERY_PARAM = "?scanId=";
    public static final String ITEM_PER_PAGE_QUERY_PARAM = "&itemsPerPage=";
    public static final long MAX_ITEMS = 1000000;
    public static final String ORIGIN = "TeamCity";


    private HttpClient apacheClient;
    private CookieStore cookieStore;
    private String cookies;
    private String csrfToken;
    ObjectMapper mapper = new ObjectMapper();


    private final HttpRequestInterceptor requestFilter = new HttpRequestInterceptor() {
        public void process(HttpRequest httpRequest, HttpContext httpContext) throws HttpException, IOException {
            if (csrfToken != null) {
                httpRequest.addHeader(CSRF_TOKEN_HEADER, csrfToken);
            }

            if (cookies != null) {
                httpRequest.addHeader("cookie", cookies);
            }
        }
    };

    private final HttpResponseInterceptor responseFilter = new HttpResponseInterceptor() {

        public void process(HttpResponse httpResponse, HttpContext httpContext) throws HttpException, IOException {

            for (org.apache.http.cookie.Cookie c : cookieStore.getCookies()) {
                if (CSRF_TOKEN_HEADER.equals(c.getName())) {
                    csrfToken = c.getValue();
                }
            }
            Header[] setCookies = httpResponse.getHeaders("Set-Cookie");

            StringBuilder sb = new StringBuilder();
            for (Header h : setCookies) {
                sb.append(h.getValue()).append(";");
            }

            cookies = (cookies == null ? "" : cookies) + sb.toString();

        }
    };

    public CxRestClient(String hostname, String username, String password) {
        this.username = username;
        this.password = password;
        this.ROOT_PATH = ROOT_PATH.replace("{hostName}", hostname);
        //create httpclient
        cookieStore = new BasicCookieStore();

        apacheClient = HttpClientBuilder.create().addInterceptorFirst(requestFilter).addInterceptorLast(responseFilter).setDefaultCookieStore(cookieStore).build();
    }

    public void setLogger(Logger log) {
        CxRestClient.log = log;
    }


    public void login() throws CxClientException, IOException {
        cookies = null;
        csrfToken = null;
        HttpResponse loginResponse = null;
        //create login request
        HttpPost loginPost = new HttpPost(ROOT_PATH + AUTHENTICATION_PATH);
        StringEntity requestEntity = new StringEntity(mapper.writeValueAsString(new LoginRequest(username, password)), ContentType.APPLICATION_JSON);
        loginPost.setEntity(requestEntity);
        try {
            //send login request
            loginResponse = apacheClient.execute(loginPost);

            //validate login response
            validateResponse(loginResponse, 200, "Fail to authenticate");
        } finally {
            loginPost.releaseConnection();
            HttpClientUtils.closeQuietly(loginResponse);

        }
    }

    public CreateOSAScanResponse createOSAScan(long projectId, List<OSAFile> osaFileList) throws IOException, CxClientException {
        //create scan request
        HttpPost post = new HttpPost(ROOT_PATH + OSA_SCAN_PROJECT_PATH);
        CreateOSAScanRequest req = new CreateOSAScanRequest(projectId, ORIGIN, osaFileList);
        StringEntity entity = new StringEntity(convertToJson(req));
        entity.setContentType("application/json");
        post.setEntity(entity);
        HttpResponse response = null;

        try {
            //send scan request
            response = apacheClient.execute(post);
            //verify scan request
            validateResponse(response, 202, "Failed to create OSA scan");
            //extract response as object and return the link
            return convertToObject(response, CreateOSAScanResponse.class);
        } finally {
            post.releaseConnection();
            HttpClientUtils.closeQuietly(response);
        }
    }


    public OSAScanStatus getOSAScanStatus(String scanId) throws CxClientException, IOException {

        String resolvedPath = ROOT_PATH + OSA_SCAN_STATUS_PATH.replace("{scanId}", String.valueOf(scanId));
        HttpGet getRequest = new HttpGet(resolvedPath);
        HttpResponse response = null;

        try {
            response = apacheClient.execute(getRequest);
            validateResponse(response, 200, "Failed to get OSA scan status");

            return convertToObject(response, OSAScanStatus.class);
        } finally {
            getRequest.releaseConnection();
            HttpClientUtils.closeQuietly(response);
        }
    }

    public OSASummaryResults getOSAScanSummaryResults(String scanId) throws CxClientException, IOException {
        String relativePath = OSA_SCAN_SUMMARY_PATH + SCAN_ID_QUERY_PARAM + scanId;
        HttpGet getRequest = createHttpRequest(relativePath, "application/json");
        HttpResponse response = null;

        try {
            response = apacheClient.execute(getRequest);
            validateResponse(response, 200, "Failed to get OSA scan summary results");
            return convertToObject(response, OSASummaryResults.class);
        } finally {
            getRequest.releaseConnection();
            HttpClientUtils.closeQuietly(response);
        }
    }

    public String getOSAScanHtmlResults(String scanId) throws CxClientException, IOException {
        String relativePath = OSA_SCAN_SUMMARY_PATH + SCAN_ID_QUERY_PARAM + scanId;
        HttpGet getRequest = createHttpRequest(relativePath, "text/html");
        HttpResponse response = null;
        try {
            response = apacheClient.execute(getRequest);
            validateResponse(response, 200, "Failed to get OSA scan html results");

            return IOUtils.toString(response.getEntity().getContent(), Charset.defaultCharset());

        } finally {
            getRequest.releaseConnection();
            HttpClientUtils.closeQuietly(response);
        }
    }

    public byte[] getOSAScanPDFResults(String scanId) throws CxClientException, IOException {
        String relativePath = OSA_SCAN_SUMMARY_PATH + SCAN_ID_QUERY_PARAM + scanId;
        HttpGet getRequest = createHttpRequest(relativePath, "application/pdf");
        HttpResponse response = null;

        try {
            response = apacheClient.execute(getRequest);
            validateResponse(response, 200, "Failed to get OSA scan pdf results");
            return IOUtils.toByteArray(response.getEntity().getContent());
        } finally {
            getRequest.releaseConnection();
            HttpClientUtils.closeQuietly(response);
        }
    }

    public List<Library> getOSALibraries(String scanId) throws CxClientException, IOException {

        String relativePath = OSA_SCAN_LIBRARIES_PATH + SCAN_ID_QUERY_PARAM + scanId + ITEM_PER_PAGE_QUERY_PARAM + MAX_ITEMS;
        HttpGet getRequest = createHttpRequest(relativePath, "application/json");
        HttpResponse response = null;
        try {
            response = apacheClient.execute(getRequest);
            validateResponse(response, 200, "Failed to get OSA libraries");
            return convertToObject(response, TypeFactory.defaultInstance().constructCollectionType(List.class, Library.class));
        } finally {
            getRequest.releaseConnection();
            HttpClientUtils.closeQuietly(response);
        }
    }

    public List<CVE> getOSAVulnerabilities(String scanId) throws CxClientException, IOException {
        String relativePath = OSA_SCAN_VULNERABILITIES_PATH + SCAN_ID_QUERY_PARAM + scanId + ITEM_PER_PAGE_QUERY_PARAM + MAX_ITEMS;
        HttpGet getRequest = createHttpRequest(relativePath, "application/json");
        HttpResponse response = null;
        try {
            response = apacheClient.execute(getRequest);
            validateResponse(response, 200, "Failed to get OSA vulnerabilities");
            return convertToObject(response, TypeFactory.defaultInstance().constructCollectionType(List.class, CVE.class));
        } finally {
            getRequest.releaseConnection();
            HttpClientUtils.closeQuietly(response);
        }
    }

    private HttpGet createHttpRequest(String relativePath, String mediaType) {
        String resolvedPath = ROOT_PATH + relativePath;
        HttpGet getRequest = new HttpGet(resolvedPath);
        getRequest.setHeader("Accept", mediaType);
        return getRequest;
    }

    public void close() {
        HttpClientUtils.closeQuietly(apacheClient);
    }

    private void validateResponse(HttpResponse response, int status, String message) throws CxClientException {
        if (response.getStatusLine().getStatusCode() != status) {
            String responseBody = extractResponseBody(response);
            responseBody = responseBody.replace("{", "").replace("}", "").replace(System.lineSeparator(), " ").replace("  ", "");
            throw new CxClientException(message + ": " + "status code: " + response.getStatusLine().getStatusCode() + ". error:" + responseBody);
        }
    }

    private <T> T convertToObject(HttpResponse response, Class<T> valueType) throws CxClientException {
        String json = "";
        try {
            json = IOUtils.toString(response.getEntity().getContent(), Charset.defaultCharset());
            return mapper.readValue(json, valueType);
        } catch (IOException e) {
            log.debug("Failed to parse json response: [" + json + "]", e);
            throw new CxClientException("Failed to parse json response: " + e.getMessage());
        }
    }


    private <T> T convertToObject(HttpResponse response, JavaType javaType) throws CxClientException {
        String json = "";
        try {
            json = IOUtils.toString(response.getEntity().getContent(), Charset.defaultCharset());
            return mapper.readValue(json, javaType);
        } catch (IOException e) {
            log.debug("Failed to parse json response: [" + json + "]", e);
            throw new CxClientException("Failed to parse json response: " + e.getMessage());
        }
    }

    private String convertToJson(Object o) throws CxClientException {
        String json = "";
        try {
            return mapper.writeValueAsString(o);
        } catch (IOException e) {
            log.debug("Failed convert object to json: [" + json + "]", e);
            throw new CxClientException("Failed convert object to json: " + e.getMessage());
        }
    }

    private String extractResponseBody(HttpResponse response) {
        try {
            return IOUtils.toString(response.getEntity().getContent(), Charset.defaultCharset());
        } catch (IOException e) {
            return "";
        }
    }
}
