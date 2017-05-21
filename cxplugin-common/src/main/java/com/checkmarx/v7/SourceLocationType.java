
package com.checkmarx.v7;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for SourceLocationType.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="SourceLocationType"&gt;
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *     &lt;enumeration value="Local"/&gt;
 *     &lt;enumeration value="Shared"/&gt;
 *     &lt;enumeration value="SourceControl"/&gt;
 *     &lt;enumeration value="SourcePulling"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 * 
 */
@XmlType(name = "SourceLocationType")
@XmlEnum
public enum SourceLocationType {

    @XmlEnumValue("Local")
    LOCAL("Local"),
    @XmlEnumValue("Shared")
    SHARED("Shared"),
    @XmlEnumValue("SourceControl")
    SOURCE_CONTROL("SourceControl"),
    @XmlEnumValue("SourcePulling")
    SOURCE_PULLING("SourcePulling");
    private final String value;

    SourceLocationType(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static SourceLocationType fromValue(String v) {
        for (SourceLocationType c: SourceLocationType.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
