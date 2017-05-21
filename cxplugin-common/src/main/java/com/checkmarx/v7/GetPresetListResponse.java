
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
 *         &lt;element name="GetPresetListResult" type="{http://Checkmarx.com/v7}CxWSResponsePresetList" minOccurs="0"/&gt;
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
    "getPresetListResult"
})
@XmlRootElement(name = "GetPresetListResponse")
public class GetPresetListResponse {

    @XmlElement(name = "GetPresetListResult")
    protected CxWSResponsePresetList getPresetListResult;

    /**
     * Gets the value of the getPresetListResult property.
     * 
     * @return
     *     possible object is
     *     {@link CxWSResponsePresetList }
     *     
     */
    public CxWSResponsePresetList getGetPresetListResult() {
        return getPresetListResult;
    }

    /**
     * Sets the value of the getPresetListResult property.
     * 
     * @param value
     *     allowed object is
     *     {@link CxWSResponsePresetList }
     *     
     */
    public void setGetPresetListResult(CxWSResponsePresetList value) {
        this.getPresetListResult = value;
    }

}
