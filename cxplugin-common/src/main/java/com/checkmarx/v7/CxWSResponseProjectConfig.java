
package com.checkmarx.v7;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for CxWSResponseProjectConfig complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="CxWSResponseProjectConfig"&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{http://Checkmarx.com/v7}CxWSBasicRepsonse"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="ProjectConfig" type="{http://Checkmarx.com/v7}ProjectConfiguration" minOccurs="0"/&gt;
 *         &lt;element name="Permission" type="{http://Checkmarx.com/v7}UserPermission" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/extension&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "CxWSResponseProjectConfig", propOrder = {
    "projectConfig",
    "permission"
})
public class CxWSResponseProjectConfig
    extends CxWSBasicRepsonse
{

    @XmlElement(name = "ProjectConfig")
    protected ProjectConfiguration projectConfig;
    @XmlElement(name = "Permission")
    protected UserPermission permission;

    /**
     * Gets the value of the projectConfig property.
     * 
     * @return
     *     possible object is
     *     {@link ProjectConfiguration }
     *     
     */
    public ProjectConfiguration getProjectConfig() {
        return projectConfig;
    }

    /**
     * Sets the value of the projectConfig property.
     * 
     * @param value
     *     allowed object is
     *     {@link ProjectConfiguration }
     *     
     */
    public void setProjectConfig(ProjectConfiguration value) {
        this.projectConfig = value;
    }

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

}
