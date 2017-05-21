
package com.checkmarx.v7;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for CxClientType.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="CxClientType"&gt;
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *     &lt;enumeration value="None"/&gt;
 *     &lt;enumeration value="WebPortal"/&gt;
 *     &lt;enumeration value="CLI"/&gt;
 *     &lt;enumeration value="Eclipse"/&gt;
 *     &lt;enumeration value="VS"/&gt;
 *     &lt;enumeration value="InteliJ"/&gt;
 *     &lt;enumeration value="Audit"/&gt;
 *     &lt;enumeration value="SDK"/&gt;
 *     &lt;enumeration value="Jenkins"/&gt;
 *     &lt;enumeration value="TFSBuild"/&gt;
 *     &lt;enumeration value="Importer"/&gt;
 *     &lt;enumeration value="Other"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 * 
 */
@XmlType(name = "CxClientType")
@XmlEnum
public enum CxClientType {

    @XmlEnumValue("None")
    NONE("None"),
    @XmlEnumValue("WebPortal")
    WEB_PORTAL("WebPortal"),
    CLI("CLI"),
    @XmlEnumValue("Eclipse")
    ECLIPSE("Eclipse"),
    VS("VS"),
    @XmlEnumValue("InteliJ")
    INTELI_J("InteliJ"),
    @XmlEnumValue("Audit")
    AUDIT("Audit"),
    SDK("SDK"),
    @XmlEnumValue("Jenkins")
    JENKINS("Jenkins"),
    @XmlEnumValue("TFSBuild")
    TFS_BUILD("TFSBuild"),
    @XmlEnumValue("Importer")
    IMPORTER("Importer"),
    @XmlEnumValue("Other")
    OTHER("Other");
    private final String value;

    CxClientType(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static CxClientType fromValue(String v) {
        for (CxClientType c: CxClientType.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
