<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<div id="checkmarx">
    <c:choose>
    <c:when test="${not empty errorMessage}">
    Error occured: ${errorMessage}
    </c:when>
    <c:otherwise>
    <div class="cxdropshadow cxlifted">
        <div class="cxScanHeader"><div class="cxLogo"></div></div>
        <div class="cxScanContent">
            <div class="cxScanContentLeft">
                <div class="cxlifted-No-Shadow">
                    <div class="cxScanContentHeader">
                        <h3>Scan Results&nbsp;(${scanResult.totalCount})</h3>
                        <div class="cxButtonContainer">
                            <a href="${scanResult.resultDeepLink}" title="Open Code Viewer" class="cxbtn_green" target="_blank">Open Code Viewer</a>
                        </div>
                        <div class="cxClear"></div>
                    </div>
                    <table class="cxSeveritiesTable" cellpadding="0" cellspacing="0">
                    <tr><th class="cxHigh cxSeverityIcon"></th><th colspan="2">High&nbsp;Risk&nbsp;Vulnerabilities&nbsp;(${scanResult.highCount})</th></tr>
                    <c:forEach var="item" items="${scanResult.highQueryResultList}"><tr><td></td><td>${item.name}</td><td>${item.count}</td></tr></c:forEach>
                    <tr><th class="cxMed cxSeverityIcon"></th><th colspan="2">Medium&nbsp;Risk&nbsp;Vulnerabilities&nbsp;(${scanResult.mediumCount})</th></tr>
                    <c:forEach var="item" items="${scanResult.mediumQueryResultList}"><tr><td></td><td>${item.name}</td><td>${item.count}</td></tr></c:forEach>
                    <tr><th class="cxLow cxSeverityIcon"></th><th colspan="2">Low&nbsp;Risk&nbsp;Vulnerabilities&nbsp;(${scanResult.lowCount})</th></tr>
                    <c:forEach var="item" items="${scanResult.lowQueryResultList}"><tr><td></td><td>${item.name}</td><td>${item.count}</td></tr></c:forEach>
                    <tr><th class="cxInfo cxSeverityIcon"></th><th colspan="2">Info&nbsp;Risk&nbsp;Vulnerabilities&nbsp;(${scanResult.infoCount})</th></tr>
                    <c:forEach var="item" items="${scanResult.infoQueryResultList}"><tr><td></td><td>${item.name}</td><td>${item.count}</td></tr></c:forEach>
                    </table>
                </div>
            </div>
            <div class="cxScanContentRight">
                <div class="cxlifted-No-Shadow">
                    <div class="cxScanContentHeader"><h3>Scan Details</h3></div>
                    <table class="cxScanDetails" cellpadding="0" cellspacing="0">
                    <colgroup><col width="30%"><col width="70%"></colgroup>
                    <tr><td>Scan Start</td><td>${scanResult.scanStart}</td></tr>
                    <tr><td>Scan Time</td><td>${scanResult.scanTime}</td></tr>
                    <tr><td>Scan Type</td><td>${scanResult.scanType}</td></tr>
                    <tr><td>Lines of Code</td><td>${scanResult.linesOfCodeScanned}</td></tr>
                    <tr><td>File Count</td><td>${scanResult.filesScanned}</td></tr>
                    </table>
                </div>
            </div>
            <div class="cxClear"></div>
        </div>
    </div>
    </c:otherwise>
    </c:choose>
</div>
