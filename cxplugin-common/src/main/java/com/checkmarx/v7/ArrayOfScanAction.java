
package com.checkmarx.v7;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ArrayOfScanAction complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ArrayOfScanAction"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="ScanAction" type="{http://Checkmarx.com/v7}ScanAction" maxOccurs="unbounded" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ArrayOfScanAction", propOrder = {
    "scanAction"
})
public class ArrayOfScanAction {

    @XmlElement(name = "ScanAction", nillable = true)
    protected List<ScanAction> scanAction;

    /**
     * Gets the value of the scanAction property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the scanAction property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getScanAction().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link ScanAction }
     * 
     * 
     */
    public List<ScanAction> getScanAction() {
        if (scanAction == null) {
            scanAction = new ArrayList<ScanAction>();
        }
        return this.scanAction;
    }

}
