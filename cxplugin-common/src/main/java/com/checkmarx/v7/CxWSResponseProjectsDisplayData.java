
package com.checkmarx.v7;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for CxWSResponseProjectsDisplayData complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="CxWSResponseProjectsDisplayData"&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{http://Checkmarx.com/v7}CxWSBasicRepsonse"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="projectList" type="{http://Checkmarx.com/v7}ArrayOfProjectDisplayData" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/extension&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "CxWSResponseProjectsDisplayData", propOrder = {
    "projectList"
})
public class CxWSResponseProjectsDisplayData
    extends CxWSBasicRepsonse
{

    protected ArrayOfProjectDisplayData projectList;

    /**
     * Gets the value of the projectList property.
     * 
     * @return
     *     possible object is
     *     {@link ArrayOfProjectDisplayData }
     *     
     */
    public ArrayOfProjectDisplayData getProjectList() {
        return projectList;
    }

    /**
     * Sets the value of the projectList property.
     * 
     * @param value
     *     allowed object is
     *     {@link ArrayOfProjectDisplayData }
     *     
     */
    public void setProjectList(ArrayOfProjectDisplayData value) {
        this.projectList = value;
    }

}
