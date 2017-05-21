
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
 *         &lt;element name="CreateScanReportResult" type="{http://Checkmarx.com/v7}CxWSCreateReportResponse" minOccurs="0"/&gt;
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
    "createScanReportResult"
})
@XmlRootElement(name = "CreateScanReportResponse")
public class CreateScanReportResponse {

    @XmlElement(name = "CreateScanReportResult")
    protected CxWSCreateReportResponse createScanReportResult;

    /**
     * Gets the value of the createScanReportResult property.
     * 
     * @return
     *     possible object is
     *     {@link CxWSCreateReportResponse }
     *     
     */
    public CxWSCreateReportResponse getCreateScanReportResult() {
        return createScanReportResult;
    }

    /**
     * Sets the value of the createScanReportResult property.
     * 
     * @param value
     *     allowed object is
     *     {@link CxWSCreateReportResponse }
     *     
     */
    public void setCreateScanReportResult(CxWSCreateReportResponse value) {
        this.createScanReportResult = value;
    }

}
