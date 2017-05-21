
package com.checkmarx.v7;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for Preset complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="Preset"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="PresetName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="ID" type="{http://www.w3.org/2001/XMLSchema}long"/&gt;
 *         &lt;element name="owningUser" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="isUserAllowToUpdate" type="{http://www.w3.org/2001/XMLSchema}boolean"/&gt;
 *         &lt;element name="isUserAllowToDelete" type="{http://www.w3.org/2001/XMLSchema}boolean"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Preset", propOrder = {
    "presetName",
    "id",
    "owningUser",
    "isUserAllowToUpdate",
    "isUserAllowToDelete"
})
public class Preset {

    @XmlElement(name = "PresetName")
    protected String presetName;
    @XmlElement(name = "ID")
    protected long id;
    protected String owningUser;
    protected boolean isUserAllowToUpdate;
    protected boolean isUserAllowToDelete;

    /**
     * Gets the value of the presetName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPresetName() {
        return presetName;
    }

    /**
     * Sets the value of the presetName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPresetName(String value) {
        this.presetName = value;
    }

    /**
     * Gets the value of the id property.
     * 
     */
    public long getID() {
        return id;
    }

    /**
     * Sets the value of the id property.
     * 
     */
    public void setID(long value) {
        this.id = value;
    }

    /**
     * Gets the value of the owningUser property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getOwningUser() {
        return owningUser;
    }

    /**
     * Sets the value of the owningUser property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOwningUser(String value) {
        this.owningUser = value;
    }

    /**
     * Gets the value of the isUserAllowToUpdate property.
     * 
     */
    public boolean isIsUserAllowToUpdate() {
        return isUserAllowToUpdate;
    }

    /**
     * Sets the value of the isUserAllowToUpdate property.
     * 
     */
    public void setIsUserAllowToUpdate(boolean value) {
        this.isUserAllowToUpdate = value;
    }

    /**
     * Gets the value of the isUserAllowToDelete property.
     * 
     */
    public boolean isIsUserAllowToDelete() {
        return isUserAllowToDelete;
    }

    /**
     * Sets the value of the isUserAllowToDelete property.
     * 
     */
    public void setIsUserAllowToDelete(boolean value) {
        this.isUserAllowToDelete = value;
    }

}
