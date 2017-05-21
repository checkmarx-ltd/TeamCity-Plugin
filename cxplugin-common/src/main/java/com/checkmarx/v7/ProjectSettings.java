
package com.checkmarx.v7;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ProjectSettings complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ProjectSettings"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="projectID" type="{http://www.w3.org/2001/XMLSchema}long"/&gt;
 *         &lt;element name="ProjectName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="PresetID" type="{http://www.w3.org/2001/XMLSchema}long"/&gt;
 *         &lt;element name="AssociatedGroupID" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="ScanConfigurationID" type="{http://www.w3.org/2001/XMLSchema}long"/&gt;
 *         &lt;element name="Description" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="Owner" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="IsPublic" type="{http://www.w3.org/2001/XMLSchema}boolean"/&gt;
 *         &lt;element name="OpenSourceSettings" type="{http://Checkmarx.com/v7}ProjectSharedLocation" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ProjectSettings", propOrder = {
    "projectID",
    "projectName",
    "presetID",
    "associatedGroupID",
    "scanConfigurationID",
    "description",
    "owner",
    "isPublic",
    "openSourceSettings"
})
public class ProjectSettings {

    protected long projectID;
    @XmlElement(name = "ProjectName")
    protected String projectName;
    @XmlElement(name = "PresetID")
    protected long presetID;
    @XmlElement(name = "AssociatedGroupID")
    protected String associatedGroupID;
    @XmlElement(name = "ScanConfigurationID")
    protected long scanConfigurationID;
    @XmlElement(name = "Description")
    protected String description;
    @XmlElement(name = "Owner")
    protected String owner;
    @XmlElement(name = "IsPublic")
    protected boolean isPublic;
    @XmlElement(name = "OpenSourceSettings")
    protected ProjectSharedLocation openSourceSettings;

    /**
     * Gets the value of the projectID property.
     * 
     */
    public long getProjectID() {
        return projectID;
    }

    /**
     * Sets the value of the projectID property.
     * 
     */
    public void setProjectID(long value) {
        this.projectID = value;
    }

    /**
     * Gets the value of the projectName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getProjectName() {
        return projectName;
    }

    /**
     * Sets the value of the projectName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setProjectName(String value) {
        this.projectName = value;
    }

    /**
     * Gets the value of the presetID property.
     * 
     */
    public long getPresetID() {
        return presetID;
    }

    /**
     * Sets the value of the presetID property.
     * 
     */
    public void setPresetID(long value) {
        this.presetID = value;
    }

    /**
     * Gets the value of the associatedGroupID property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAssociatedGroupID() {
        return associatedGroupID;
    }

    /**
     * Sets the value of the associatedGroupID property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAssociatedGroupID(String value) {
        this.associatedGroupID = value;
    }

    /**
     * Gets the value of the scanConfigurationID property.
     * 
     */
    public long getScanConfigurationID() {
        return scanConfigurationID;
    }

    /**
     * Sets the value of the scanConfigurationID property.
     * 
     */
    public void setScanConfigurationID(long value) {
        this.scanConfigurationID = value;
    }

    /**
     * Gets the value of the description property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the value of the description property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDescription(String value) {
        this.description = value;
    }

    /**
     * Gets the value of the owner property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getOwner() {
        return owner;
    }

    /**
     * Sets the value of the owner property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOwner(String value) {
        this.owner = value;
    }

    /**
     * Gets the value of the isPublic property.
     * 
     */
    public boolean isIsPublic() {
        return isPublic;
    }

    /**
     * Sets the value of the isPublic property.
     * 
     */
    public void setIsPublic(boolean value) {
        this.isPublic = value;
    }

    /**
     * Gets the value of the openSourceSettings property.
     * 
     * @return
     *     possible object is
     *     {@link ProjectSharedLocation }
     *     
     */
    public ProjectSharedLocation getOpenSourceSettings() {
        return openSourceSettings;
    }

    /**
     * Sets the value of the openSourceSettings property.
     * 
     * @param value
     *     allowed object is
     *     {@link ProjectSharedLocation }
     *     
     */
    public void setOpenSourceSettings(ProjectSharedLocation value) {
        this.openSourceSettings = value;
    }

}
