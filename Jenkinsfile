pipeline {
    agent any

    environment {
        IMAGE_NAME = 'springboot-devops-app'
        S3_BUCKET = 'abhilash-springboot-devops-artifacts-2026'
        DEPLOY_SERVER = '172.31.39.88'
    }

    stages {

        stage('Checkout') {
            steps {
                echo 'Checking out source code from GitHub'
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
                echo 'Verifying generated JAR'
                sh 'ls -lh target/springboot-devops-app-0.0.1-SNAPSHOT.jar'
            }
        }

        stage('Docker Build') {
            steps {
                echo 'Building Docker image'
                sh '''
                    docker build \
                    -t ${IMAGE_NAME}:${BUILD_NUMBER} \
                    .
                '''
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

        stage('Verify S3 Artifact') {
            steps {
                echo 'Verifying artifact in S3'
                sh '''
                    aws s3 ls \
                    s3://${S3_BUCKET}/docker-images/${IMAGE_NAME}-${BUILD_NUMBER}.tar
                '''
            }
        }

        stage('Deploy to AWS EC2') {
            steps {
                echo 'Deploying Spring Boot application to AWS EC2'

                sshagent(credentials: ['deployment-ec2-key']) {

                    sh '''
                        ssh \
                        -o StrictHostKeyChecking=no \
                        ec2-user@${DEPLOY_SERVER} "

                            echo 'Downloading Docker artifact from S3'

                            aws s3 cp \
                            s3://${S3_BUCKET}/docker-images/${IMAGE_NAME}-${BUILD_NUMBER}.tar \
                            /tmp/${IMAGE_NAME}-${BUILD_NUMBER}.tar


                            echo 'Loading Docker image'

                            sudo docker load \
                            -i /tmp/${IMAGE_NAME}-${BUILD_NUMBER}.tar


                            echo 'Stopping existing container'

                            sudo docker stop ${IMAGE_NAME} || true


                            echo 'Removing existing container'

                            sudo docker rm ${IMAGE_NAME} || true


                            echo 'Starting new Spring Boot container'

                            sudo docker run \
                            -d \
                            --name ${IMAGE_NAME} \
                            --restart unless-stopped \
                            -p 8080:8080 \
                            ${IMAGE_NAME}:${BUILD_NUMBER}


                            echo 'Removing temporary artifact'

                            rm -f \
                            /tmp/${IMAGE_NAME}-${BUILD_NUMBER}.tar


                            echo 'Deployment completed'
                        "
                    '''
                }
            }
        }

        stage('Verify Deployment') {
            steps {
                echo 'Verifying deployed Spring Boot application'

                sh '''
                    sleep 10

                    curl \
                    --fail \
                    --show-error \
                    http://${DEPLOY_SERVER}:8080/api/hello
                '''
            }
        }

    }

    post {

        success {
            echo 'FULL CI/CD PIPELINE COMPLETED SUCCESSFULLY'
        }

        failure {
            echo 'CI/CD PIPELINE FAILED'
        }

        always {
            echo 'Cleaning Jenkins temporary Docker artifact'

            sh '''
                rm -f \
                ${IMAGE_NAME}-${BUILD_NUMBER}.tar
            '''
        }

    }
}