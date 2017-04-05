package com.checkmarx.teamcity.common;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.ArrayList;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import static com.checkmarx.teamcity.common.CxResultSeverity.*;


/**
 * CxScanResult encapsulates the scan result and handles the xml parsing
 *
 * Activated in CxScanBuilder
 */

public class CxScanResult {
    private int highCount = 0;
    private int mediumCount = 0;
    private int lowCount = 0;
    private int infoCount = 0;
    private LinkedList<QueryNode> highQueryResultList = new LinkedList<>();
    private LinkedList<QueryNode> mediumQueryResultList = new LinkedList<>();
    private LinkedList<QueryNode> lowQueryResultList = new LinkedList<>();
    private LinkedList<QueryNode> infoQueryResultList = new LinkedList<>();

    private boolean resultIsValid = false;
    private String resultDeepLink = "";
    private String scanStart = "";
    private String scanTime = "";
    private String linesOfCodeScanned = "";
    private String filesScanned = "";
    private String scanType = "";

    public boolean isResultValid() {
        return this.resultIsValid;
    }
    public int getHighCount() {
        return this.highCount;
    }
    public int getMediumCount() {
        return this.mediumCount;
    }
    public int getLowCount() {
        return this.lowCount;
    }
    public int getInfoCount() {
        return this.infoCount;
    }
    public int getTotalCount() { return this.highCount + this.mediumCount + this.lowCount + this.infoCount; }
    public List<QueryNode> getHighQueryResultList() {
        return this.highQueryResultList;
    }
    public List<QueryNode> getMediumQueryResultList() {
        return this.mediumQueryResultList;
    }
    public List<QueryNode> getLowQueryResultList() {
        return this.lowQueryResultList;
    }
    public List<QueryNode> getInfoQueryResultList() {
        return this.infoQueryResultList;
    }

    public String getResultDeepLink() {
        return this.resultDeepLink;
    }
    public String getScanStart() {
        return this.scanStart;
    }
    public String getScanTime() {
        return this.scanTime;
    }
    public String getLinesOfCodeScanned() {
        return this.linesOfCodeScanned;
    }
    public String getFilesScanned() {
        return this.filesScanned;
    }
    public String getScanType() {
        return this.scanType;
    }

    public void readScanXMLReport(final File scanXMLReport) throws CxAbortException {
        ResultsParseHandler handler = new ResultsParseHandler();

        try {
            SAXParser saxParser = SAXParserFactory.newInstance().newSAXParser();

            this.highCount = 0;
            this.mediumCount = 0;
            this.lowCount = 0;
            this.infoCount = 0;

            saxParser.parse(scanXMLReport, handler);

            this.resultIsValid = true;
        } catch (SAXException | IOException | ParserConfigurationException e) {
            throw new CxAbortException(e.getMessage());
        }
    }

    private class ResultsParseHandler extends DefaultHandler {
        @Nullable
        private String currentQueryName;
        @Nullable
        private String currentQuerySeverity;
        private int currentQueryNumOfResults;
        private ResultNode lastResult;
        private List<ResultNode> resultList;

