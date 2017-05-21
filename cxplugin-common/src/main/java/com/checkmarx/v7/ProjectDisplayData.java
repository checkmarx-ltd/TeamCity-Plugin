
package com.checkmarx.v7;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ProjectDisplayData complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ProjectDisplayData"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="Permission" type="{http://Checkmarx.com/v7}UserPermission" minOccurs="0"/&gt;
 *         &lt;element name="projectID" type="{http://www.w3.org/2001/XMLSchema}long"/&gt;
 *         &lt;element name="ServiceProvider" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="Company" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="Owner" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="ProjectName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="Group" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="Preset" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="LastScanDate" type="{http://Checkmarx.com/v7}CxDateTime" minOccurs="0"/&gt;
 *         &lt;element name="TotalScans" type="{http://www.w3.org/2001/XMLSchema}int"/&gt;
 *         &lt;element name="IsPublic" type="{http://www.w3.org/2001/XMLSchema}boolean"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ProjectDisplayData", propOrder = {
    "permission",
    "projectID",
    "serviceProvider",
    "company",
    "owner",
    "projectName",
    "group",
    "preset",
    "lastScanDate",
    "totalScans",
    "isPublic"
})
public class ProjectDisplayData {

    @XmlElement(name = "Permission")
    protected UserPermission permission;
    protected long projectID;
    @XmlElement(name = "ServiceProvider")
    protected String serviceProvider;
    @XmlElement(name = "Company")
    protected String company;
    @XmlElement(name = "Owner")
    protected String owner;
    @XmlElement(name = "ProjectName")
    protected String projectName;
    @XmlElement(name = "Group")
    protected String group;
    @XmlElement(name = "Preset")
    protected String preset;
    @XmlElement(name = "LastScanDate")
    protected CxDateTime lastScanDate;
    @XmlElement(name = "TotalScans")
    protected int totalScans;
    @XmlElement(name = "IsPublic")
    protected boolean isPublic;

    /**
     * Gets the value of the permission property.
     * 
     * @return
     *     possible object is
     *     {@link UserPermission }
     *     
     */
    public UserPermission getPermission() {
        return permission;
    }

    /**
     * Sets the value of the permission property.
     * 
     * @param value
     *     allowed object is
     *     {@link UserPermission }
     *     
     */
    public void setPermission(UserPermission value) {
        this.permission = value;
    }

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
     * Gets the value of the serviceProvider property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getServiceProvider() {
        return serviceProvider;
    }

    /**
     * Sets the value of the serviceProvider property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setServiceProvider(String value) {
        this.serviceProvider = value;
    }

    /**
     * Gets the value of the company property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCompany() {
        return company;
    }

    /**
     * Sets the value of the company property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCompany(String value) {
        this.company = value;
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
     * Gets the value of the group property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getGroup() {
        return group;
    }

    /**
     * Sets the value of the group property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setGroup(String value) {
        this.group = value;
    }

    /**
     * Gets the value of the preset property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPreset() {
        return preset;
    }

    /**
     * Sets the value of the preset property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPreset(String value) {
        this.preset = value;
    }

    /**
     * Gets the value of the lastScanDate property.
     * 
     * @return
     *     possible object is
     *     {@link CxDateTime }
     *     
     */
    public CxDateTime getLastScanDate() {
        return lastScanDate;
    }

    /**
     * Sets the value of the lastScanDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link CxDateTime }
     *     
     */
    public void setLastScanDate(CxDateTime value) {
        this.lastScanDate = value;
    }

    /**
     * Gets the value of the totalScans property.
     * 
     */
    public int getTotalScans() {
        return totalScans;
    }

    /**
     * Sets the value of the totalScans property.
     * 
     */
    public void setTotalScans(int value) {
        this.totalScans = value;
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

}
