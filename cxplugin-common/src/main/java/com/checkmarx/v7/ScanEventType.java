
package com.checkmarx.v7;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ScanEventType.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="ScanEventType"&gt;
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *     &lt;enumeration value="BeforeScanStarts"/&gt;
 *     &lt;enumeration value="AfterScanSucceeds"/&gt;
 *     &lt;enumeration value="OnScanFailure"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 * 
 */
@XmlType(name = "ScanEventType")
@XmlEnum
public enum ScanEventType {

    @XmlEnumValue("BeforeScanStarts")
    BEFORE_SCAN_STARTS("BeforeScanStarts"),
    @XmlEnumValue("AfterScanSucceeds")
    AFTER_SCAN_SUCCEEDS("AfterScanSucceeds"),
    @XmlEnumValue("OnScanFailure")
    ON_SCAN_FAILURE("OnScanFailure");
    private final String value;

    ScanEventType(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static ScanEventType fromValue(String v) {
        for (ScanEventType c: ScanEventType.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
