pipeline {
    agent any
    
    // Set the static Project ID and dynamic environment variables
    environment {
        STARCSEC_PROJECT_ID = 'DEMO%20ORG-ASSET-125927-543' // <-- THE CRITICAL FIX
        COMMIT_AUTHOR = '' 
        COMMIT_EMAIL = ''
    }

    stages {
        stage('Checkout Code and Get Details') {
            steps {
                script {
                    // 1. Checkout the code to expose SCM details
                    checkout scm
                    
                    // 2. Extract Author details dynamically using Git CLI
                    env.COMMIT_AUTHOR = sh(
                        script: "git log -1 --pretty=format:'%an'", // Gets Author Name
                        returnStdout: true
                    ).trim()
                    
                    env.COMMIT_EMAIL = sh(
                        script: "git log -1 --pretty=format:'%ae'", // Gets Author Email
                        returnStdout: true
                    ).trim()
                    
                    echo "Extracted Author: ${env.COMMIT_AUTHOR} (${env.COMMIT_EMAIL})"
                    echo "StarcSec Project ID: ${env.STARCSEC_PROJECT_ID}"
                }
            }
        }

        stage('Build') {
            steps {
                echo 'Building the application...'
                // Insert your actual build commands here
            }
        }

        stage('Send StarcSec Webhook') {
            steps {
                script {
                    // NOTE: Ensure you have the 'Pipeline Utility Steps Plugin' installed
                    def webhookUrl = 'https://api.starcsec.com/pipeline/v1/jenkins/webhook'
                    
                    def payload = [
                        "status": currentBuild.currentResult ?: "SUCCESS", // Use the actual build result
                        "message": "Jenkins build complete for job ${env.JOB_NAME}", 
                        "build_url": env.BUILD_URL,
                        "repo_url": env.GIT_URL ?: "Unknown",
                        "branch": env.BRANCH_NAME ?: "main",
                        "commit": env.GIT_COMMIT ?: "Unknown",
                        "pr_url": "N/A",
                        // Dynamic Author details
                        "pr_author": "${env.COMMIT_AUTHOR} <${env.COMMIT_EMAIL}>", 
                        "job_name": env.JOB_NAME, 
                        "build_number": env.BUILD_NUMBER, 
                        "action_type": "Branch Commit", 
                        "source_branch": env.BRANCH_NAME ?: "main", 
                        "target_branch": "", 
                        "jenkins_url": env.JENKINS_URL,
                        // FIX: Including the Project ID
                        "starcSecProjectId": env.STARCSEC_PROJECT_ID 
                    ]

                    // Send the custom payload
                    httpRequest (
                        url: webhookUrl,
                        httpMode: 'POST',
                        contentType: 'APPLICATION_JSON',
                        requestBody: groovy.json.JsonOutput.toJson(payload)
                    )
                    echo "Custom Webhook Sent successfully with Project ID."
                }
            }
        }
        
        stage('Test') {
            steps {
                echo 'Running tests...'
            }
        }
    }
}
