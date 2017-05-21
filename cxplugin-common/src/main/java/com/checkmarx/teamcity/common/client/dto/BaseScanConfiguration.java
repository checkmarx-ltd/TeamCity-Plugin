package com.checkmarx.teamcity.common.client.dto;

/**
 * Created by: Dorg.
 * Date: 22/08/2016.
 */
public class BaseScanConfiguration {


    private ClientOrigin clientOrigin;

    private String projectName;

    private long projectId;

    private String fullTeamPath;

    private String groupId;

    private String preset;

    private long presetId;

    private boolean isIncrementalScan;

    private String folderExclusions;

    private String fileExclusions;

    private long scanConfigurationId;
    private String description;
    private String owner;
    private boolean isPublic;
    private boolean isPrivateScan;
    private String comment;
    private boolean ignoreScanWithUnchangedCode;

    private boolean failPresetNotFound = false;

    private boolean failTeamNotFound = false;

    public ClientOrigin getClientOrigin() {
        return clientOrigin;
    }

    public void setClientOrigin(ClientOrigin clientOrigin) {
        this.clientOrigin = clientOrigin;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getFullTeamPath() {
        return fullTeamPath;
    }

    public void setFullTeamPath(String fullTeamPath) {
        this.fullTeamPath = fullTeamPath;
    }

    public String getPreset() {
        return preset;
    }

    public void setPreset(String preset) {
        this.preset = preset;
    }

    public boolean isIncrementalScan() {
        return isIncrementalScan;
    }

    public void setIncrementalScan(boolean incrementalScan) {
        isIncrementalScan = incrementalScan;
    }

    public String getFolderExclusions() {
        return folderExclusions;
    }

    public void setFolderExclusions(String folderExclusions) {
        this.folderExclusions = folderExclusions;
    }

    public String getFileExclusions() {
        return fileExclusions;
    }

    public void setFileExclusions(String fileExclusions) {
        this.fileExclusions = fileExclusions;
    }

    public long getProjectId() {
        return projectId;
    }

    public void setProjectId(long projectId) {
        this.projectId = projectId;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public long getPresetId() {
        return presetId;
    }

    public void setPresetId(long presetId)
    {
        this.presetId = presetId;
    }

    public long getScanConfigurationId() {
        return scanConfigurationId;
    }

    public void setScanConfigurationId(long scanConfigurationId) {
        this.scanConfigurationId = scanConfigurationId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public boolean isPublic() {
        return isPublic;
    }

    public void setPublic(boolean aPublic) {
        isPublic = aPublic;
    }

    public boolean isPrivateScan() {
        return isPrivateScan;
    }

    public void setPrivateScan(boolean privateScan) {
        isPrivateScan = privateScan;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public boolean isIgnoreScanWithUnchangedCode() {
        return ignoreScanWithUnchangedCode;
    }

    public void setIgnoreScanWithUnchangedCode(boolean ignoreScanWithUnchangedCode) {
        this.ignoreScanWithUnchangedCode = ignoreScanWithUnchangedCode;
    }

    public boolean isFailPresetNotFound() {
        return failPresetNotFound;
    }

    public void setFailPresetNotFound(boolean failPresetNotFound) {
        this.failPresetNotFound = failPresetNotFound;
    }

    public boolean isFailTeamNotFound() {
        return failTeamNotFound;
    }

    public void setFailTeamNotFound(boolean failTeamNotFound) {
        this.failTeamNotFound = failTeamNotFound;
    }
}
