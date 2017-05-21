
package com.checkmarx.v7;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ProjectConfiguration complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ProjectConfiguration"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="ProjectSettings" type="{http://Checkmarx.com/v7}ProjectSettings" minOccurs="0"/&gt;
 *         &lt;element name="SourceCodeSettings" type="{http://Checkmarx.com/v7}SourceCodeSettings" minOccurs="0"/&gt;
 *         &lt;element name="ScheduleSettings" type="{http://Checkmarx.com/v7}ScheduleSettings" minOccurs="0"/&gt;
 *         &lt;element name="ScanActionSettings" type="{http://Checkmarx.com/v7}ScanActionSettings" minOccurs="0"/&gt;
 *         &lt;element name="ProjectIssueTrackingSettings" type="{http://Checkmarx.com/v7}CxWSProjectIssueTrackingSettings" minOccurs="0"/&gt;
 *         &lt;element name="CustomFields" type="{http://Checkmarx.com/v7}ArrayOfCxWSProjectCustomField" minOccurs="0"/&gt;
 *         &lt;element name="DataRetentionSettings" type="{http://Checkmarx.com/v7}DataRetentionSettings" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ProjectConfiguration", propOrder = {
    "projectSettings",
    "sourceCodeSettings",
    "scheduleSettings",
    "scanActionSettings",
    "projectIssueTrackingSettings",
    "customFields",
    "dataRetentionSettings"
})
public class ProjectConfiguration {

    @XmlElement(name = "ProjectSettings")
    protected ProjectSettings projectSettings;
    @XmlElement(name = "SourceCodeSettings")
    protected SourceCodeSettings sourceCodeSettings;
    @XmlElement(name = "ScheduleSettings")
    protected ScheduleSettings scheduleSettings;
    @XmlElement(name = "ScanActionSettings")
    protected ScanActionSettings scanActionSettings;
    @XmlElement(name = "ProjectIssueTrackingSettings")
    protected CxWSProjectIssueTrackingSettings projectIssueTrackingSettings;
    @XmlElement(name = "CustomFields")
    protected ArrayOfCxWSProjectCustomField customFields;
    @XmlElement(name = "DataRetentionSettings")
    protected DataRetentionSettings dataRetentionSettings;

    /**
     * Gets the value of the projectSettings property.
     * 
     * @return
     *     possible object is
     *     {@link ProjectSettings }
     *     
     */
    public ProjectSettings getProjectSettings() {
        return projectSettings;
    }

    /**
     * Sets the value of the projectSettings property.
     * 
     * @param value
     *     allowed object is
     *     {@link ProjectSettings }
     *     
     */
    public void setProjectSettings(ProjectSettings value) {
        this.projectSettings = value;
    }

    /**
     * Gets the value of the sourceCodeSettings property.
     * 
     * @return
     *     possible object is
     *     {@link SourceCodeSettings }
     *     
     */
    public SourceCodeSettings getSourceCodeSettings() {
        return sourceCodeSettings;
    }

    /**
     * Sets the value of the sourceCodeSettings property.
     * 
     * @param value
     *     allowed object is
     *     {@link SourceCodeSettings }
     *     
     */
    public void setSourceCodeSettings(SourceCodeSettings value) {
        this.sourceCodeSettings = value;
    }

    /**
     * Gets the value of the scheduleSettings property.
     * 
     * @return
     *     possible object is
     *     {@link ScheduleSettings }
     *     
     */
    public ScheduleSettings getScheduleSettings() {
        return scheduleSettings;
    }

    /**
     * Sets the value of the scheduleSettings property.
     * 
     * @param value
     *     allowed object is
     *     {@link ScheduleSettings }
     *     
     */
    public void setScheduleSettings(ScheduleSettings value) {
        this.scheduleSettings = value;
    }

    /**
     * Gets the value of the scanActionSettings property.
     * 
     * @return
     *     possible object is
     *     {@link ScanActionSettings }
     *     
     */
    public ScanActionSettings getScanActionSettings() {
        return scanActionSettings;
    }

    /**
     * Sets the value of the scanActionSettings property.
     * 
     * @param value
     *     allowed object is
     *     {@link ScanActionSettings }
     *     
     */
    public void setScanActionSettings(ScanActionSettings value) {
        this.scanActionSettings = value;
    }

    /**
     * Gets the value of the projectIssueTrackingSettings property.
     * 
     * @return
     *     possible object is
     *     {@link CxWSProjectIssueTrackingSettings }
     *     
     */
    public CxWSProjectIssueTrackingSettings getProjectIssueTrackingSettings() {
        return projectIssueTrackingSettings;
    }

    /**
     * Sets the value of the projectIssueTrackingSettings property.
     * 
     * @param value
     *     allowed object is
     *     {@link CxWSProjectIssueTrackingSettings }
     *     
     */
    public void setProjectIssueTrackingSettings(CxWSProjectIssueTrackingSettings value) {
        this.projectIssueTrackingSettings = value;
    }

    /**
     * Gets the value of the customFields property.
     * 
     * @return
     *     possible object is
     *     {@link ArrayOfCxWSProjectCustomField }
     *     
     */
    public ArrayOfCxWSProjectCustomField getCustomFields() {
        return customFields;
    }

    /**
     * Sets the value of the customFields property.
     * 
     * @param value
     *     allowed object is
     *     {@link ArrayOfCxWSProjectCustomField }
     *     
     */
    public void setCustomFields(ArrayOfCxWSProjectCustomField value) {
        this.customFields = value;
    }

    /**
     * Gets the value of the dataRetentionSettings property.
     * 
     * @return
     *     possible object is
     *     {@link DataRetentionSettings }
     *     
     */
    public DataRetentionSettings getDataRetentionSettings() {
        return dataRetentionSettings;
    }

    /**
     * Sets the value of the dataRetentionSettings property.
     * 
     * @param value
     *     allowed object is
     *     {@link DataRetentionSettings }
     *     
     */
    public void setDataRetentionSettings(DataRetentionSettings value) {
        this.dataRetentionSettings = value;
    }

}
