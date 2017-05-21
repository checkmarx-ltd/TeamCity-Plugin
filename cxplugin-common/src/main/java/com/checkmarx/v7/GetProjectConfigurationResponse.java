
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
 *         &lt;element name="GetProjectConfigurationResult" type="{http://Checkmarx.com/v7}CxWSResponseProjectConfig" minOccurs="0"/&gt;
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
    "getProjectConfigurationResult"
})
@XmlRootElement(name = "GetProjectConfigurationResponse")
public class GetProjectConfigurationResponse {

    @XmlElement(name = "GetProjectConfigurationResult")
    protected CxWSResponseProjectConfig getProjectConfigurationResult;

    /**
     * Gets the value of the getProjectConfigurationResult property.
     * 
     * @return
     *     possible object is
     *     {@link CxWSResponseProjectConfig }
     *     
     */
    public CxWSResponseProjectConfig getGetProjectConfigurationResult() {
        return getProjectConfigurationResult;
    }

    /**
     * Sets the value of the getProjectConfigurationResult property.
     * 
     * @param value
     *     allowed object is
     *     {@link CxWSResponseProjectConfig }
     *     
     */
    public void setGetProjectConfigurationResult(CxWSResponseProjectConfig value) {
        this.getProjectConfigurationResult = value;
    }

}
