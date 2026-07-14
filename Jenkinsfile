pipeline {
    agent any

    environment {
        IMAGE_NAME = 'springboot-devops-app'
        S3_BUCKET = 'abhilash-springboot-devops-artifacts-2026'
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

        stage('Docker Save') {
            steps {
                echo 'Saving Docker image as TAR artifact'
                sh '''
                    docker save \
                    -o ${IMAGE_NAME}-${BUILD_NUMBER}.tar \
                    ${IMAGE_NAME}:${BUILD_NUMBER}
                '''
            }
        }

        stage('Verify Docker Artifact') {
            steps {
                echo 'Verifying Docker TAR artifact'
                sh 'ls -lh ${IMAGE_NAME}-${BUILD_NUMBER}.tar'
            }
        }

        stage('Upload Artifact to S3') {
            steps {
                echo 'Uploading Docker artifact to AWS S3'
                sh '''
                    aws s3 cp \
                    ${IMAGE_NAME}-${BUILD_NUMBER}.tar \
                    s3://${S3_BUCKET}/docker-images/${IMAGE_NAME}-${BUILD_NUMBER}.tar
                '''
            }
        }

        stage('Verify S3 Upload') {
            steps {
                echo 'Verifying Docker artifact in AWS S3'
                sh '''
                    aws s3 ls \
                    s3://${S3_BUCKET}/docker-images/${IMAGE_NAME}-${BUILD_NUMBER}.tar
                '''
            }
        }
    }

    post {
        success {
            echo 'Maven, Docker and S3 artifact pipeline completed successfully'
        }

        failure {
            echo 'Pipeline failed'
        }

        always {
            sh '''
                rm -f ${IMAGE_NAME}-${BUILD_NUMBER}.tar
            '''
        }
    }
}