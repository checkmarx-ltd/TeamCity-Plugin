
package com.checkmarx.v7;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ScanActionType.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="ScanActionType"&gt;
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *     &lt;enumeration value="EmailNotification"/&gt;
 *     &lt;enumeration value="PostScanAction"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 * 
 */
@XmlType(name = "ScanActionType")
@XmlEnum
public enum ScanActionType {

    @XmlEnumValue("EmailNotification")
    EMAIL_NOTIFICATION("EmailNotification"),
    @XmlEnumValue("PostScanAction")
    POST_SCAN_ACTION("PostScanAction");
    private final String value;

    ScanActionType(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static ScanActionType fromValue(String v) {
        for (ScanActionType c: ScanActionType.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
