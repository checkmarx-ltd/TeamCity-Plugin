
package com.checkmarx.v7;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for CxWSEnableCRUDAction complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="CxWSEnableCRUDAction"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="Type" type="{http://Checkmarx.com/v7}CxWSCrudEnum"/&gt;
 *         &lt;element name="Enable" type="{http://www.w3.org/2001/XMLSchema}boolean"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "CxWSEnableCRUDAction", propOrder = {
    "type",
    "enable"
})
public class CxWSEnableCRUDAction {

    @XmlElement(name = "Type", required = true)
    @XmlSchemaType(name = "string")
    protected CxWSCrudEnum type;
    @XmlElement(name = "Enable")
    protected boolean enable;

    /**
     * Gets the value of the type property.
     * 
     * @return
     *     possible object is
     *     {@link CxWSCrudEnum }
     *     
     */
    public CxWSCrudEnum getType() {
        return type;
    }

    /**
     * Sets the value of the type property.
     * 
     * @param value
     *     allowed object is
     *     {@link CxWSCrudEnum }
     *     
     */
    public void setType(CxWSCrudEnum value) {
        this.type = value;
    }

    /**
     * Gets the value of the enable property.
     * 
     */
    public boolean isEnable() {
        return enable;
    }

    /**
     * Sets the value of the enable property.
     * 
     */
    public void setEnable(boolean value) {
        this.enable = value;
    }

}
