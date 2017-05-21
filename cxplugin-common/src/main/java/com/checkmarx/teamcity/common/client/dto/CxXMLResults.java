
package com.checkmarx.teamcity.common.client.dto;

import javax.xml.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="Query" maxOccurs="unbounded" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="Result" maxOccurs="unbounded" minOccurs="0">
 *                     &lt;complexType>
 *                       &lt;complexContent>
 *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                           &lt;sequence>
 *                             &lt;element name="Path">
 *                               &lt;complexType>
 *                                 &lt;complexContent>
 *                                   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                                     &lt;sequence>
 *                                       &lt;element name="PathNode" maxOccurs="unbounded" minOccurs="0">
 *                                         &lt;complexType>
 *                                           &lt;complexContent>
 *                                             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                                               &lt;sequence>
 *                                                 &lt;element name="FileName" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                                                 &lt;element name="Line" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                                                 &lt;element name="Column" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                                                 &lt;element name="NodeId" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                                                 &lt;element name="Name" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                                                 &lt;element name="Type" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                                                 &lt;element name="Length" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                                                 &lt;element name="Snippet">
 *                                                   &lt;complexType>
 *                                                     &lt;complexContent>
 *                                                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                                                         &lt;sequence>
 *                                                           &lt;element name="Line">
 *                                                             &lt;complexType>
 *                                                               &lt;complexContent>
 *                                                                 &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                                                                   &lt;sequence>
 *                                                                     &lt;element name="Number" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                                                                     &lt;element name="Code" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                                                                   &lt;/sequence>
 *                                                                 &lt;/restriction>
 *                                                               &lt;/complexContent>
 *                                                             &lt;/complexType>
 *                                                           &lt;/element>
 *                                                         &lt;/sequence>
 *                                                       &lt;/restriction>
 *                                                     &lt;/complexContent>
 *                                                   &lt;/complexType>
 *                                                 &lt;/element>
 *                                               &lt;/sequence>
 *                                             &lt;/restriction>
 *                                           &lt;/complexContent>
 *                                         &lt;/complexType>
 *                                       &lt;/element>
 *                                     &lt;/sequence>
 *                                     &lt;attribute name="ResultId" type="{http://www.w3.org/2001/XMLSchema}string" />
 *                                     &lt;attribute name="PathId" type="{http://www.w3.org/2001/XMLSchema}string" />
 *                                     &lt;attribute name="SimilarityId" type="{http://www.w3.org/2001/XMLSchema}string" />
 *                                   &lt;/restriction>
 *                                 &lt;/complexContent>
 *                               &lt;/complexType>
 *                             &lt;/element>
 *                           &lt;/sequence>
 *                           &lt;attribute name="NodeId" type="{http://www.w3.org/2001/XMLSchema}string" />
 *                           &lt;attribute name="FileName" type="{http://www.w3.org/2001/XMLSchema}string" />
 *                           &lt;attribute name="Status" type="{http://www.w3.org/2001/XMLSchema}string" />
 *                           &lt;attribute name="Line" type="{http://www.w3.org/2001/XMLSchema}string" />
 *                           &lt;attribute name="Column" type="{http://www.w3.org/2001/XMLSchema}string" />
 *                           &lt;attribute name="FalsePositive" type="{http://www.w3.org/2001/XMLSchema}string" />
 *                           &lt;attribute name="Severity" type="{http://www.w3.org/2001/XMLSchema}string" />
 *                           &lt;attribute name="AssignToUser" type="{http://www.w3.org/2001/XMLSchema}string" />
 *                           &lt;attribute name="state" type="{http://www.w3.org/2001/XMLSchema}string" />
 *                           &lt;attribute name="Remark" type="{http://www.w3.org/2001/XMLSchema}string" />
 *                           &lt;attribute name="DeepLink" type="{http://www.w3.org/2001/XMLSchema}anyURI" />
 *                           &lt;attribute name="SeverityIndex" type="{http://www.w3.org/2001/XMLSchema}string" />
 *                         &lt;/restriction>
 *                       &lt;/complexContent>
 *                     &lt;/complexType>
 *                   &lt;/element>
 *                 &lt;/sequence>
 *                 &lt;attribute name="id" type="{http://www.w3.org/2001/XMLSchema}string" />
 *                 &lt;attribute name="categories" type="{http://www.w3.org/2001/XMLSchema}string" />
 *                 &lt;attribute name="cweId" type="{http://www.w3.org/2001/XMLSchema}string" />
 *                 &lt;attribute name="name" type="{http://www.w3.org/2001/XMLSchema}string" />
 *                 &lt;attribute name="group" type="{http://www.w3.org/2001/XMLSchema}string" />
 *                 &lt;attribute name="Severity" type="{http://www.w3.org/2001/XMLSchema}string" />
 *                 &lt;attribute name="Language" type="{http://www.w3.org/2001/XMLSchema}string" />
 *                 &lt;attribute name="LanguageHash" type="{http://www.w3.org/2001/XMLSchema}string" />
 *                 &lt;attribute name="LanguageChangeDate" type="{http://www.w3.org/2001/XMLSchema}dateTime" />
 *                 &lt;attribute name="SeverityIndex" type="{http://www.w3.org/2001/XMLSchema}string" />
 *                 &lt;attribute name="QueryPath" type="{http://www.w3.org/2001/XMLSchema}string" />
 *                 &lt;attribute name="QueryVersionCode" type="{http://www.w3.org/2001/XMLSchema}string" />
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *       &lt;/sequence>
 *       &lt;attribute name="InitiatorName" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="Owner" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="ScanId" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="ProjectId" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="ProjectName" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="TeamFullPathOnReportDate" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="DeepLink" type="{http://www.w3.org/2001/XMLSchema}anyURI" />
 *       &lt;attribute name="ScanStart" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="Preset" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="ScanTime" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="LinesOfCodeScanned" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="FilesScanned" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="ReportCreationTime" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="Team" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="CheckmarxVersion" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="ScanComments" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="ScanType" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="SourceOrigin" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="Visibility" type="{http://www.w3.org/2001/XMLSchema}string" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "query"
})
@XmlRootElement(name = "CxXMLResults")
public class CxXMLResults
{

