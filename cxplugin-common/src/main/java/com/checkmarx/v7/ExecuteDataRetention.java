
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
 *         &lt;element name="SessionID" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="dataRetentionConfiguration" type="{http://Checkmarx.com/v7}CxDataRetentionConfiguration" minOccurs="0"/&gt;
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
    "sessionID",
    "dataRetentionConfiguration"
})
@XmlRootElement(name = "ExecuteDataRetention")
public class ExecuteDataRetention {

    @XmlElement(name = "SessionID")
    protected String sessionID;
    protected CxDataRetentionConfiguration dataRetentionConfiguration;

    /**
     * Gets the value of the sessionID property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSessionID() {
        return sessionID;
    }

    /**
     * Sets the value of the sessionID property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSessionID(String value) {
        this.sessionID = value;
    }

    /**
     * Gets the value of the dataRetentionConfiguration property.
     * 
     * @return
     *     possible object is
     *     {@link CxDataRetentionConfiguration }
     *     
     */
    public CxDataRetentionConfiguration getDataRetentionConfiguration() {
        return dataRetentionConfiguration;
    }

    /**
     * Sets the value of the dataRetentionConfiguration property.
     * 
     * @param value
     *     allowed object is
     *     {@link CxDataRetentionConfiguration }
     *     
     */
    public void setDataRetentionConfiguration(CxDataRetentionConfiguration value) {
        this.dataRetentionConfiguration = value;
    }

}
