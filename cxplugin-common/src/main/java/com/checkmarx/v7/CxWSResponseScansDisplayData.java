
package com.checkmarx.v7;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for CxWSResponseScansDisplayData complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="CxWSResponseScansDisplayData"&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{http://Checkmarx.com/v7}CxWSBasicRepsonse"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="ScanList" type="{http://Checkmarx.com/v7}ArrayOfScanDisplayData" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/extension&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "CxWSResponseScansDisplayData", propOrder = {
    "scanList"
})
public class CxWSResponseScansDisplayData
    extends CxWSBasicRepsonse
{

    @XmlElement(name = "ScanList")
    protected ArrayOfScanDisplayData scanList;

    /**
     * Gets the value of the scanList property.
     * 
     * @return
     *     possible object is
     *     {@link ArrayOfScanDisplayData }
     *     
     */
    public ArrayOfScanDisplayData getScanList() {
        return scanList;
    }

    /**
     * Sets the value of the scanList property.
     * 
     * @param value
     *     allowed object is
     *     {@link ArrayOfScanDisplayData }
     *     
     */
    public void setScanList(ArrayOfScanDisplayData value) {
        this.scanList = value;
    }

}