    @XmlElement(name = "Query")
    protected List<Query> query;
    @XmlAttribute(name = "InitiatorName")
    protected String initiatorName;
    @XmlAttribute(name = "Owner")
    protected String owner;
    @XmlAttribute(name = "ScanId")
    protected String scanId;
    @XmlAttribute(name = "ProjectId")
    protected String projectId;
    @XmlAttribute(name = "ProjectName")
    protected String projectName;
    @XmlAttribute(name = "TeamFullPathOnReportDate")
    protected String teamFullPathOnReportDate;
    @XmlAttribute(name = "DeepLink")
    @XmlSchemaType(name = "anyURI")
    protected String deepLink;
    @XmlAttribute(name = "ScanStart")
    protected String scanStart;
    @XmlAttribute(name = "Preset")
    protected String preset;
    @XmlAttribute(name = "ScanTime")
    protected String scanTime;
    @XmlAttribute(name = "LinesOfCodeScanned")
    protected String linesOfCodeScanned;
    @XmlAttribute(name = "FilesScanned")
    protected String filesScanned;
    @XmlAttribute(name = "ReportCreationTime")
    protected String reportCreationTime;
    @XmlAttribute(name = "Team")
    protected String team;
    @XmlAttribute(name = "CheckmarxVersion")
    protected String checkmarxVersion;
    @XmlAttribute(name = "ScanComments")
    protected String scanComments;
    @XmlAttribute(name = "ScanType")
    protected String scanType;
    @XmlAttribute(name = "SourceOrigin")
    protected String sourceOrigin;
    @XmlAttribute(name = "Visibility")
    protected String visibility;

    /**
     * Gets the value of the query property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the query property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getQuery().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Query }
     * 
     * 
     */
    public List<Query> getQuery() {
        if (query == null) {
            query = new ArrayList<Query>();
        }
        return this.query;
    }

