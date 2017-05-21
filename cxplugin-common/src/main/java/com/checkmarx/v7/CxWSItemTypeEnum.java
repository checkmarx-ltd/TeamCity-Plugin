
package com.checkmarx.v7;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for CxWSItemTypeEnum.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="CxWSItemTypeEnum"&gt;
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *     &lt;enumeration value="Project"/&gt;
 *     &lt;enumeration value="Task"/&gt;
 *     &lt;enumeration value="Scan"/&gt;
 *     &lt;enumeration value="Preset"/&gt;
 *     &lt;enumeration value="Configuration"/&gt;
 *     &lt;enumeration value="Users"/&gt;
 *     &lt;enumeration value="Roles"/&gt;
 *     &lt;enumeration value="Other"/&gt;
 *     &lt;enumeration value="SystemSettings"/&gt;
 *     &lt;enumeration value="Ignore_Path"/&gt;
 *     &lt;enumeration value="ResultComment"/&gt;
 *     &lt;enumeration value="ResultSeverity"/&gt;
 *     &lt;enumeration value="ResultStatus"/&gt;
 *     &lt;enumeration value="AuditUser"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 * 
 */
@XmlType(name = "CxWSItemTypeEnum")
@XmlEnum
public enum CxWSItemTypeEnum {

    @XmlEnumValue("Project")
    PROJECT("Project"),
    @XmlEnumValue("Task")
    TASK("Task"),
    @XmlEnumValue("Scan")
    SCAN("Scan"),
    @XmlEnumValue("Preset")
    PRESET("Preset"),
    @XmlEnumValue("Configuration")
    CONFIGURATION("Configuration"),
    @XmlEnumValue("Users")
    USERS("Users"),
    @XmlEnumValue("Roles")
    ROLES("Roles"),
    @XmlEnumValue("Other")
    OTHER("Other"),
    @XmlEnumValue("SystemSettings")
    SYSTEM_SETTINGS("SystemSettings"),
    @XmlEnumValue("Ignore_Path")
    IGNORE_PATH("Ignore_Path"),
    @XmlEnumValue("ResultComment")
    RESULT_COMMENT("ResultComment"),
    @XmlEnumValue("ResultSeverity")
    RESULT_SEVERITY("ResultSeverity"),
    @XmlEnumValue("ResultStatus")
    RESULT_STATUS("ResultStatus"),
    @XmlEnumValue("AuditUser")
    AUDIT_USER("AuditUser");
    private final String value;

    CxWSItemTypeEnum(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static CxWSItemTypeEnum fromValue(String v) {
        for (CxWSItemTypeEnum c: CxWSItemTypeEnum.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
