
package com.checkmarx.v7;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for CxWSResponseScanResults complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="CxWSResponseScanResults"&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{http://Checkmarx.com/v7}CxWSBasicRepsonse"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="ScanResults" type="{http://www.w3.org/2001/XMLSchema}base64Binary" minOccurs="0"/&gt;
 *         &lt;element name="containsAllResults" type="{http://www.w3.org/2001/XMLSchema}boolean"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/extension&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "CxWSResponseScanResults", propOrder = {
    "scanResults",
    "containsAllResults"
})
public class CxWSResponseScanResults
    extends CxWSBasicRepsonse
{

    @XmlElement(name = "ScanResults")
    protected byte[] scanResults;
    protected boolean containsAllResults;

    /**
     * Gets the value of the scanResults property.
     * 
     * @return
     *     possible object is
     *     byte[]
     */
    public byte[] getScanResults() {
        return scanResults;
    }

    /**
     * Sets the value of the scanResults property.
     * 
     * @param value
     *     allowed object is
     *     byte[]
     */
    public void setScanResults(byte[] value) {
        this.scanResults = value;
    }

    /**
     * Gets the value of the containsAllResults property.
     * 
     */
    public boolean isContainsAllResults() {
        return containsAllResults;
    }

    /**
     * Sets the value of the containsAllResults property.
     * 
     */
    public void setContainsAllResults(boolean value) {
        this.containsAllResults = value;
    }

}
