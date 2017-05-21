
package com.checkmarx.v7;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for CxWSResponseUserData complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="CxWSResponseUserData"&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{http://Checkmarx.com/v7}CxWSBasicRepsonse"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="UserDataList" type="{http://Checkmarx.com/v7}ArrayOfUserData" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/extension&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "CxWSResponseUserData", propOrder = {
    "userDataList"
})
public class CxWSResponseUserData
    extends CxWSBasicRepsonse
{

    @XmlElement(name = "UserDataList")
    protected ArrayOfUserData userDataList;

    /**
     * Gets the value of the userDataList property.
     * 
     * @return
     *     possible object is
     *     {@link ArrayOfUserData }
     *     
     */
    public ArrayOfUserData getUserDataList() {
        return userDataList;
    }

    /**
     * Sets the value of the userDataList property.
     * 
     * @param value
     *     allowed object is
     *     {@link ArrayOfUserData }
     *     
     */
    public void setUserDataList(ArrayOfUserData value) {
        this.userDataList = value;
    }

}
