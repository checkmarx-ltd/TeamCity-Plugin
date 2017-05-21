
package com.checkmarx.v7;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ArrayOfCxWSEnableCRUDAction complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ArrayOfCxWSEnableCRUDAction"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="CxWSEnableCRUDAction" type="{http://Checkmarx.com/v7}CxWSEnableCRUDAction" maxOccurs="unbounded" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ArrayOfCxWSEnableCRUDAction", propOrder = {
    "cxWSEnableCRUDAction"
})
public class ArrayOfCxWSEnableCRUDAction {

    @XmlElement(name = "CxWSEnableCRUDAction", nillable = true)
    protected List<CxWSEnableCRUDAction> cxWSEnableCRUDAction;

    /**
     * Gets the value of the cxWSEnableCRUDAction property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the cxWSEnableCRUDAction property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getCxWSEnableCRUDAction().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link CxWSEnableCRUDAction }
     * 
     * 
     */
    public List<CxWSEnableCRUDAction> getCxWSEnableCRUDAction() {
        if (cxWSEnableCRUDAction == null) {
            cxWSEnableCRUDAction = new ArrayList<CxWSEnableCRUDAction>();
        }
        return this.cxWSEnableCRUDAction;
    }

}
