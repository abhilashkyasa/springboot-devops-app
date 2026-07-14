pipeline {
    agent any

    stages {
        stage('Checkout') {
            steps {
                echo 'Checking out Spring Boot source code from GitHub'
                checkout scm
            }
        }

        stage('Maven Build') {
            steps {
                echo 'Building Spring Boot application using Maven'
                sh 'mvn clean package'
            }
        }

        stage('Verify JAR') {
            steps {
                echo 'Verifying generated Spring Boot JAR'
                sh 'ls -lh target/*.jar'
            }
        }
    }

    post {
        success {
            echo 'Spring Boot Maven build completed successfully'
        }

        failure {
            echo 'Spring Boot Maven build failed'
        }
    }
}