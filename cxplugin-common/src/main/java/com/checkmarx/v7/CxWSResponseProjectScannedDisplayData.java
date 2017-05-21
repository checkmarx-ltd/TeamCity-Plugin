
package com.checkmarx.v7;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for CxWSResponseProjectScannedDisplayData complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="CxWSResponseProjectScannedDisplayData"&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{http://Checkmarx.com/v7}CxWSBasicRepsonse"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="ProjectScannedList" type="{http://Checkmarx.com/v7}ArrayOfProjectScannedDisplayData" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/extension&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "CxWSResponseProjectScannedDisplayData", propOrder = {
    "projectScannedList"
})
public class CxWSResponseProjectScannedDisplayData
    extends CxWSBasicRepsonse
{

    @XmlElement(name = "ProjectScannedList")
    protected ArrayOfProjectScannedDisplayData projectScannedList;

    /**
     * Gets the value of the projectScannedList property.
     * 
     * @return
     *     possible object is
     *     {@link ArrayOfProjectScannedDisplayData }
     *     
     */
    public ArrayOfProjectScannedDisplayData getProjectScannedList() {
        return projectScannedList;
    }

    /**
     * Sets the value of the projectScannedList property.
     * 
     * @param value
     *     allowed object is
     *     {@link ArrayOfProjectScannedDisplayData }
     *     
     */
    public void setProjectScannedList(ArrayOfProjectScannedDisplayData value) {
        this.projectScannedList = value;
    }

}
