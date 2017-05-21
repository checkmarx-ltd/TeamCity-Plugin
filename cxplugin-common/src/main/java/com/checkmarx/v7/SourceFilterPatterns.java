
package com.checkmarx.v7;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for SourceFilterPatterns complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="SourceFilterPatterns"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="ExcludeFilesPatterns" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="ExcludeFoldersPatterns" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "SourceFilterPatterns", propOrder = {
    "excludeFilesPatterns",
    "excludeFoldersPatterns"
})
public class SourceFilterPatterns {

    @XmlElement(name = "ExcludeFilesPatterns")
    protected String excludeFilesPatterns;
    @XmlElement(name = "ExcludeFoldersPatterns")
    protected String excludeFoldersPatterns;

    /**
     * Gets the value of the excludeFilesPatterns property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getExcludeFilesPatterns() {
        return excludeFilesPatterns;
    }

    /**
     * Sets the value of the excludeFilesPatterns property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setExcludeFilesPatterns(String value) {
        this.excludeFilesPatterns = value;
    }

    /**
     * Gets the value of the excludeFoldersPatterns property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getExcludeFoldersPatterns() {
        return excludeFoldersPatterns;
    }

    /**
     * Sets the value of the excludeFoldersPatterns property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setExcludeFoldersPatterns(String value) {
        this.excludeFoldersPatterns = value;
    }

}
