
package com.checkmarx.v7;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for CxWSReportType.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="CxWSReportType"&gt;
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *     &lt;enumeration value="PDF"/&gt;
 *     &lt;enumeration value="RTF"/&gt;
 *     &lt;enumeration value="CSV"/&gt;
 *     &lt;enumeration value="XML"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 * 
 */
@XmlType(name = "CxWSReportType")
@XmlEnum
public enum CxWSReportType {

    PDF,
    RTF,
    CSV,
    XML;

    public String value() {
        return name();
    }

    public static CxWSReportType fromValue(String v) {
        return valueOf(v);
    }

}