    /**
     * Gets the value of the initiatorName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getInitiatorName() {
        return initiatorName;
    }

    /**
     * Sets the value of the initiatorName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setInitiatorName(String value) {
        this.initiatorName = value;
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
     * Gets the value of the scanId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getScanId() {
        return scanId;
    }

    /**
     * Sets the value of the scanId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setScanId(String value) {
        this.scanId = value;
    }

    /**
     * Gets the value of the projectId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getProjectId() {
        return projectId;
    }

    /**
     * Sets the value of the projectId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setProjectId(String value) {
        this.projectId = value;
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
     * Gets the value of the teamFullPathOnReportDate property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTeamFullPathOnReportDate() {
        return teamFullPathOnReportDate;
    }

    /**
     * Sets the value of the teamFullPathOnReportDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTeamFullPathOnReportDate(String value) {
        this.teamFullPathOnReportDate = value;
    }

    /**
     * Gets the value of the deepLink property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDeepLink() {
        return deepLink;
    }

    /**
     * Sets the value of the deepLink property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDeepLink(String value) {
        this.deepLink = value;
    }

    /**
     * Gets the value of the scanStart property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getScanStart() {
        return scanStart;
    }

    /**
     * Sets the value of the scanStart property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setScanStart(String value) {
        this.scanStart = value;
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
     * Gets the value of the scanTime property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getScanTime() {
        return scanTime;
    }

    /**
     * Sets the value of the scanTime property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setScanTime(String value) {
        this.scanTime = value;
    }

    /**
     * Gets the value of the linesOfCodeScanned property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLinesOfCodeScanned() {
        return linesOfCodeScanned;
    }

    /**
     * Sets the value of the linesOfCodeScanned property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLinesOfCodeScanned(String value) {
        this.linesOfCodeScanned = value;
    }

    /**
     * Gets the value of the filesScanned property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFilesScanned() {
        return filesScanned;
    }

    /**
     * Sets the value of the filesScanned property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFilesScanned(String value) {
        this.filesScanned = value;
    }

    /**
     * Gets the value of the reportCreationTime property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getReportCreationTime() {
        return reportCreationTime;
    }

    /**
     * Sets the value of the reportCreationTime property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setReportCreationTime(String value) {
        this.reportCreationTime = value;
    }

    /**
     * Gets the value of the team property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTeam() {
        return team;
    }

    /**
     * Sets the value of the team property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTeam(String value) {
        this.team = value;
    }

    /**
     * Gets the value of the checkmarxVersion property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCheckmarxVersion() {
        return checkmarxVersion;
    }

    /**
     * Sets the value of the checkmarxVersion property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCheckmarxVersion(String value) {
        this.checkmarxVersion = value;
    }

    /**
     * Gets the value of the scanComments property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getScanComments() {
        return scanComments;
    }

    /**
     * Sets the value of the scanComments property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setScanComments(String value) {
        this.scanComments = value;
    }

    /**
     * Gets the value of the scanType property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getScanType() {
        return scanType;
    }

    /**
     * Sets the value of the scanType property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setScanType(String value) {
        this.scanType = value;
    }

    /**
     * Gets the value of the sourceOrigin property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSourceOrigin() {
        return sourceOrigin;
    }

    /**
     * Sets the value of the sourceOrigin property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSourceOrigin(String value) {
        this.sourceOrigin = value;
    }

    /**
     * Gets the value of the visibility property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getVisibility() {
        return visibility;
    }

    /**
     * Sets the value of the visibility property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setVisibility(String value) {
        this.visibility = value;
    }


    /**
     * <p>Java class for anonymous complex type.
     * 
     * <p>The following schema fragment specifies the expected content contained within this class.
     * 
     * <pre>
     * &lt;complexType>
     *   &lt;complexContent>
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *       &lt;sequence>
     *         &lt;element name="Result" maxOccurs="unbounded" minOccurs="0">
     *           &lt;complexType>
     *             &lt;complexContent>
     *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                 &lt;sequence>
     *                   &lt;element name="Path">
     *                     &lt;complexType>
     *                       &lt;complexContent>
     *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                           &lt;sequence>
     *                             &lt;element name="PathNode" maxOccurs="unbounded" minOccurs="0">
     *                               &lt;complexType>
     *                                 &lt;complexContent>
     *                                   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                                     &lt;sequence>
     *                                       &lt;element name="FileName" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *                                       &lt;element name="Line" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *                                       &lt;element name="Column" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *                                       &lt;element name="NodeId" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *                                       &lt;element name="Name" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *                                       &lt;element name="Type" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *                                       &lt;element name="Length" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *                                       &lt;element name="Snippet">
     *                                         &lt;complexType>
     *                                           &lt;complexContent>
     *                                             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                                               &lt;sequence>
     *                                                 &lt;element name="Line">
     *                                                   &lt;complexType>
     *                                                     &lt;complexContent>
     *                                                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                                                         &lt;sequence>
     *                                                           &lt;element name="Number" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *                                                           &lt;element name="Code" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *                                                         &lt;/sequence>
     *                                                       &lt;/restriction>
     *                                                     &lt;/complexContent>
     *                                                   &lt;/complexType>
     *                                                 &lt;/element>
     *                                               &lt;/sequence>
     *                                             &lt;/restriction>
     *                                           &lt;/complexContent>
     *                                         &lt;/complexType>
     *                                       &lt;/element>
     *                                     &lt;/sequence>
     *                                   &lt;/restriction>
     *                                 &lt;/complexContent>
     *                               &lt;/complexType>
     *                             &lt;/element>
     *                           &lt;/sequence>
     *                           &lt;attribute name="ResultId" type="{http://www.w3.org/2001/XMLSchema}string" />
     *                           &lt;attribute name="PathId" type="{http://www.w3.org/2001/XMLSchema}string" />
     *                           &lt;attribute name="SimilarityId" type="{http://www.w3.org/2001/XMLSchema}string" />
     *                         &lt;/restriction>
     *                       &lt;/complexContent>
     *                     &lt;/complexType>
     *                   &lt;/element>
     *                 &lt;/sequence>
     *                 &lt;attribute name="NodeId" type="{http://www.w3.org/2001/XMLSchema}string" />
     *                 &lt;attribute name="FileName" type="{http://www.w3.org/2001/XMLSchema}string" />
     *                 &lt;attribute name="Status" type="{http://www.w3.org/2001/XMLSchema}string" />
     *                 &lt;attribute name="Line" type="{http://www.w3.org/2001/XMLSchema}string" />
     *                 &lt;attribute name="Column" type="{http://www.w3.org/2001/XMLSchema}string" />
     *                 &lt;attribute name="FalsePositive" type="{http://www.w3.org/2001/XMLSchema}string" />
     *                 &lt;attribute name="Severity" type="{http://www.w3.org/2001/XMLSchema}string" />
     *                 &lt;attribute name="AssignToUser" type="{http://www.w3.org/2001/XMLSchema}string" />
     *                 &lt;attribute name="state" type="{http://www.w3.org/2001/XMLSchema}string" />
     *                 &lt;attribute name="Remark" type="{http://www.w3.org/2001/XMLSchema}string" />
     *                 &lt;attribute name="DeepLink" type="{http://www.w3.org/2001/XMLSchema}anyURI" />
     *                 &lt;attribute name="SeverityIndex" type="{http://www.w3.org/2001/XMLSchema}string" />
     *               &lt;/restriction>
     *             &lt;/complexContent>
     *           &lt;/complexType>
     *         &lt;/element>
     *       &lt;/sequence>
     *       &lt;attribute name="id" type="{http://www.w3.org/2001/XMLSchema}string" />
     *       &lt;attribute name="categories" type="{http://www.w3.org/2001/XMLSchema}string" />
     *       &lt;attribute name="cweId" type="{http://www.w3.org/2001/XMLSchema}string" />
     *       &lt;attribute name="name" type="{http://www.w3.org/2001/XMLSchema}string" />
     *       &lt;attribute name="group" type="{http://www.w3.org/2001/XMLSchema}string" />
     *       &lt;attribute name="Severity" type="{http://www.w3.org/2001/XMLSchema}string" />
     *       &lt;attribute name="Language" type="{http://www.w3.org/2001/XMLSchema}string" />
     *       &lt;attribute name="LanguageHash" type="{http://www.w3.org/2001/XMLSchema}string" />
     *       &lt;attribute name="LanguageChangeDate" type="{http://www.w3.org/2001/XMLSchema}dateTime" />
     *       &lt;attribute name="SeverityIndex" type="{http://www.w3.org/2001/XMLSchema}string" />
     *       &lt;attribute name="QueryPath" type="{http://www.w3.org/2001/XMLSchema}string" />
     *       &lt;attribute name="QueryVersionCode" type="{http://www.w3.org/2001/XMLSchema}string" />
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "result"
    })
    public static class Query {
        @Override
        public String toString() {
            return "{" +
                    "'name': '" + name +
                    "', 'severity': '" + severity +
                    "', 'resultLength': '" + result.size() +
                    "'}";
        }

        @XmlElement(name = "Result")
        protected List<Result> result;
        @XmlAttribute(name = "id")
        protected String id;
        @XmlAttribute(name = "categories")
        protected String categories;
        @XmlAttribute(name = "cweId")
        protected String cweId;
        @XmlAttribute(name = "name")
        protected String name;
        @XmlAttribute(name = "group")
        protected String group;
        @XmlAttribute(name = "Severity")
        protected String severity;
        @XmlAttribute(name = "Language")
        protected String language;
        @XmlAttribute(name = "LanguageHash")
        protected String languageHash;
        @XmlAttribute(name = "LanguageChangeDate")
        @XmlSchemaType(name = "dateTime")
        protected String languageChangeDate;
        @XmlAttribute(name = "SeverityIndex")
        protected String severityIndex;
        @XmlAttribute(name = "QueryPath")
        protected String queryPath;
        @XmlAttribute(name = "QueryVersionCode")
        protected String queryVersionCode;

        /**
         * Gets the value of the result property.
         * 
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the result property.
         * 
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getResult().add(newItem);
         * </pre>
         * 
         * 
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link Result }
         * 
         * 
         */
        public List<Result> getResult() {
            if (result == null) {
                result = new ArrayList<Result>();
            }
            return this.result;
        }

