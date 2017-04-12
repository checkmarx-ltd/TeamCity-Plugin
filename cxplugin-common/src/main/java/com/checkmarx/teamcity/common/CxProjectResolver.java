package com.checkmarx.teamcity.common;

import com.checkmarx.CxJenkinsWebService.*;
import jetbrains.buildServer.agent.BuildProgressLogger;

import java.util.LinkedList;
import java.util.List;


/**
 * CxProjectContract is copied from the Jenkins plugin
 */

public class CxProjectResolver {
    final private CxWebService cxWebService;

    private BuildProgressLogger logger;

    public CxProjectResolver(final CxWebService cxWebService, BuildProgressLogger logger){
        this.cxWebService = cxWebService;
        this.logger = logger;
    }

    public long resolveProjectId(String projectName, String groupId) throws CxAbortException {

        List<ProjectDisplayData> projects = cxWebService.getProjectsDisplayData();

        final List<Group> groups = cxWebService.getAssociatedGroups();
        final List<Group> selected = new LinkedList<>();

        for (Group group : groups){
            if(group.getID().equals(groupId)){
                selected.add(group);
            }
        }

        if (selected.isEmpty()) {
            final String message = "Could not translate group (team) id: " + groupId + " to group name\n"
                    + "Open the Checkmarx build configuration page, and select a team.\n";
            throw new CxAbortException(message);

        } else if (selected.size() > 1) {
            logger.error("Server returned more than one group with id: " + groupId);
            for (Group g : selected) {
                logger.error("Group Id: " + g.getID() + " groupName: " + g.getGroupName());
            }
        }

        long projectId = 0;
        if (projects != null) {
            for (ProjectDisplayData projectDisplayData : projects) {
                if (projectDisplayData.getProjectName().equalsIgnoreCase(projectName)
                        && projectDisplayData.getGroup().equals(selected.get(0).getGroupName())) {
                    projectId = projectDisplayData.getProjectID();
                    break;
                }
            }
        }

        return projectId;
    }
}
