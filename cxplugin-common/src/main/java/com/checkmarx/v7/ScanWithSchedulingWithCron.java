
package com.checkmarx.v7;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
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
 *         &lt;element name="sessionId" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="args" type="{http://Checkmarx.com/v7}CliScanArgs" minOccurs="0"/&gt;
 *         &lt;element name="cronString" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="utcEpochStartTime" type="{http://www.w3.org/2001/XMLSchema}long"/&gt;
 *         &lt;element name="utcEpochEndTime" type="{http://www.w3.org/2001/XMLSchema}long"/&gt;
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
    "sessionId",
    "args",
    "cronString",
    "utcEpochStartTime",
    "utcEpochEndTime"
})
@XmlRootElement(name = "ScanWithSchedulingWithCron")
public class ScanWithSchedulingWithCron {

    protected String sessionId;
    protected CliScanArgs args;
    protected String cronString;
    protected long utcEpochStartTime;
    protected long utcEpochEndTime;

    /**
     * Gets the value of the sessionId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSessionId() {
        return sessionId;
    }

    /**
     * Sets the value of the sessionId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSessionId(String value) {
        this.sessionId = value;
    }

    /**
     * Gets the value of the args property.
     * 
     * @return
     *     possible object is
     *     {@link CliScanArgs }
     *     
     */
    public CliScanArgs getArgs() {
        return args;
    }

    /**
     * Sets the value of the args property.
     * 
     * @param value
     *     allowed object is
     *     {@link CliScanArgs }
     *     
     */
    public void setArgs(CliScanArgs value) {
        this.args = value;
    }

    /**
     * Gets the value of the cronString property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCronString() {
        return cronString;
    }

    /**
     * Sets the value of the cronString property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCronString(String value) {
        this.cronString = value;
    }

    /**
     * Gets the value of the utcEpochStartTime property.
     * 
     */
    public long getUtcEpochStartTime() {
        return utcEpochStartTime;
    }

    /**
     * Sets the value of the utcEpochStartTime property.
     * 
     */
    public void setUtcEpochStartTime(long value) {
        this.utcEpochStartTime = value;
    }

    /**
     * Gets the value of the utcEpochEndTime property.
     * 
     */
    public long getUtcEpochEndTime() {
        return utcEpochEndTime;
    }

    /**
     * Sets the value of the utcEpochEndTime property.
     * 
     */
    public void setUtcEpochEndTime(long value) {
        this.utcEpochEndTime = value;
    }

}