        /**
         * Gets the value of the id property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getId() {
            return id;
        }

        /**
         * Sets the value of the id property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setId(String value) {
            this.id = value;
        }

        /**
         * Gets the value of the categories property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getCategories() {
            return categories;
        }

        /**
         * Sets the value of the categories property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setCategories(String value) {
            this.categories = value;
        }

        /**
         * Gets the value of the cweId property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getCweId() {
            return cweId;
        }

        /**
         * Sets the value of the cweId property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setCweId(String value) {
            this.cweId = value;
        }

        /**
         * Gets the value of the name property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getName() {
            return name;
        }

        /**
         * Sets the value of the name property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setName(String value) {
            this.name = value;
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
         * Gets the value of the severity property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getSeverity() {
            return severity;
        }

        /**
         * Sets the value of the severity property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setSeverity(String value) {
            this.severity = value;
        }

        /**
         * Gets the value of the language property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getLanguage() {
            return language;
        }

        /**
         * Sets the value of the language property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setLanguage(String value) {
            this.language = value;
        }

        /**
         * Gets the value of the languageHash property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getLanguageHash() {
            return languageHash;
        }

        /**
         * Sets the value of the languageHash property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setLanguageHash(String value) {
            this.languageHash = value;
        }

        /**
         * Gets the value of the languageChangeDate property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getLanguageChangeDate() {
            return languageChangeDate;
        }

        /**
         * Sets the value of the languageChangeDate property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setLanguageChangeDate(String value) {
            this.languageChangeDate = value;
        }

        /**
         * Gets the value of the severityIndex property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getSeverityIndex() {
            return severityIndex;
        }

        /**
         * Sets the value of the severityIndex property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setSeverityIndex(String value) {
            this.severityIndex = value;
        }

        /**
         * Gets the value of the queryPath property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getQueryPath() {
            return queryPath;
        }

        /**
         * Sets the value of the queryPath property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setQueryPath(String value) {
            this.queryPath = value;
        }

        /**
         * Gets the value of the queryVersionCode property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getQueryVersionCode() {
            return queryVersionCode;
        }

        /**
         * Sets the value of the queryVersionCode property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setQueryVersionCode(String value) {
            this.queryVersionCode = value;
        }


        /**
         * <p>Java class for anonymous complex type.
         * 
         * <p>The following schema fragment specifies the expected content contained within this class.
         * 
         * <pre>
         * &lt;complexType>
         *   &lt;complexContent>
         *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
         *       &lt;sequence>
         *         &lt;element name="Path">
         *           &lt;complexType>
         *             &lt;complexContent>
         *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
         *                 &lt;sequence>
         *                   &lt;element name="PathNode" maxOccurs="unbounded" minOccurs="0">
         *                     &lt;complexType>
         *                       &lt;complexContent>
         *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
         *                           &lt;sequence>
         *                             &lt;element name="FileName" type="{http://www.w3.org/2001/XMLSchema}string"/>
         *                             &lt;element name="Line" type="{http://www.w3.org/2001/XMLSchema}string"/>
         *                             &lt;element name="Column" type="{http://www.w3.org/2001/XMLSchema}string"/>
         *                             &lt;element name="NodeId" type="{http://www.w3.org/2001/XMLSchema}string"/>
         *                             &lt;element name="Name" type="{http://www.w3.org/2001/XMLSchema}string"/>
         *                             &lt;element name="Type" type="{http://www.w3.org/2001/XMLSchema}string"/>
         *                             &lt;element name="Length" type="{http://www.w3.org/2001/XMLSchema}string"/>
         *                             &lt;element name="Snippet">
         *                               &lt;complexType>
         *                                 &lt;complexContent>
         *                                   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
         *                                     &lt;sequence>
         *                                       &lt;element name="Line">
         *                                         &lt;complexType>
         *                                           &lt;complexContent>
         *                                             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
         *                                               &lt;sequence>
         *                                                 &lt;element name="Number" type="{http://www.w3.org/2001/XMLSchema}string"/>
         *                                                 &lt;element name="Code" type="{http://www.w3.org/2001/XMLSchema}string"/>
         *                                               &lt;/sequence>
         *                                             &lt;/restriction>
         *                                           &lt;/complexContent>
         *                                         &lt;/complexType>
         *                                       &lt;/element>
         *                                     &lt;/sequence>
         *                                   &lt;/restriction>
         *                                 &lt;/complexContent>
         *                               &lt;/complexType>
         *                             &lt;/element>
         *                           &lt;/sequence>
         *                         &lt;/restriction>
         *                       &lt;/complexContent>
         *                     &lt;/complexType>
         *                   &lt;/element>
         *                 &lt;/sequence>
         *                 &lt;attribute name="ResultId" type="{http://www.w3.org/2001/XMLSchema}string" />
         *                 &lt;attribute name="PathId" type="{http://www.w3.org/2001/XMLSchema}string" />
         *                 &lt;attribute name="SimilarityId" type="{http://www.w3.org/2001/XMLSchema}string" />
         *               &lt;/restriction>
         *             &lt;/complexContent>
         *           &lt;/complexType>
         *         &lt;/element>
         *       &lt;/sequence>
         *       &lt;attribute name="NodeId" type="{http://www.w3.org/2001/XMLSchema}string" />
         *       &lt;attribute name="FileName" type="{http://www.w3.org/2001/XMLSchema}string" />
         *       &lt;attribute name="Status" type="{http://www.w3.org/2001/XMLSchema}string" />
         *       &lt;attribute name="Line" type="{http://www.w3.org/2001/XMLSchema}string" />
         *       &lt;attribute name="Column" type="{http://www.w3.org/2001/XMLSchema}string" />
         *       &lt;attribute name="FalsePositive" type="{http://www.w3.org/2001/XMLSchema}string" />
         *       &lt;attribute name="Severity" type="{http://www.w3.org/2001/XMLSchema}string" />
         *       &lt;attribute name="AssignToUser" type="{http://www.w3.org/2001/XMLSchema}string" />
         *       &lt;attribute name="state" type="{http://www.w3.org/2001/XMLSchema}string" />
         *       &lt;attribute name="Remark" type="{http://www.w3.org/2001/XMLSchema}string" />
         *       &lt;attribute name="DeepLink" type="{http://www.w3.org/2001/XMLSchema}anyURI" />
         *       &lt;attribute name="SeverityIndex" type="{http://www.w3.org/2001/XMLSchema}string" />
         *     &lt;/restriction>
         *   &lt;/complexContent>
         * &lt;/complexType>
         * </pre>
         * 
         * 
         */
        @XmlAccessorType(XmlAccessType.FIELD)
        @XmlType(name = "", propOrder = {
            "path"
        })
        public static class Result {

