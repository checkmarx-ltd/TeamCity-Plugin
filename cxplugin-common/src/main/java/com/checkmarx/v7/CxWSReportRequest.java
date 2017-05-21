
package com.checkmarx.v7;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for CxWSReportRequest complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="CxWSReportRequest"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="Type" type="{http://Checkmarx.com/v7}CxWSReportType"/&gt;
 *         &lt;element name="ScanID" type="{http://www.w3.org/2001/XMLSchema}long"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "CxWSReportRequest", propOrder = {
    "type",
    "scanID"
})
public class CxWSReportRequest {

    @XmlElement(name = "Type", required = true)
    @XmlSchemaType(name = "string")
    protected CxWSReportType type;
    @XmlElement(name = "ScanID")
    protected long scanID;

    /**
     * Gets the value of the type property.
     * 
     * @return
     *     possible object is
     *     {@link CxWSReportType }
     *     
     */
    public CxWSReportType getType() {
        return type;
    }

    /**
     * Sets the value of the type property.
     * 
     * @param value
     *     allowed object is
     *     {@link CxWSReportType }
     *     
     */
    public void setType(CxWSReportType value) {
        this.type = value;
    }

    /**
     * Gets the value of the scanID property.
     * 
     */
    public long getScanID() {
        return scanID;
    }

    /**
     * Sets the value of the scanID property.
     * 
     */
    public void setScanID(long value) {
        this.scanID = value;
    }

}
