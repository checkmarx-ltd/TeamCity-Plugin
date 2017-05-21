
package com.checkmarx.v7;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ScanType.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="ScanType"&gt;
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *     &lt;enumeration value="UNKNOWN"/&gt;
 *     &lt;enumeration value="ALLSCANS"/&gt;
 *     &lt;enumeration value="REGULAR"/&gt;
 *     &lt;enumeration value="SUBSET"/&gt;
 *     &lt;enumeration value="PARTIAL"/&gt;
 *     &lt;enumeration value="RUNNING"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 * 
 */
@XmlType(name = "ScanType")
@XmlEnum
public enum ScanType {

    UNKNOWN,
    ALLSCANS,
    REGULAR,
    SUBSET,
    PARTIAL,
    RUNNING;

    public String value() {
        return name();
    }

    public static ScanType fromValue(String v) {
        return valueOf(v);
    }

}
