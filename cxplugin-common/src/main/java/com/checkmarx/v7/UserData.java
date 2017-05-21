
package com.checkmarx.v7;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for UserData complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="UserData"&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{http://Checkmarx.com/v7}WebClientUser"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="IsActive" type="{http://www.w3.org/2001/XMLSchema}boolean"/&gt;
 *         &lt;element name="RoleData" type="{http://Checkmarx.com/v7}Role" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/extension&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "UserData", propOrder = {
    "isActive",
    "roleData"
})
public class UserData
    extends WebClientUser
{

    @XmlElement(name = "IsActive")
    protected boolean isActive;
    @XmlElement(name = "RoleData")
    protected Role roleData;

    /**
     * Gets the value of the isActive property.
     * 
     */
    public boolean isIsActive() {
        return isActive;
    }

    /**
     * Sets the value of the isActive property.
     * 
     */
    public void setIsActive(boolean value) {
        this.isActive = value;
    }

    /**
     * Gets the value of the roleData property.
     * 
     * @return
     *     possible object is
     *     {@link Role }
     *     
     */
    public Role getRoleData() {
        return roleData;
    }

    /**
     * Sets the value of the roleData property.
     * 
     * @param value
     *     allowed object is
     *     {@link Role }
     *     
     */
    public void setRoleData(Role value) {
        this.roleData = value;
    }

}
