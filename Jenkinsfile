pipeline {
    agent any

    environment {
        DOCKERHUB_CREDENTIALS = 'dockerhub' 
        IMAGE_NAME = 'lordofkangs/chat-service' // Your DockerHub repository name
        IMAGE_TAG = 'tagname' // Replace with your desired tag name, or use dynamic values like ${BUILD_NUMBER}
        REGISTRY = 'docker.io' // DockerHub registry
    }

    stages {
        stage('Checkout') {
            steps {
                // Checkout the Git repository
                checkout scm
            }
        }
    }
        stage('Spring APP Build') {
            steps {
                sh './gradlew bootJar'
            }
        }

        stage('Docker Image Build') {
            steps {
               
                sh "docker build -t $IMAGE_NAME:$IMAGE_TAG ."
            }
        }

        stage('Docker Build and Push') {
            steps {
                     {
                    // Login to DockerHub
                    withDockerRegistry([ credentialsId: DOCKERHUB_CREDENTIALS, url: "" ]){
                    sh "docker push $IMAGE_NAME:$IMAGE_TAG"
                    }             
                }
        }
    }

    post {
        always {
            // Logout from DockerHub
            sh "docker logout $REGISTRY"
        }
    }
}
