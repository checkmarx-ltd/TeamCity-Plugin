package com.checkmarx.teamcity.common.client.dto;

/**
 * Created by: Dorg.
 * Date: 20/09/2016.
 */
public enum ClientOrigin {
    NONE("None"),
    WEB_PORTAL("WebPortal"),
    CLI("CLI"),
    ECLIPSE("Eclipse"),
    VS("VS"),
    INTELI_J("InteliJ"),
    AUDIT("Audit"),
    SDK("SDK"),
    JENKINS("Jenkins"),
    MAVEN("Maven"),
    TFS_BUILD("TFSBuild"),
    TEAMCITY("TeamCity"),
    IMPORTER("Importer"),
    BAMBOO("Bamboo"),
    OTHER("Other");
    private final String value;

    ClientOrigin(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static ClientOrigin fromValue(String v) {
        for (ClientOrigin c: ClientOrigin.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }


}
