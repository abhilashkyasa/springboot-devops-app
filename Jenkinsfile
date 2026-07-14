pipeline {
    agent any

    environment {
        IMAGE_NAME = 'springboot-devops-app'
    }

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
                sh 'ls -lh target/springboot-devops-app-0.0.1-SNAPSHOT.jar'
            }
        }

        stage('Docker Build') {
            steps {
                echo 'Building Docker image'
                sh 'docker build -t ${IMAGE_NAME}:${BUILD_NUMBER} .'
            }
        }

        stage('Verify Docker Image') {
            steps {
                echo 'Verifying Docker image'
                sh 'docker images ${IMAGE_NAME}:${BUILD_NUMBER}'
            }
        }
    }

    post {
        success {
            echo 'Maven and Docker build completed successfully'
        }

        failure {
            echo 'Pipeline failed'
        }
    }
}