        @Override
        public void startElement(String uri, String localName, String qName, Attributes attributes)
                throws SAXException {
            super.startElement(uri, localName, qName, attributes);

            switch (qName) {
                case "Result":
                    @Nullable
                    final String falsePositive = attributes.getValue("FalsePositive");
                    if (!"True".equals(falsePositive)) {
                        this.currentQueryNumOfResults++;

                        this.lastResult = new ResultNode();
                        this.lastResult.fileName = attributes.getValue("FileName");
                        this.lastResult.line = attributes.getValue("Line");
                        this.lastResult.deepLink = attributes.getValue("DeepLink");

                        @Nullable
                        final String severity = attributes.getValue("SeverityIndex");
                        if (severity != null) {
                            if (severity.equals(HIGH.xmlParseString)) {
                                highCount++;
                            } else if (severity.equals(MEDIUM.xmlParseString)) {
                                mediumCount++;
                            } else if (severity.equals(LOW.xmlParseString)) {
                                lowCount++;
                            } else if (severity.equals(INFO.xmlParseString)) {
                                infoCount++;
                            }
                        } else {
                            throw new SAXException("\"SeverityIndex\" attribute was not found in element \"Result\" in XML report. "
                                    + "Make sure you are working with Checkmarx server version 7.1.6 HF3 or above.");
                        }
                    }
                    break;

                case "Query":
                    this.currentQueryName = attributes.getValue("name");
                    if (this.currentQueryName == null) {
                        throw new SAXException("\"name\" attribute was not found in element \"Query\" in XML report");
                    }
                    this.currentQuerySeverity = attributes.getValue("SeverityIndex");
                    if (this.currentQuerySeverity == null) {
                        throw new SAXException("\"SeverityIndex\" attribute was not found in element \"Query\" in XML report. "
                                + "Make sure you are working with Checkmarx server version 7.1.6 HF3 or above.");
                    }
                    this.currentQueryNumOfResults = 0;
                    this.resultList = new ArrayList<>();
                    break;

                case "Path":
                    if (this.lastResult != null) {
                        this.lastResult.similarityId = attributes.getValue("SimilarityId");
                    }
                    break;

                default:
                    if ("CxXMLResults".equals(qName)) {
                        resultDeepLink = attributes.getValue("DeepLink");
                        scanStart = attributes.getValue("ScanStart");
                        scanTime = attributes.getValue("ScanTime");
                        linesOfCodeScanned = attributes.getValue("LinesOfCodeScanned");
                        filesScanned = attributes.getValue("FilesScanned");
                        scanType = attributes.getValue("ScanType");
                    }
                    break;
            }
        }

        @Override
        public void endElement(String uri, String localName, String qName) throws SAXException {
            super.endElement(uri, localName, qName);

            switch (qName) {
                case "Result":
                    if (this.resultList != null && this.lastResult != null) {
                        this.resultList.add(this.lastResult);
                    }
                    break;

                case "Query":
                    QueryNode qr = new QueryNode();
                    qr.setName(this.currentQueryName);
                    qr.setSeverity(this.currentQuerySeverity);
                    qr.setCount(this.currentQueryNumOfResults);
                    qr.setResultList(this.resultList);

                    if (qr.getSeverity().equals(HIGH.xmlParseString)) {
                        highQueryResultList.add(qr);
                    } else if (qr.getSeverity().equals(MEDIUM.xmlParseString)) {
                        mediumQueryResultList.add(qr);
                    } else if (qr.getSeverity().equals(LOW.xmlParseString)) {
                        lowQueryResultList.add(qr);
                    } else if (qr.getSeverity().equals(INFO.xmlParseString)) {
                        infoQueryResultList.add(qr);
                    } else {
                        throw new SAXException("Encountered a result query with unknown severity: " + qr.getSeverity());
                    }
                    break;
            }
        }
    }

    public static class ResultNode {
        @Nullable
        public String similarityId;
        @Nullable
        public String fileName;
        @Nullable
        public String line;
        @Nullable
        public String deepLink;
    }

    public static class QueryNode {
        @Nullable
        private String name;
        @Nullable
        private String severity;
        private int count;

        private List<ResultNode> resultList = new ArrayList<>();

        @Nullable
        public String getName() {
            return this.name;
        }
        public void setName(@Nullable String name) {
            this.name = name;
        }

        @Nullable
        public String getSeverity() {
            return this.severity;
        }
        public void setSeverity(@Nullable String severity) {
            this.severity = severity;
        }

        public int getCount() {
            return this.count;
        }
        public void setCount(int count) {
            this.count = count;
        }

        @NotNull
        public String getPrettyName() {
            if (this.name != null) {
                return this.name.replace('_', ' ');
            } else {
                return "";
            }
        }

        public List<ResultNode> getResultList() { return this.resultList; }
        public void setResultList(final List<ResultNode> resultList) { this.resultList = resultList; }
    }
}
