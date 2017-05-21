
package com.checkmarx.v7;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for CxWSResponseConfigSetList complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="CxWSResponseConfigSetList"&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{http://Checkmarx.com/v7}CxWSBasicRepsonse"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="ConfigSetList" type="{http://Checkmarx.com/v7}ArrayOfConfigurationSet" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/extension&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "CxWSResponseConfigSetList", propOrder = {
    "configSetList"
})
public class CxWSResponseConfigSetList
    extends CxWSBasicRepsonse
{

    @XmlElement(name = "ConfigSetList")
    protected ArrayOfConfigurationSet configSetList;

    /**
     * Gets the value of the configSetList property.
     * 
     * @return
     *     possible object is
     *     {@link ArrayOfConfigurationSet }
     *     
     */
    public ArrayOfConfigurationSet getConfigSetList() {
        return configSetList;
    }

    /**
     * Sets the value of the configSetList property.
     * 
     * @param value
     *     allowed object is
     *     {@link ArrayOfConfigurationSet }
     *     
     */
    public void setConfigSetList(ArrayOfConfigurationSet value) {
        this.configSetList = value;
    }

}
