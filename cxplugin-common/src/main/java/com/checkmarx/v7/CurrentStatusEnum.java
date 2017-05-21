
package com.checkmarx.v7;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for CurrentStatusEnum.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="CurrentStatusEnum"&gt;
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *     &lt;enumeration value="Queued"/&gt;
 *     &lt;enumeration value="Working"/&gt;
 *     &lt;enumeration value="Finished"/&gt;
 *     &lt;enumeration value="Failed"/&gt;
 *     &lt;enumeration value="Canceled"/&gt;
 *     &lt;enumeration value="Deleted"/&gt;
 *     &lt;enumeration value="Unknown"/&gt;
 *     &lt;enumeration value="Unzipping"/&gt;
 *     &lt;enumeration value="WaitingToProcess"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 * 
 */
@XmlType(name = "CurrentStatusEnum")
@XmlEnum
public enum CurrentStatusEnum {

    @XmlEnumValue("Queued")
    QUEUED("Queued"),
    @XmlEnumValue("Working")
    WORKING("Working"),
    @XmlEnumValue("Finished")
    FINISHED("Finished"),
    @XmlEnumValue("Failed")
    FAILED("Failed"),
    @XmlEnumValue("Canceled")
    CANCELED("Canceled"),
    @XmlEnumValue("Deleted")
    DELETED("Deleted"),
    @XmlEnumValue("Unknown")
    UNKNOWN("Unknown"),
    @XmlEnumValue("Unzipping")
    UNZIPPING("Unzipping"),
    @XmlEnumValue("WaitingToProcess")
    WAITING_TO_PROCESS("WaitingToProcess");
    private final String value;

    CurrentStatusEnum(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static CurrentStatusEnum fromValue(String v) {
        for (CurrentStatusEnum c: CurrentStatusEnum.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
