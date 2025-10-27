pipeline {
    agent any
    
    // Environment variables
    environment {
        // These will be populated by the 'checkout' step using the Git CLI
        COMMIT_AUTHOR = '' 
        COMMIT_EMAIL = ''
        // Temporarily set the Project ID to "MISSING"
        STARCSEC_PROJECT_ID = 'MISSING_REQUIRED_ID' 
    }

    stages {
        stage('Checkout Code and Get Details') {
            steps {
                script {
                    // 1. Checkout the code to expose SCM details
                    checkout scm
                    
                    // 2. Extract Author details dynamically
                    env.COMMIT_AUTHOR = sh(
                        script: "git log -1 --pretty=format:'%an'", // Gets Author Name
                        returnStdout: true
                    ).trim()
                    
                    env.COMMIT_EMAIL = sh(
                        script: "git log -1 --pretty=format:'%ae'", // Gets Author Email
                        returnStdout: true
                    ).trim()
                    
                    echo "Extracted Author: ${env.COMMIT_AUTHOR} (${env.COMMIT_EMAIL})"
                }
            }
        }

        stage('Build') {
            steps {
                echo 'Building the application...'
            }
        }

        stage('Send StarcSec Webhook') {
            steps {
                script {
                    def webhookUrl = 'https://api.starcsec.com/pipeline/v1/jenkins/webhook'
                    
                    // Construct the full payload with the extracted details
                    def payload = [
                        "status": "FAILURE", // Assuming failure for now, adjust as needed
                        "message": "Build FAILURE (Missing Project ID)", 
                        "build_url": env.BUILD_URL,
                        "repo_url": env.GIT_URL ?: "Unknown",
                        "branch": env.BRANCH_NAME ?: "main",
                        "commit": env.GIT_COMMIT ?: "Unknown",
                        "pr_url": "N/A",
                        // FIX: Author details are now correctly injected
                        "pr_author": "${env.COMMIT_AUTHOR} <${env.COMMIT_EMAIL}>", 
                        "job_name": env.JOB_NAME, 
                        "build_number": env.BUILD_NUMBER, 
                        "action_type": "Branch Commit", 
                        "source_branch": env.BRANCH_NAME ?: "main", 
                        "target_branch": "", 
                        "jenkins_url": env.JENKINS_URL,
                        // This MUST be updated later to fix the 'skipped' error
                        "starcSecProjectId": env.STARCSEC_PROJECT_ID 
                    ]

                    // Send the custom payload (Requires Pipeline Utility Steps Plugin)
                    httpRequest (
                        url: webhookUrl,
                        httpMode: 'POST',
                        contentType: 'APPLICATION_JSON',
                        requestBody: groovy.json.JsonOutput.toJson(payload)
                    )
                    echo "Custom Webhook Sent. Awaiting Project ID update."
                }
            }
        }
    }
}
