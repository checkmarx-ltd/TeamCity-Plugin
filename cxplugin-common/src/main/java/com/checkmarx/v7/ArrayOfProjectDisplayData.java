
package com.checkmarx.v7;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ArrayOfProjectDisplayData complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ArrayOfProjectDisplayData"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="ProjectDisplayData" type="{http://Checkmarx.com/v7}ProjectDisplayData" maxOccurs="unbounded" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ArrayOfProjectDisplayData", propOrder = {
    "projectDisplayData"
})
public class ArrayOfProjectDisplayData {

    @XmlElement(name = "ProjectDisplayData", nillable = true)
    protected List<ProjectDisplayData> projectDisplayData;

    /**
     * Gets the value of the projectDisplayData property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the projectDisplayData property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getProjectDisplayData().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link ProjectDisplayData }
     * 
     * 
     */
    public List<ProjectDisplayData> getProjectDisplayData() {
        if (projectDisplayData == null) {
            projectDisplayData = new ArrayList<ProjectDisplayData>();
        }
        return this.projectDisplayData;
    }

}