            @XmlElement(name = "Path", required = true)
            protected Path path;
            @XmlAttribute(name = "NodeId")
            protected String nodeId;
            @XmlAttribute(name = "FileName")
            protected String fileName;
            @XmlAttribute(name = "Status")
            protected String status;
            @XmlAttribute(name = "Line")
            protected String line;
            @XmlAttribute(name = "Column")
            protected String column;
            @XmlAttribute(name = "FalsePositive")
            protected String falsePositive;
            @XmlAttribute(name = "Severity")
            protected String severity;
            @XmlAttribute(name = "AssignToUser")
            protected String assignToUser;
            @XmlAttribute(name = "state")
            protected String state;
            @XmlAttribute(name = "Remark")
            protected String remark;
            @XmlAttribute(name = "DeepLink")
            @XmlSchemaType(name = "anyURI")
            protected String deepLink;
            @XmlAttribute(name = "SeverityIndex")
            protected String severityIndex;

            /**
             * Gets the value of the path property.
             * 
             * @return
             *     possible object is
             *     {@link Path }
             *     
             */
            public Path getPath() {
                return path;
            }

            /**
             * Sets the value of the path property.
             * 
             * @param value
             *     allowed object is
             *     {@link Path }
             *     
             */
            public void setPath(Path value) {
                this.path = value;
            }

            /**
             * Gets the value of the nodeId property.
             * 
             * @return
             *     possible object is
             *     {@link String }
             *     
             */
            public String getNodeId() {
                return nodeId;
            }

            /**
             * Sets the value of the nodeId property.
             * 
             * @param value
             *     allowed object is
             *     {@link String }
             *     
             */
            public void setNodeId(String value) {
                this.nodeId = value;
            }

            /**
             * Gets the value of the fileName property.
             * 
             * @return
             *     possible object is
             *     {@link String }
             *     
             */
            public String getFileName() {
                return fileName;
            }

            /**
             * Sets the value of the fileName property.
             * 
             * @param value
             *     allowed object is
             *     {@link String }
             *     
             */
            public void setFileName(String value) {
                this.fileName = value;
            }

            /**
             * Gets the value of the status property.
             * 
             * @return
             *     possible object is
             *     {@link String }
             *     
             */
            public String getStatus() {
                return status;
            }

            /**
             * Sets the value of the status property.
             * 
             * @param value
             *     allowed object is
             *     {@link String }
             *     
             */
            public void setStatus(String value) {
                this.status = value;
            }

            /**
             * Gets the value of the line property.
             * 
             * @return
             *     possible object is
             *     {@link String }
             *     
             */
            public String getLine() {
                return line;
            }

            /**
             * Sets the value of the line property.
             * 
             * @param value
             *     allowed object is
             *     {@link String }
             *     
             */
            public void setLine(String value) {
                this.line = value;
            }

            /**
             * Gets the value of the column property.
             * 
             * @return
             *     possible object is
             *     {@link String }
             *     
             */
            public String getColumn() {
                return column;
            }

            /**
             * Sets the value of the column property.
             * 
             * @param value
             *     allowed object is
             *     {@link String }
             *     
             */
            public void setColumn(String value) {
                this.column = value;
            }

