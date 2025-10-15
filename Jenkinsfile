// This Jenkinsfile is for a Declarative Pipeline.
pipeline {
    agent any
    
    // =========================================================
    // 1. PLACE THE 'triggers' BLOCK HERE
    //    It goes directly under 'agent' and before 'stages'.
    // =========================================================
    triggers {
        // This will build the job on a nightly schedule (e.g., 3:00 AM)
        cron('H 03 * * *') 
    }

    stages {
        stage('Checkout') {
            steps {
                // 'checkout scm' is correct for Multibranch Pipelines
                checkout scm
            }
        }

        stage('Build') {
            steps {
                script {
                    echo 'Building the application...'
                }
            }
        }

        stage('Test') {
            steps {
                script {
                    echo 'Running tests...'
                }
            }
        }
    }
}
// =========================================================
// IMPORTANT NOTE on WEBHOOKS:
//
// For Multibranch Pipelines, the GitHub webhook trigger (which 
// initiates the SCM *scan*) is configured in the Jenkins job 
// settings, NOT in this Jenkinsfile.
//
// If you want a *regular* Pipeline job to be triggered by a 
// webhook, you would use:
/*
triggers {
    githubPush()
    // OR
    // genericTrigger() 
}
*/
// But for Multibranch jobs, the dedicated "Scan by webhook" 
// setting in the Jenkins UI handles the whole repository scan.
// =========================================================
