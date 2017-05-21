
package com.checkmarx.v7;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ArrayOfCxWSProjectCustomField complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ArrayOfCxWSProjectCustomField"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="CxWSProjectCustomField" type="{http://Checkmarx.com/v7}CxWSProjectCustomField" maxOccurs="unbounded" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ArrayOfCxWSProjectCustomField", propOrder = {
    "cxWSProjectCustomField"
})
public class ArrayOfCxWSProjectCustomField {

    @XmlElement(name = "CxWSProjectCustomField", nillable = true)
    protected List<CxWSProjectCustomField> cxWSProjectCustomField;

    /**
     * Gets the value of the cxWSProjectCustomField property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the cxWSProjectCustomField property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getCxWSProjectCustomField().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link CxWSProjectCustomField }
     * 
     * 
     */
    public List<CxWSProjectCustomField> getCxWSProjectCustomField() {
        if (cxWSProjectCustomField == null) {
            cxWSProjectCustomField = new ArrayList<CxWSProjectCustomField>();
        }
        return this.cxWSProjectCustomField;
    }

}
