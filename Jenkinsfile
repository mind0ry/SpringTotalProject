pipeline {
    agent any

    environment {
        DOCKERHUB_USER = credentials('DOCKERHUB_USERNAME')
        DOCKERHUB_PASS = credentials('DOCKERHUB_PASSWORD')

        SERVER_IP   = '3.39.232.170'
        SERVER_USER = 'ubuntu'

        IMAGE_NAME  = 'mindory0144/total-app'
        CONTAINER   = 'total-app'
        PORT        = '9090'
    }

    stages {

        stage('Checkout') {
            steps {
				echo 'Git Checkout'
                checkout scm
            }
        }
		// 권한 부여
        stage('Grant Gradle Permission') {
            steps {
                sh 'chmod +x gradlew'
            }
        }
		// gradlew build
        stage('Build War') {
            steps {
                sh './gradlew clean build'
            }
        }

        stage('Docker Login') {
            steps {
                sh '''
                echo $DOCKERHUB_PASS | docker login -u $DOCKERHUB_USER --password-stdin
                '''
            }
        }

        stage('Build & Push Docker Image') {
            steps {
                sh '''
                docker build -t $DOCKERHUB_USER/${IMAGE_NAME}:latest .
                docker push $DOCKERHUB_USER/${IMAGE_NAME}:latest
                '''
            }
        }

        stage('Deploy to Ubuntu Server') {
            steps {
                sshagent(credentials: ['SERVER_SSH_KEY']) {
                    sh """
                    ssh -o StrictHostKeyChecking=no ${SERVER_USER}@${SERVER_IP} << EOF
                        docker stop ${CONTAINER} || true
                        docker rm ${CONTAINER} || true
                        docker pull $DOCKERHUB_USER/${IMAGE_NAME}:latest
                        docker run -d --name ${CONTAINER} -p ${PORT}:${PORT} \
                          $DOCKERHUB_USER/${IMAGE_NAME}:latest
EOF
                    """
                }
            }
        }
    }

    post {
        success {
            echo 'Docker Deploy Success'
        }
        failure {
            echo 'Docker Deploy Failed'
        }
    }
}
