
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
 *         &lt;element name="GetScanSummaryResult" type="{http://Checkmarx.com/v7}CxWSResponseScanSummary" minOccurs="0"/&gt;
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
    "getScanSummaryResult"
})
@XmlRootElement(name = "GetScanSummaryResponse")
public class GetScanSummaryResponse {

    @XmlElement(name = "GetScanSummaryResult")
    protected CxWSResponseScanSummary getScanSummaryResult;

    /**
     * Gets the value of the getScanSummaryResult property.
     * 
     * @return
     *     possible object is
     *     {@link CxWSResponseScanSummary }
     *     
     */
    public CxWSResponseScanSummary getGetScanSummaryResult() {
        return getScanSummaryResult;
    }

    /**
     * Sets the value of the getScanSummaryResult property.
     * 
     * @param value
     *     allowed object is
     *     {@link CxWSResponseScanSummary }
     *     
     */
    public void setGetScanSummaryResult(CxWSResponseScanSummary value) {
        this.getScanSummaryResult = value;
    }

}