            /**
             * Gets the value of the falsePositive property.
             * 
             * @return
             *     possible object is
             *     {@link String }
             *     
             */
            public String getFalsePositive() {
                return falsePositive;
            }

            /**
             * Sets the value of the falsePositive property.
             * 
             * @param value
             *     allowed object is
             *     {@link String }
             *     
             */
            public void setFalsePositive(String value) {
                this.falsePositive = value;
            }

            /**
             * Gets the value of the severity property.
             * 
             * @return
             *     possible object is
             *     {@link String }
             *     
             */
            public String getSeverity() {
                return severity;
            }

            /**
             * Sets the value of the severity property.
             * 
             * @param value
             *     allowed object is
             *     {@link String }
             *     
             */
            public void setSeverity(String value) {
                this.severity = value;
            }

            /**
             * Gets the value of the assignToUser property.
             * 
             * @return
             *     possible object is
             *     {@link String }
             *     
             */
            public String getAssignToUser() {
                return assignToUser;
            }

            /**
             * Sets the value of the assignToUser property.
             * 
             * @param value
             *     allowed object is
             *     {@link String }
             *     
             */
            public void setAssignToUser(String value) {
                this.assignToUser = value;
            }

            /**
             * Gets the value of the state property.
             * 
             * @return
             *     possible object is
             *     {@link String }
             *     
             */
            public String getState() {
                return state;
            }

            /**
             * Sets the value of the state property.
             * 
             * @param value
             *     allowed object is
             *     {@link String }
             *     
             */
            public void setState(String value) {
                this.state = value;
            }

            /**
             * Gets the value of the remark property.
             * 
             * @return
             *     possible object is
             *     {@link String }
             *     
             */
            public String getRemark() {
                return remark;
            }

            /**
             * Sets the value of the remark property.
             * 
             * @param value
             *     allowed object is
             *     {@link String }
             *     
             */
            public void setRemark(String value) {
                this.remark = value;
            }

            /**
             * Gets the value of the deepLink property.
             * 
             * @return
             *     possible object is
             *     {@link String }
             *     
             */
            public String getDeepLink() {
                return deepLink;
            }

            /**
             * Sets the value of the deepLink property.
             * 
             * @param value
             *     allowed object is
             *     {@link String }
             *     
             */
            public void setDeepLink(String value) {
                this.deepLink = value;
            }

            /**
             * Gets the value of the severityIndex property.
             * 
             * @return
             *     possible object is
             *     {@link String }
             *     
             */
            public String getSeverityIndex() {
                return severityIndex;
            }

            /**
             * Sets the value of the severityIndex property.
             * 
             * @param value
             *     allowed object is
             *     {@link String }
             *     
             */
            public void setSeverityIndex(String value) {
                this.severityIndex = value;
            }


            /**
             * <p>Java class for anonymous complex type.
             * 
             * <p>The following schema fragment specifies the expected content contained within this class.
             * 
             * <pre>
             * &lt;complexType>
             *   &lt;complexContent>
             *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
             *       &lt;sequence>
             *         &lt;element name="PathNode" maxOccurs="unbounded" minOccurs="0">
             *           &lt;complexType>
             *             &lt;complexContent>
             *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
             *                 &lt;sequence>
             *                   &lt;element name="FileName" type="{http://www.w3.org/2001/XMLSchema}string"/>
             *                   &lt;element name="Line" type="{http://www.w3.org/2001/XMLSchema}string"/>
             *                   &lt;element name="Column" type="{http://www.w3.org/2001/XMLSchema}string"/>
             *                   &lt;element name="NodeId" type="{http://www.w3.org/2001/XMLSchema}string"/>
             *                   &lt;element name="Name" type="{http://www.w3.org/2001/XMLSchema}string"/>
             *                   &lt;element name="Type" type="{http://www.w3.org/2001/XMLSchema}string"/>
             *                   &lt;element name="Length" type="{http://www.w3.org/2001/XMLSchema}string"/>
             *                   &lt;element name="Snippet">
             *                     &lt;complexType>
             *                       &lt;complexContent>
             *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
             *                           &lt;sequence>
             *                             &lt;element name="Line">
             *                               &lt;complexType>
             *                                 &lt;complexContent>
             *                                   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
             *                                     &lt;sequence>
             *                                       &lt;element name="Number" type="{http://www.w3.org/2001/XMLSchema}string"/>
             *                                       &lt;element name="Code" type="{http://www.w3.org/2001/XMLSchema}string"/>
             *                                     &lt;/sequence>
             *                                   &lt;/restriction>
             *                                 &lt;/complexContent>
             *                               &lt;/complexType>
             *                             &lt;/element>
             *                           &lt;/sequence>
             *                         &lt;/restriction>
             *                       &lt;/complexContent>
             *                     &lt;/complexType>
             *                   &lt;/element>
             *                 &lt;/sequence>
             *               &lt;/restriction>
             *             &lt;/complexContent>
             *           &lt;/complexType>
             *         &lt;/element>
             *       &lt;/sequence>
             *       &lt;attribute name="ResultId" type="{http://www.w3.org/2001/XMLSchema}string" />
             *       &lt;attribute name="PathId" type="{http://www.w3.org/2001/XMLSchema}string" />
             *       &lt;attribute name="SimilarityId" type="{http://www.w3.org/2001/XMLSchema}string" />
             *     &lt;/restriction>
             *   &lt;/complexContent>
             * &lt;/complexType>
             * </pre>
             * 
             * 
             */
            @XmlAccessorType(XmlAccessType.FIELD)
            @XmlType(name = "", propOrder = {
                "pathNode"
            })
            public static class Path {

