
package com.checkmarx.v7;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for GitLsRemoteViewType.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="GitLsRemoteViewType"&gt;
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *     &lt;enumeration value="TAGS"/&gt;
 *     &lt;enumeration value="HEADS"/&gt;
 *     &lt;enumeration value="TAGS_AND_HEADS"/&gt;
 *     &lt;enumeration value="ALL"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 * 
 */
@XmlType(name = "GitLsRemoteViewType")
@XmlEnum
public enum GitLsRemoteViewType {

    TAGS,
    HEADS,
    TAGS_AND_HEADS,
    ALL;

    public String value() {
        return name();
    }

    public static GitLsRemoteViewType fromValue(String v) {
        return valueOf(v);
    }

}
