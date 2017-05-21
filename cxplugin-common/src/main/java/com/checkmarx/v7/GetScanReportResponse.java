
package com.checkmarx.v7;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="GetScanReportResult" type="{http://Checkmarx.com/v7}CxWSResponseScanResults" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "getScanReportResult"
})
@XmlRootElement(name = "GetScanReportResponse")
public class GetScanReportResponse {

    @XmlElement(name = "GetScanReportResult")
    protected CxWSResponseScanResults getScanReportResult;

    /**
     * Gets the value of the getScanReportResult property.
     * 
     * @return
     *     possible object is
     *     {@link CxWSResponseScanResults }
     *     
     */
    public CxWSResponseScanResults getGetScanReportResult() {
        return getScanReportResult;
    }

    /**
     * Sets the value of the getScanReportResult property.
     * 
     * @param value
     *     allowed object is
     *     {@link CxWSResponseScanResults }
     *     
     */
    public void setGetScanReportResult(CxWSResponseScanResults value) {
        this.getScanReportResult = value;
    }

}
