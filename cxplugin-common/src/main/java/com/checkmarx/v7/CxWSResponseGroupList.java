
package com.checkmarx.v7;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for CxWSResponseGroupList complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="CxWSResponseGroupList"&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{http://Checkmarx.com/v7}CxWSBasicRepsonse"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="GroupList" type="{http://Checkmarx.com/v7}ArrayOfGroup" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/extension&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "CxWSResponseGroupList", propOrder = {
    "groupList"
})
public class CxWSResponseGroupList
    extends CxWSBasicRepsonse
{

    @XmlElement(name = "GroupList")
    protected ArrayOfGroup groupList;

    /**
     * Gets the value of the groupList property.
     * 
     * @return
     *     possible object is
     *     {@link ArrayOfGroup }
     *     
     */
    public ArrayOfGroup getGroupList() {
        return groupList;
    }

    /**
     * Sets the value of the groupList property.
     * 
     * @param value
     *     allowed object is
     *     {@link ArrayOfGroup }
     *     
     */
    public void setGroupList(ArrayOfGroup value) {
        this.groupList = value;
    }

}
