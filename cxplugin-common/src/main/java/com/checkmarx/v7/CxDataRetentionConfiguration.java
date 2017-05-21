
package com.checkmarx.v7;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for CxDataRetentionConfiguration complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="CxDataRetentionConfiguration"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="DataRetentionType" type="{http://Checkmarx.com/v7}CxDataRetentionType"/&gt;
 *         &lt;element name="NumOfScansToPreserve" type="{http://www.w3.org/2001/XMLSchema}int"/&gt;
 *         &lt;element name="StartDate" type="{http://www.w3.org/2001/XMLSchema}long"/&gt;
 *         &lt;element name="EndDate" type="{http://www.w3.org/2001/XMLSchema}long"/&gt;
 *         &lt;element name="DurationLimitInHours" type="{http://www.w3.org/2001/XMLSchema}long"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "CxDataRetentionConfiguration", propOrder = {
    "dataRetentionType",
    "numOfScansToPreserve",
    "startDate",
    "endDate",
    "durationLimitInHours"
})
public class CxDataRetentionConfiguration {

    @XmlElement(name = "DataRetentionType", required = true)
    @XmlSchemaType(name = "string")
    protected CxDataRetentionType dataRetentionType;
    @XmlElement(name = "NumOfScansToPreserve")
    protected int numOfScansToPreserve;
    @XmlElement(name = "StartDate")
    protected long startDate;
    @XmlElement(name = "EndDate")
    protected long endDate;
    @XmlElement(name = "DurationLimitInHours", required = true, type = Long.class, nillable = true)
    protected Long durationLimitInHours;

    /**
     * Gets the value of the dataRetentionType property.
     * 
     * @return
     *     possible object is
     *     {@link CxDataRetentionType }
     *     
     */
    public CxDataRetentionType getDataRetentionType() {
        return dataRetentionType;
    }

    /**
     * Sets the value of the dataRetentionType property.
     * 
     * @param value
     *     allowed object is
     *     {@link CxDataRetentionType }
     *     
     */
    public void setDataRetentionType(CxDataRetentionType value) {
        this.dataRetentionType = value;
    }

    /**
     * Gets the value of the numOfScansToPreserve property.
     * 
     */
    public int getNumOfScansToPreserve() {
        return numOfScansToPreserve;
    }

    /**
     * Sets the value of the numOfScansToPreserve property.
     * 
     */
    public void setNumOfScansToPreserve(int value) {
        this.numOfScansToPreserve = value;
    }

    /**
     * Gets the value of the startDate property.
     * 
     */
    public long getStartDate() {
        return startDate;
    }

    /**
     * Sets the value of the startDate property.
     * 
     */
    public void setStartDate(long value) {
        this.startDate = value;
    }

    /**
     * Gets the value of the endDate property.
     * 
     */
    public long getEndDate() {
        return endDate;
    }

    /**
     * Sets the value of the endDate property.
     * 
     */
    public void setEndDate(long value) {
        this.endDate = value;
    }

    /**
     * Gets the value of the durationLimitInHours property.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getDurationLimitInHours() {
        return durationLimitInHours;
    }

    /**
     * Sets the value of the durationLimitInHours property.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setDurationLimitInHours(Long value) {
        this.durationLimitInHours = value;
    }

}
