
package com.checkmarx.v7;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for CxWSRoleWithUserPrivileges complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="CxWSRoleWithUserPrivileges"&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{http://Checkmarx.com/v7}Role"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="ItemsCRUD" type="{http://Checkmarx.com/v7}ArrayOfCxWSItemAndCRUD" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/extension&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "CxWSRoleWithUserPrivileges", propOrder = {
    "itemsCRUD"
})
public class CxWSRoleWithUserPrivileges
    extends Role
{

    @XmlElement(name = "ItemsCRUD")
    protected ArrayOfCxWSItemAndCRUD itemsCRUD;

    /**
     * Gets the value of the itemsCRUD property.
     * 
     * @return
     *     possible object is
     *     {@link ArrayOfCxWSItemAndCRUD }
     *     
     */
    public ArrayOfCxWSItemAndCRUD getItemsCRUD() {
        return itemsCRUD;
    }

    /**
     * Sets the value of the itemsCRUD property.
     * 
     * @param value
     *     allowed object is
     *     {@link ArrayOfCxWSItemAndCRUD }
     *     
     */
    public void setItemsCRUD(ArrayOfCxWSItemAndCRUD value) {
        this.itemsCRUD = value;
    }

}
