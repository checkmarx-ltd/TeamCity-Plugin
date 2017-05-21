
package com.checkmarx.v7;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for SourceControlSettings complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="SourceControlSettings"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="Port" type="{http://www.w3.org/2001/XMLSchema}int"/&gt;
 *         &lt;element name="UseSSL" type="{http://www.w3.org/2001/XMLSchema}boolean"/&gt;
 *         &lt;element name="UseSSH" type="{http://www.w3.org/2001/XMLSchema}boolean"/&gt;
 *         &lt;element name="ServerName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="Repository" type="{http://Checkmarx.com/v7}RepositoryType"/&gt;
 *         &lt;element name="UserCredentials" type="{http://Checkmarx.com/v7}Credentials" minOccurs="0"/&gt;
 *         &lt;element name="Protocol" type="{http://Checkmarx.com/v7}SourceControlProtocolType"/&gt;
 *         &lt;element name="RepositoryName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="ProtocolParameters" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="GITBranch" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="GitLsViewType" type="{http://Checkmarx.com/v7}GitLsRemoteViewType"/&gt;
 *         &lt;element name="SSHPublicKey" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="SSHPrivateKey" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="GitHubSettings" type="{http://Checkmarx.com/v7}GitHubIntegrationSettings" minOccurs="0"/&gt;
 *         &lt;element name="PerforceBrowsingMode" type="{http://Checkmarx.com/v7}CxWSPerforceBrowsingMode"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "SourceControlSettings", propOrder = {
    "port",
    "useSSL",
    "useSSH",
    "serverName",
    "repository",
    "userCredentials",
    "protocol",
    "repositoryName",
    "protocolParameters",
    "gitBranch",
    "gitLsViewType",
    "sshPublicKey",
    "sshPrivateKey",
    "gitHubSettings",
    "perforceBrowsingMode"
})
public class SourceControlSettings {

    @XmlElement(name = "Port")
    protected int port;
    @XmlElement(name = "UseSSL")
    protected boolean useSSL;
    @XmlElement(name = "UseSSH")
    protected boolean useSSH;
    @XmlElement(name = "ServerName")
    protected String serverName;
    @XmlElement(name = "Repository", required = true)
    @XmlSchemaType(name = "string")
    protected RepositoryType repository;
    @XmlElement(name = "UserCredentials")
    protected Credentials userCredentials;
    @XmlElement(name = "Protocol", required = true)
    @XmlSchemaType(name = "string")
    protected SourceControlProtocolType protocol;
    @XmlElement(name = "RepositoryName")
    protected String repositoryName;
    @XmlElement(name = "ProtocolParameters")
    protected String protocolParameters;
    @XmlElement(name = "GITBranch")
    protected String gitBranch;
    @XmlElement(name = "GitLsViewType", required = true)
    @XmlSchemaType(name = "string")
    protected GitLsRemoteViewType gitLsViewType;
    @XmlElement(name = "SSHPublicKey")
    protected String sshPublicKey;
    @XmlElement(name = "SSHPrivateKey")
    protected String sshPrivateKey;
    @XmlElement(name = "GitHubSettings")
    protected GitHubIntegrationSettings gitHubSettings;
    @XmlElement(name = "PerforceBrowsingMode", required = true)
    @XmlSchemaType(name = "string")
    protected CxWSPerforceBrowsingMode perforceBrowsingMode;

    /**
     * Gets the value of the port property.
     * 
     */
    public int getPort() {
        return port;
    }

    /**
     * Sets the value of the port property.
     * 
     */
    public void setPort(int value) {
        this.port = value;
    }

    /**
     * Gets the value of the useSSL property.
     * 
     */
    public boolean isUseSSL() {
        return useSSL;
    }

    /**
     * Sets the value of the useSSL property.
     * 
     */
    public void setUseSSL(boolean value) {
        this.useSSL = value;
    }

    /**
     * Gets the value of the useSSH property.
     * 
     */
    public boolean isUseSSH() {
        return useSSH;
    }

    /**
     * Sets the value of the useSSH property.
     * 
     */
    public void setUseSSH(boolean value) {
        this.useSSH = value;
    }

    /**
     * Gets the value of the serverName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getServerName() {
        return serverName;
    }

    /**
     * Sets the value of the serverName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setServerName(String value) {
        this.serverName = value;
    }

    /**
     * Gets the value of the repository property.
     * 
     * @return
     *     possible object is
     *     {@link RepositoryType }
     *     
     */
    public RepositoryType getRepository() {
        return repository;
    }

    /**
     * Sets the value of the repository property.
     * 
     * @param value
     *     allowed object is
     *     {@link RepositoryType }
     *     
     */
    public void setRepository(RepositoryType value) {
        this.repository = value;
    }

    /**
     * Gets the value of the userCredentials property.
     * 
     * @return
     *     possible object is
     *     {@link Credentials }
     *     
     */
    public Credentials getUserCredentials() {
        return userCredentials;
    }

    /**
     * Sets the value of the userCredentials property.
     * 
     * @param value
     *     allowed object is
     *     {@link Credentials }
     *     
     */
    public void setUserCredentials(Credentials value) {
        this.userCredentials = value;
    }

    /**
     * Gets the value of the protocol property.
     * 
     * @return
     *     possible object is
     *     {@link SourceControlProtocolType }
     *     
     */
    public SourceControlProtocolType getProtocol() {
        return protocol;
    }

    /**
     * Sets the value of the protocol property.
     * 
     * @param value
     *     allowed object is
     *     {@link SourceControlProtocolType }
     *     
     */
    public void setProtocol(SourceControlProtocolType value) {
        this.protocol = value;
    }

    /**
     * Gets the value of the repositoryName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRepositoryName() {
        return repositoryName;
    }

    /**
     * Sets the value of the repositoryName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRepositoryName(String value) {
        this.repositoryName = value;
    }

    /**
     * Gets the value of the protocolParameters property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getProtocolParameters() {
        return protocolParameters;
    }

    /**
     * Sets the value of the protocolParameters property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setProtocolParameters(String value) {
        this.protocolParameters = value;
    }

    /**
     * Gets the value of the gitBranch property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getGITBranch() {
        return gitBranch;
    }

    /**
     * Sets the value of the gitBranch property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setGITBranch(String value) {
        this.gitBranch = value;
    }

    /**
     * Gets the value of the gitLsViewType property.
     * 
     * @return
     *     possible object is
     *     {@link GitLsRemoteViewType }
     *     
     */
    public GitLsRemoteViewType getGitLsViewType() {
        return gitLsViewType;
    }

    /**
     * Sets the value of the gitLsViewType property.
     * 
     * @param value
     *     allowed object is
     *     {@link GitLsRemoteViewType }
     *     
     */
    public void setGitLsViewType(GitLsRemoteViewType value) {
        this.gitLsViewType = value;
    }

    /**
     * Gets the value of the sshPublicKey property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSSHPublicKey() {
        return sshPublicKey;
    }

    /**
     * Sets the value of the sshPublicKey property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSSHPublicKey(String value) {
        this.sshPublicKey = value;
    }

    /**
     * Gets the value of the sshPrivateKey property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSSHPrivateKey() {
        return sshPrivateKey;
    }

    /**
     * Sets the value of the sshPrivateKey property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSSHPrivateKey(String value) {
        this.sshPrivateKey = value;
    }

    /**
     * Gets the value of the gitHubSettings property.
     * 
     * @return
     *     possible object is
     *     {@link GitHubIntegrationSettings }
     *     
     */
    public GitHubIntegrationSettings getGitHubSettings() {
        return gitHubSettings;
    }

    /**
     * Sets the value of the gitHubSettings property.
     * 
     * @param value
     *     allowed object is
     *     {@link GitHubIntegrationSettings }
     *     
     */
    public void setGitHubSettings(GitHubIntegrationSettings value) {
        this.gitHubSettings = value;
    }

    /**
     * Gets the value of the perforceBrowsingMode property.
     * 
     * @return
     *     possible object is
     *     {@link CxWSPerforceBrowsingMode }
     *     
     */
    public CxWSPerforceBrowsingMode getPerforceBrowsingMode() {
        return perforceBrowsingMode;
    }

    /**
     * Sets the value of the perforceBrowsingMode property.
     * 
     * @param value
     *     allowed object is
     *     {@link CxWSPerforceBrowsingMode }
     *     
     */
    public void setPerforceBrowsingMode(CxWSPerforceBrowsingMode value) {
        this.perforceBrowsingMode = value;
    }

}
