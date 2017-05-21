
package com.checkmarx.v7;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ArrayOfCxWSItemAndCRUD complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ArrayOfCxWSItemAndCRUD"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="CxWSItemAndCRUD" type="{http://Checkmarx.com/v7}CxWSItemAndCRUD" maxOccurs="unbounded" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ArrayOfCxWSItemAndCRUD", propOrder = {
    "cxWSItemAndCRUD"
})
public class ArrayOfCxWSItemAndCRUD {

    @XmlElement(name = "CxWSItemAndCRUD", nillable = true)
    protected List<CxWSItemAndCRUD> cxWSItemAndCRUD;

    /**
     * Gets the value of the cxWSItemAndCRUD property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the cxWSItemAndCRUD property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getCxWSItemAndCRUD().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link CxWSItemAndCRUD }
     * 
     * 
     */
    public List<CxWSItemAndCRUD> getCxWSItemAndCRUD() {
        if (cxWSItemAndCRUD == null) {
            cxWSItemAndCRUD = new ArrayList<CxWSItemAndCRUD>();
        }
        return this.cxWSItemAndCRUD;
    }

}
