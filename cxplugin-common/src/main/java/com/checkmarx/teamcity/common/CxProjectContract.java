package com.checkmarx.teamcity.common;

import com.checkmarx.CxJenkinsWebService.*;


/**
 * CxProjectContract is copied from the Jenkins plugin
 */

public class CxProjectContract {
    final private CxWebService cxWebService;

    public CxProjectContract(final CxWebService cxWebService){
        this.cxWebService = cxWebService;
    }

    public boolean newProject(final String projectName, final String groupId){
        final CxWSBasicRepsonse validateProjectResponse = this.cxWebService.validateProjectName(projectName, groupId);
        return validateProjectResponse.isIsSuccesfull();
    }
}
