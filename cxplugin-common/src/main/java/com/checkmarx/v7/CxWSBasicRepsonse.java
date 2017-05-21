
package com.checkmarx.v7;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for CxWSBasicRepsonse complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="CxWSBasicRepsonse"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="IsSuccesfull" type="{http://www.w3.org/2001/XMLSchema}boolean"/&gt;
 *         &lt;element name="ErrorMessage" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "CxWSBasicRepsonse", propOrder = {
    "isSuccesfull",
    "errorMessage"
})
@XmlSeeAlso({
    CxWSResponseRunID.class,
    CxWSResponsePresetList.class,
    CxWSResponseConfigSetList.class,
    CxWSResponseProjectsDisplayData.class,
    CxWSResponseProjectScannedDisplayData.class,
    CxWSResponseProjectConfig.class,
    CxWSResponseGroupList.class,
    CxWSResponseScanStatus.class,
    CxWSResponseScansDisplayData.class,
    CxWSResponseScanSummary.class,
    CxWSResponseUserData.class,
    CxWSCreateReportResponse.class,
    CxWSReportStatusResponse.class,
    CxWSResponseScanResults.class,
    CxWSResponseTeamLdapGroupMappingData.class,
    CxWSResponseSessionID.class
})
public class CxWSBasicRepsonse {

    @XmlElement(name = "IsSuccesfull")
    protected boolean isSuccesfull;
    @XmlElement(name = "ErrorMessage")
    protected String errorMessage;

    /**
     * Gets the value of the isSuccesfull property.
     * 
     */
    public boolean isIsSuccesfull() {
        return isSuccesfull;
    }

    /**
     * Sets the value of the isSuccesfull property.
     * 
     */
    public void setIsSuccesfull(boolean value) {
        this.isSuccesfull = value;
    }

    /**
     * Gets the value of the errorMessage property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getErrorMessage() {
        return errorMessage;
    }

    /**
     * Sets the value of the errorMessage property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setErrorMessage(String value) {
        this.errorMessage = value;
    }

}
