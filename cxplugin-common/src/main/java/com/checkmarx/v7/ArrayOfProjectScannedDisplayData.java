
package com.checkmarx.v7;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ArrayOfProjectScannedDisplayData complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ArrayOfProjectScannedDisplayData"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="ProjectScannedDisplayData" type="{http://Checkmarx.com/v7}ProjectScannedDisplayData" maxOccurs="unbounded" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ArrayOfProjectScannedDisplayData", propOrder = {
    "projectScannedDisplayData"
})
public class ArrayOfProjectScannedDisplayData {

    @XmlElement(name = "ProjectScannedDisplayData", nillable = true)
    protected List<ProjectScannedDisplayData> projectScannedDisplayData;

    /**
     * Gets the value of the projectScannedDisplayData property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the projectScannedDisplayData property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getProjectScannedDisplayData().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link ProjectScannedDisplayData }
     * 
     * 
     */
    public List<ProjectScannedDisplayData> getProjectScannedDisplayData() {
        if (projectScannedDisplayData == null) {
            projectScannedDisplayData = new ArrayList<ProjectScannedDisplayData>();
        }
        return this.projectScannedDisplayData;
    }

}
