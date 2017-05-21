
package com.checkmarx.v7;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for CliScanArgs complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="CliScanArgs"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="PrjSettings" type="{http://Checkmarx.com/v7}ProjectSettings" minOccurs="0"/&gt;
 *         &lt;element name="SrcCodeSettings" type="{http://Checkmarx.com/v7}SourceCodeSettings" minOccurs="0"/&gt;
 *         &lt;element name="IsPrivateScan" type="{http://www.w3.org/2001/XMLSchema}boolean"/&gt;
 *         &lt;element name="IsIncremental" type="{http://www.w3.org/2001/XMLSchema}boolean"/&gt;
 *         &lt;element name="Comment" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="IgnoreScanWithUnchangedCode" type="{http://www.w3.org/2001/XMLSchema}boolean"/&gt;
 *         &lt;element name="ClientOrigin" type="{http://Checkmarx.com/v7}CxClientType"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "CliScanArgs", propOrder = {
    "prjSettings",
    "srcCodeSettings",
    "isPrivateScan",
    "isIncremental",
    "comment",
    "ignoreScanWithUnchangedCode",
    "clientOrigin"
})
public class CliScanArgs {

    @XmlElement(name = "PrjSettings")
    protected ProjectSettings prjSettings;
    @XmlElement(name = "SrcCodeSettings")
    protected SourceCodeSettings srcCodeSettings;
    @XmlElement(name = "IsPrivateScan")
    protected boolean isPrivateScan;
    @XmlElement(name = "IsIncremental")
    protected boolean isIncremental;
    @XmlElement(name = "Comment")
    protected String comment;
    @XmlElement(name = "IgnoreScanWithUnchangedCode")
    protected boolean ignoreScanWithUnchangedCode;
    @XmlElement(name = "ClientOrigin", required = true)
    @XmlSchemaType(name = "string")
    protected CxClientType clientOrigin;

    /**
     * Gets the value of the prjSettings property.
     * 
     * @return
     *     possible object is
     *     {@link ProjectSettings }
     *     
     */
    public ProjectSettings getPrjSettings() {
        return prjSettings;
    }

    /**
     * Sets the value of the prjSettings property.
     * 
     * @param value
     *     allowed object is
     *     {@link ProjectSettings }
     *     
     */
    public void setPrjSettings(ProjectSettings value) {
        this.prjSettings = value;
    }

    /**
     * Gets the value of the srcCodeSettings property.
     * 
     * @return
     *     possible object is
     *     {@link SourceCodeSettings }
     *     
     */
    public SourceCodeSettings getSrcCodeSettings() {
        return srcCodeSettings;
    }

    /**
     * Sets the value of the srcCodeSettings property.
     * 
     * @param value
     *     allowed object is
     *     {@link SourceCodeSettings }
     *     
     */
    public void setSrcCodeSettings(SourceCodeSettings value) {
        this.srcCodeSettings = value;
    }

    /**
     * Gets the value of the isPrivateScan property.
     * 
     */
    public boolean isIsPrivateScan() {
        return isPrivateScan;
    }

    /**
     * Sets the value of the isPrivateScan property.
     * 
     */
    public void setIsPrivateScan(boolean value) {
        this.isPrivateScan = value;
    }

    /**
     * Gets the value of the isIncremental property.
     * 
     */
    public boolean isIsIncremental() {
        return isIncremental;
    }

    /**
     * Sets the value of the isIncremental property.
     * 
     */
    public void setIsIncremental(boolean value) {
        this.isIncremental = value;
    }

    /**
     * Gets the value of the comment property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getComment() {
        return comment;
    }

    /**
     * Sets the value of the comment property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setComment(String value) {
        this.comment = value;
    }

    /**
     * Gets the value of the ignoreScanWithUnchangedCode property.
     * 
     */
    public boolean isIgnoreScanWithUnchangedCode() {
        return ignoreScanWithUnchangedCode;
    }

    /**
     * Sets the value of the ignoreScanWithUnchangedCode property.
     * 
     */
    public void setIgnoreScanWithUnchangedCode(boolean value) {
        this.ignoreScanWithUnchangedCode = value;
    }

    /**
     * Gets the value of the clientOrigin property.
     * 
     * @return
     *     possible object is
     *     {@link CxClientType }
     *     
     */
    public CxClientType getClientOrigin() {
        return clientOrigin;
    }

    /**
     * Sets the value of the clientOrigin property.
     * 
     * @param value
     *     allowed object is
     *     {@link CxClientType }
     *     
     */
    public void setClientOrigin(CxClientType value) {
        this.clientOrigin = value;
    }

}
