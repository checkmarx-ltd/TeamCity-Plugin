
package com.checkmarx.v7;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for CxWSResponsePresetList complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="CxWSResponsePresetList"&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{http://Checkmarx.com/v7}CxWSBasicRepsonse"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="PresetList" type="{http://Checkmarx.com/v7}ArrayOfPreset" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/extension&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "CxWSResponsePresetList", propOrder = {
    "presetList"
})
public class CxWSResponsePresetList
    extends CxWSBasicRepsonse
{

    @XmlElement(name = "PresetList")
    protected ArrayOfPreset presetList;

    /**
     * Gets the value of the presetList property.
     * 
     * @return
     *     possible object is
     *     {@link ArrayOfPreset }
     *     
     */
    public ArrayOfPreset getPresetList() {
        return presetList;
    }

    /**
     * Sets the value of the presetList property.
     * 
     * @param value
     *     allowed object is
     *     {@link ArrayOfPreset }
     *     
     */
    public void setPresetList(ArrayOfPreset value) {
        this.presetList = value;
    }

}