                @XmlElement(name = "PathNode")
                protected List<PathNode> pathNode;
                @XmlAttribute(name = "ResultId")
                protected String resultId;
                @XmlAttribute(name = "PathId")
                protected String pathId;
                @XmlAttribute(name = "SimilarityId")
                protected String similarityId;

                /**
                 * Gets the value of the pathNode property.
                 * 
                 * <p>
                 * This accessor method returns a reference to the live list,
                 * not a snapshot. Therefore any modification you make to the
                 * returned list will be present inside the JAXB object.
                 * This is why there is not a <CODE>set</CODE> method for the pathNode property.
                 * 
                 * <p>
                 * For example, to add a new item, do as follows:
                 * <pre>
                 *    getPathNode().add(newItem);
                 * </pre>
                 * 
                 * 
                 * <p>
                 * Objects of the following type(s) are allowed in the list
                 * {@link PathNode }
                 * 
                 * 
                 */
                public List<PathNode> getPathNode() {
                    if (pathNode == null) {
                        pathNode = new ArrayList<PathNode>();
                    }
                    return this.pathNode;
                }

                /**
                 * Gets the value of the resultId property.
                 * 
                 * @return
                 *     possible object is
                 *     {@link String }
                 *     
                 */
                public String getResultId() {
                    return resultId;
                }

                /**
                 * Sets the value of the resultId property.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link String }
                 *     
                 */
                public void setResultId(String value) {
                    this.resultId = value;
                }

                /**
                 * Gets the value of the pathId property.
                 * 
                 * @return
                 *     possible object is
                 *     {@link String }
                 *     
                 */
                public String getPathId() {
                    return pathId;
                }

                /**
                 * Sets the value of the pathId property.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link String }
                 *     
                 */
                public void setPathId(String value) {
                    this.pathId = value;
                }

                /**
                 * Gets the value of the similarityId property.
                 * 
                 * @return
                 *     possible object is
                 *     {@link String }
                 *     
                 */
                public String getSimilarityId() {
                    return similarityId;
                }

                /**
                 * Sets the value of the similarityId property.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link String }
                 *     
                 */
                public void setSimilarityId(String value) {
                    this.similarityId = value;
                }


                /**
                 * <p>Java class for anonymous complex type.
                 * 
                 * <p>The following schema fragment specifies the expected content contained within this class.
                 * 
                 * <pre>
                 * &lt;complexType>
                 *   &lt;complexContent>
                 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
                 *       &lt;sequence>
                 *         &lt;element name="FileName" type="{http://www.w3.org/2001/XMLSchema}string"/>
                 *         &lt;element name="Line" type="{http://www.w3.org/2001/XMLSchema}string"/>
                 *         &lt;element name="Column" type="{http://www.w3.org/2001/XMLSchema}string"/>
                 *         &lt;element name="NodeId" type="{http://www.w3.org/2001/XMLSchema}string"/>
                 *         &lt;element name="Name" type="{http://www.w3.org/2001/XMLSchema}string"/>
                 *         &lt;element name="Type" type="{http://www.w3.org/2001/XMLSchema}string"/>
                 *         &lt;element name="Length" type="{http://www.w3.org/2001/XMLSchema}string"/>
                 *         &lt;element name="Snippet">
                 *           &lt;complexType>
                 *             &lt;complexContent>
                 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
                 *                 &lt;sequence>
                 *                   &lt;element name="Line">
                 *                     &lt;complexType>
                 *                       &lt;complexContent>
                 *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
                 *                           &lt;sequence>
                 *                             &lt;element name="Number" type="{http://www.w3.org/2001/XMLSchema}string"/>
                 *                             &lt;element name="Code" type="{http://www.w3.org/2001/XMLSchema}string"/>
                 *                           &lt;/sequence>
                 *                         &lt;/restriction>
                 *                       &lt;/complexContent>
                 *                     &lt;/complexType>
                 *                   &lt;/element>
                 *                 &lt;/sequence>
                 *               &lt;/restriction>
                 *             &lt;/complexContent>
                 *           &lt;/complexType>
                 *         &lt;/element>
                 *       &lt;/sequence>
                 *     &lt;/restriction>
                 *   &lt;/complexContent>
                 * &lt;/complexType>
                 * </pre>
                 * 
                 * 
                 */
                @XmlAccessorType(XmlAccessType.FIELD)
                @XmlType(name = "", propOrder = {
                    "fileName",
                    "line",
                    "column",
                    "nodeId",
                    "name",
                    "type",
                    "length",
                    "snippet"
                })
                public static class PathNode {

                    @XmlElement(name = "FileName", required = true)
                    protected String fileName;
                    @XmlElement(name = "Line")
                    protected String line;
                    @XmlElement(name = "Column")
                    protected String column;
                    @XmlElement(name = "NodeId")
                    protected String nodeId;
                    @XmlElement(name = "Name", required = true)
                    protected String name;
                    @XmlElement(name = "Type", required = true)
                    protected String type;
                    @XmlElement(name = "Length")
                    protected String length;
                    @XmlElement(name = "Snippet", required = true)
                    protected Snippet snippet;

                    /**
                     * Gets the value of the fileName property.
                     * 
                     * @return
                     *     possible object is
                     *     {@link String }
                     *     
                     */
                    public String getFileName() {
                        return fileName;
                    }

                    /**
                     * Sets the value of the fileName property.
                     * 
                     * @param value
                     *     allowed object is
                     *     {@link String }
                     *     
                     */
                    public void setFileName(String value) {
                        this.fileName = value;
                    }

                    /**
                     * Gets the value of the line property.
                     * 
                     */
                    public String getLine() {
                        return line;
                    }

