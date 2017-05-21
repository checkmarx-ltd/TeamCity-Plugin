
package com.checkmarx.v7;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for SourceControlProtocolType.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="SourceControlProtocolType"&gt;
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *     &lt;enumeration value="WindowsAuthentication"/&gt;
 *     &lt;enumeration value="SSL"/&gt;
 *     &lt;enumeration value="SSH"/&gt;
 *     &lt;enumeration value="PasswordServer"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 * 
 */
@XmlType(name = "SourceControlProtocolType")
@XmlEnum
public enum SourceControlProtocolType {

    @XmlEnumValue("WindowsAuthentication")
    WINDOWS_AUTHENTICATION("WindowsAuthentication"),
    SSL("SSL"),
    SSH("SSH"),
    @XmlEnumValue("PasswordServer")
    PASSWORD_SERVER("PasswordServer");
    private final String value;

    SourceControlProtocolType(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static SourceControlProtocolType fromValue(String v) {
        for (SourceControlProtocolType c: SourceControlProtocolType.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
