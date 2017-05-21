
package com.checkmarx.v7;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for UserPermission complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="UserPermission"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="IsAllowedToDelete" type="{http://www.w3.org/2001/XMLSchema}boolean"/&gt;
 *         &lt;element name="IsAllowedToDuplicate" type="{http://www.w3.org/2001/XMLSchema}boolean"/&gt;
 *         &lt;element name="IsAllowedToRun" type="{http://www.w3.org/2001/XMLSchema}boolean"/&gt;
 *         &lt;element name="IsAllowedToUpdate" type="{http://www.w3.org/2001/XMLSchema}boolean"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "UserPermission", propOrder = {
    "isAllowedToDelete",
    "isAllowedToDuplicate",
    "isAllowedToRun",
    "isAllowedToUpdate"
})
public class UserPermission {

    @XmlElement(name = "IsAllowedToDelete")
    protected boolean isAllowedToDelete;
    @XmlElement(name = "IsAllowedToDuplicate")
    protected boolean isAllowedToDuplicate;
    @XmlElement(name = "IsAllowedToRun")
    protected boolean isAllowedToRun;
    @XmlElement(name = "IsAllowedToUpdate")
    protected boolean isAllowedToUpdate;

    /**
     * Gets the value of the isAllowedToDelete property.
     * 
     */
    public boolean isIsAllowedToDelete() {
        return isAllowedToDelete;
    }

    /**
     * Sets the value of the isAllowedToDelete property.
     * 
     */
    public void setIsAllowedToDelete(boolean value) {
        this.isAllowedToDelete = value;
    }

    /**
     * Gets the value of the isAllowedToDuplicate property.
     * 
     */
    public boolean isIsAllowedToDuplicate() {
        return isAllowedToDuplicate;
    }

    /**
     * Sets the value of the isAllowedToDuplicate property.
     * 
     */
    public void setIsAllowedToDuplicate(boolean value) {
        this.isAllowedToDuplicate = value;
    }

    /**
     * Gets the value of the isAllowedToRun property.
     * 
     */
    public boolean isIsAllowedToRun() {
        return isAllowedToRun;
    }

    /**
     * Sets the value of the isAllowedToRun property.
     * 
     */
    public void setIsAllowedToRun(boolean value) {
        this.isAllowedToRun = value;
    }

    /**
     * Gets the value of the isAllowedToUpdate property.
     * 
     */
    public boolean isIsAllowedToUpdate() {
        return isAllowedToUpdate;
    }

    /**
     * Sets the value of the isAllowedToUpdate property.
     * 
     */
    public void setIsAllowedToUpdate(boolean value) {
        this.isAllowedToUpdate = value;
    }

}