                    /**
                     * Sets the value of the line property.
                     * 
                     */
                    public void setLine(String value) {
                        this.line = value;
                    }

                    /**
                     * Gets the value of the column property.
                     * 
                     */
                    public String getColumn() {
                        return column;
                    }

                    /**
                     * Sets the value of the column property.
                     * 
                     */
                    public void setColumn(String value) {
                        this.column = value;
                    }

                    /**
                     * Gets the value of the nodeId property.
                     * 
                     */
                    public String getNodeId() {
                        return nodeId;
                    }

                    /**
                     * Sets the value of the nodeId property.
                     * 
                     */
                    public void setNodeId(String value) {
                        this.nodeId = value;
                    }

                    /**
                     * Gets the value of the name property.
                     * 
                     * @return
                     *     possible object is
                     *     {@link String }
                     *     
                     */
                    public String getName() {
                        return name;
                    }

                    /**
                     * Sets the value of the name property.
                     * 
                     * @param value
                     *     allowed object is
                     *     {@link String }
                     *     
                     */
                    public void setName(String value) {
                        this.name = value;
                    }

                    /**
                     * Gets the value of the type property.
                     * 
                     * @return
                     *     possible object is
                     *     {@link String }
                     *     
                     */
                    public String getType() {
                        return type;
                    }

                    /**
                     * Sets the value of the type property.
                     * 
                     * @param value
                     *     allowed object is
                     *     {@link String }
                     *     
                     */
                    public void setType(String value) {
                        this.type = value;
                    }

                    /**
                     * Gets the value of the length property.
                     * 
                     */
                    public String getLength() {
                        return length;
                    }

                    /**
                     * Sets the value of the length property.
                     * 
                     */
                    public void setLength(String value) {
                        this.length = value;
                    }

                    /**
                     * Gets the value of the snippet property.
                     * 
                     * @return
                     *     possible object is
                     *     {@link Snippet }
                     *     
                     */
                    public Snippet getSnippet() {
                        return snippet;
                    }

                    /**
                     * Sets the value of the snippet property.
                     * 
                     * @param value
                     *     allowed object is
                     *     {@link Snippet }
                     *     
                     */
                    public void setSnippet(Snippet value) {
                        this.snippet = value;
                    }


                    /**
                     * <p>Java class for anonymous complex type.
                     * 
                     * <p>The following schema fragment specifies the expected content contained within this class.
                     * 
                     * <pre>
                     * &lt;complexType>
                     *   &lt;complexContent>
                     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
                     *       &lt;sequence>
                     *         &lt;element name="Line">
                     *           &lt;complexType>
                     *             &lt;complexContent>
                     *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
                     *                 &lt;sequence>
                     *                   &lt;element name="Number" type="{http://www.w3.org/2001/XMLSchema}string"/>
                     *                   &lt;element name="Code" type="{http://www.w3.org/2001/XMLSchema}string"/>
                     *                 &lt;/sequence>
                     *               &lt;/restriction>
                     *             &lt;/complexContent>
                     *           &lt;/complexType>
                     *         &lt;/element>
                     *       &lt;/sequence>
                     *     &lt;/restriction>
                     *   &lt;/complexContent>
                     * &lt;/complexType>
                     * </pre>
                     * 
                     * 
                     */
                    @XmlAccessorType(XmlAccessType.FIELD)
                    @XmlType(name = "", propOrder = {
                        "line"
                    })
                    public static class Snippet {

                        @XmlElement(name = "Line", required = true)
                        protected Line line;

                        /**
                         * Gets the value of the line property.
                         * 
                         * @return
                         *     possible object is
                         *     {@link Line }
                         *     
                         */
                        public Line getLine() {
                            return line;
                        }

                        /**
                         * Sets the value of the line property.
                         * 
                         * @param value
                         *     allowed object is
                         *     {@link Line }
                         *     
                         */
                        public void setLine(Line value) {
                            this.line = value;
                        }


                        /**
                         * <p>Java class for anonymous complex type.
                         * 
                         * <p>The following schema fragment specifies the expected content contained within this class.
                         * 
                         * <pre>
                         * &lt;complexType>
                         *   &lt;complexContent>
                         *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
                         *       &lt;sequence>
                         *         &lt;element name="Number" type="{http://www.w3.org/2001/XMLSchema}string"/>
                         *         &lt;element name="Code" type="{http://www.w3.org/2001/XMLSchema}string"/>
                         *       &lt;/sequence>
                         *     &lt;/restriction>
                         *   &lt;/complexContent>
                         * &lt;/complexType>
                         * </pre>
                         * 
                         * 
                         */
                        @XmlAccessorType(XmlAccessType.FIELD)
                        @XmlType(name = "", propOrder = {
                            "number",
                            "code"
                        })
                        public static class Line {

                            @XmlElement(name = "Number")
                            protected String number;
                            @XmlElement(name = "Code", required = true)
                            protected String code;

                            /**
                             * Gets the value of the number property.
                             * 
                             */
                            public String getNumber() {
                                return number;
                            }

                            /**
                             * Sets the value of the number property.
                             * 
                             */
                            public void setNumber(String value) {
                                this.number = value;
                            }

                            /**
                             * Gets the value of the code property.
                             * 
                             * @return
                             *     possible object is
                             *     {@link String }
                             *     
                             */
                            public String getCode() {
                                return code;
                            }

                            /**
                             * Sets the value of the code property.
                             * 
                             * @param value
                             *     allowed object is
                             *     {@link String }
                             *     
                             */
                            public void setCode(String value) {
                                this.code = value;
                            }

                        }


                    }

                }

            }

        }

    }

}
