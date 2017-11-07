pipeline {
    
    agent {
        node { label 'Plugins' }
    }
	
	tools { 
        maven 'mvn_3.3.3_windows' 
        jdk 'JDK_WINDOWS_1.8.0_92' 
    }
	
	parameters
	{
		booleanParam (
            defaultValue: false,
            description: '',
            name : 'IsReleaseBuild')
	}
		
    stages {
	
		stage('Pipeline Info') {
            steps {
                echo bat(returnStdout: true, script: 'set')
            }
        }
		
        stage('Remove Snapshot') {
		    when {
                expression {
                    return params.IsReleaseBuild 
                }
            }
            steps {
                echo " ----------------------------------------------------- "
				echo "|  SNAPSHOT DISABLED: Removing Snapshot Before Build  |"
				echo " ----------------------------------------------------- "
				
				script {
					workspacePath = pwd()
                }
				
				dir("$workspacePath") {
					powershell '''		If(Test-Path pom.xml)
					{  
						[xml]$XmlDocument = Get-Content -Path pom.xml
						$XmlDocument.project.version = $XmlDocument.project.version.Replace("-SNAPSHOT", "")
						$XmlDocument.Save("$pwd\\pom.xml")
					}'''
				}
				
				dir("$workspacePath\\build") {
				    powershell '''		If(Test-Path pom.xml)
					{  
						[xml]$XmlDocument = Get-Content -Path pom.xml
						$XmlDocument.project.version = $XmlDocument.project.version.Replace("-SNAPSHOT", "")
						$XmlDocument.Save("$pwd\\pom.xml")
					}'''
				}
				
				dir("$workspacePath\\cxplugin-agent") {
					powershell '''		If(Test-Path pom.xml)
					{  
						[xml]$XmlDocument = Get-Content -Path pom.xml
						$XmlDocument.project.version = $XmlDocument.project.version.Replace("-SNAPSHOT", "")
						$XmlDocument.Save("$pwd\\pom.xml")
					}'''
				}	

				dir("$workspacePath\\cxplugin-common") {
				    powershell '''		If(Test-Path pom.xml)
					{  
						[xml]$XmlDocument = Get-Content -Path pom.xml
						$XmlDocument.project.version = $XmlDocument.project.version.Replace("-SNAPSHOT", "")
						$XmlDocument.Save("$pwd\\pom.xml")
					}'''
				}
				
				dir("$workspacePath\\cxplugin-server") {
				    powershell '''		If(Test-Path pom.xml)
					{  
						[xml]$XmlDocument = Get-Content -Path pom.xml
						$XmlDocument.project.version = $XmlDocument.project.version.Replace("-SNAPSHOT", "")
						$XmlDocument.Save("$pwd\\pom.xml")
					}'''
				}
			}		
		}
		
		stage('Build') {
            steps{
                bat "mvn clean install -Dbuild.number=${BUILD_NUMBER}"
            }
        }
		
		stage('Apply Artifact Version') {
            steps{
                script {
					workspacePath = pwd()
					writeFile file: "$workspacePath\\buildNumber.txt", text: "${env.BUILD_NUMBER}"
                }
                
                dir("$workspacePath") {
                    
					powershell ''' If(Test-Path pom.xml)
					{ 
						[xml]$XmlDocument = Get-Content -Path pom.xml
                        $version = $XmlDocument.project.version.Split(\'-\')[0]
                        $fileName = Get-ChildItem target\\*.zip | Split-Path -Leaf
                        $buildNumber = Get-Content buildNumber.txt
                        $newName = $fileName.Split('.')[0] + "-$version" + "." + "$buildNumber" + ".zip"
                        Get-ChildItem target\\*.zip | Rename-Item -NewName {$newName}
					}'''
				}
            }
        }
		
		stage('Upload To Artifactory') {
            steps {
                script {
                    def server = Artifactory.server "-484709638@1439224648400"
                    def buildInfo = Artifactory.newBuildInfo()
                    buildInfo.env.capture = true
                    buildInfo.env.collect()
                    def uploadSpec = ""
                    
                    if("${params.IsReleaseBuild}" == "true") {
                        uploadSpec = """{
                        "files": [
                        {
                        "pattern": "target/*.zip",
                        "target": "plugins-release-local/com/checkmarx/teamcity/"
                        }
                        ]
                        }"""
                    } else {
                        uploadSpec = """{
                        "files": [
                        {
                        "pattern": "target/*.zip",
                        "target": "plugins-snapshot-local/com/checkmarx/teamcity/"
                        }
                        ]
                        }"""
                    }
                    
                    server.upload spec: uploadSpec, buildInfo: buildInfo
                   
				}   
			}
        }
	}
		
	
    post { 
        success { 
            cleanWs()
        }
        failure { 
			emailext body: """
            <b>TeamCity Failed Pipeline</b><br>
            <B>Build URL: </b> ${BUILD_URL} <br>
            <br>
            <p>Please check and fix.</p>
            <br>
            <hr />
            <p>Thanks,</p>            
			<p>DevOps Team</p>
			""", subject: "Jenkins Job TeamCity Plugin - Failed", to: "Rona.Hirsch@checkmarx.com"         
			
			cleanWs()
        }
    }
}
