
package com.checkmarx.v7;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for CxWSCrudEnum.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="CxWSCrudEnum"&gt;
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *     &lt;enumeration value="Create"/&gt;
 *     &lt;enumeration value="Delete"/&gt;
 *     &lt;enumeration value="Update"/&gt;
 *     &lt;enumeration value="View"/&gt;
 *     &lt;enumeration value="Run"/&gt;
 *     &lt;enumeration value="Investigate"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 * 
 */
@XmlType(name = "CxWSCrudEnum")
@XmlEnum
public enum CxWSCrudEnum {

    @XmlEnumValue("Create")
    CREATE("Create"),
    @XmlEnumValue("Delete")
    DELETE("Delete"),
    @XmlEnumValue("Update")
    UPDATE("Update"),
    @XmlEnumValue("View")
    VIEW("View"),
    @XmlEnumValue("Run")
    RUN("Run"),
    @XmlEnumValue("Investigate")
    INVESTIGATE("Investigate");
    private final String value;

    CxWSCrudEnum(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static CxWSCrudEnum fromValue(String v) {
        for (CxWSCrudEnum c: CxWSCrudEnum.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
