
package com.checkmarx.v7;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ArrayOfCxWSLdapGroupMapping complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ArrayOfCxWSLdapGroupMapping"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="CxWSLdapGroupMapping" type="{http://Checkmarx.com/v7}CxWSLdapGroupMapping" maxOccurs="unbounded" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ArrayOfCxWSLdapGroupMapping", propOrder = {
    "cxWSLdapGroupMapping"
})
public class ArrayOfCxWSLdapGroupMapping {

    @XmlElement(name = "CxWSLdapGroupMapping", nillable = true)
    protected List<CxWSLdapGroupMapping> cxWSLdapGroupMapping;

    /**
     * Gets the value of the cxWSLdapGroupMapping property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the cxWSLdapGroupMapping property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getCxWSLdapGroupMapping().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link CxWSLdapGroupMapping }
     * 
     * 
     */
    public List<CxWSLdapGroupMapping> getCxWSLdapGroupMapping() {
        if (cxWSLdapGroupMapping == null) {
            cxWSLdapGroupMapping = new ArrayList<CxWSLdapGroupMapping>();
        }
        return this.cxWSLdapGroupMapping;
    }

}
