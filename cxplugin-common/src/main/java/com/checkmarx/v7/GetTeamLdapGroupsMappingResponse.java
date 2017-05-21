
package com.checkmarx.v7;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="GetTeamLdapGroupsMappingResult" type="{http://Checkmarx.com/v7}CxWSResponseTeamLdapGroupMappingData" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "getTeamLdapGroupsMappingResult"
})
@XmlRootElement(name = "GetTeamLdapGroupsMappingResponse")
public class GetTeamLdapGroupsMappingResponse {

    @XmlElement(name = "GetTeamLdapGroupsMappingResult")
    protected CxWSResponseTeamLdapGroupMappingData getTeamLdapGroupsMappingResult;

    /**
     * Gets the value of the getTeamLdapGroupsMappingResult property.
     * 
     * @return
     *     possible object is
     *     {@link CxWSResponseTeamLdapGroupMappingData }
     *     
     */
    public CxWSResponseTeamLdapGroupMappingData getGetTeamLdapGroupsMappingResult() {
        return getTeamLdapGroupsMappingResult;
    }

    /**
     * Sets the value of the getTeamLdapGroupsMappingResult property.
     * 
     * @param value
     *     allowed object is
     *     {@link CxWSResponseTeamLdapGroupMappingData }
     *     
     */
    public void setGetTeamLdapGroupsMappingResult(CxWSResponseTeamLdapGroupMappingData value) {
        this.getTeamLdapGroupsMappingResult = value;
    }

}
