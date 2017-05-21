
package com.checkmarx.v7;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for RepositoryType.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="RepositoryType"&gt;
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *     &lt;enumeration value="TFS"/&gt;
 *     &lt;enumeration value="SVN"/&gt;
 *     &lt;enumeration value="CVS"/&gt;
 *     &lt;enumeration value="GIT"/&gt;
 *     &lt;enumeration value="Perforce"/&gt;
 *     &lt;enumeration value="NONE"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 * 
 */
@XmlType(name = "RepositoryType")
@XmlEnum
public enum RepositoryType {

    TFS("TFS"),
    SVN("SVN"),
    CVS("CVS"),
    GIT("GIT"),
    @XmlEnumValue("Perforce")
    PERFORCE("Perforce"),
    NONE("NONE");
    private final String value;

    RepositoryType(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static RepositoryType fromValue(String v) {
        for (RepositoryType c: RepositoryType.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
