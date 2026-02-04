pipeline {
	agent any
	
	environment {
		IMAGE_NAME = "mindory0144/total-app"
		IMAGE_TAG = "latest"
		CONTAINER_NAME = "total-app"
	}

	stages {
		stage('Checkout') {
			steps {
				echo 'Git Checkout'
				checkout scm
			}
		}
		
		stage('Gradle Build') {
			steps {
				echo 'Gradle Build'
				sh '''
					chmod +x gradlew
					./gradlew clean build -x test
				   '''
			}
		}
		
		stage('Docker Build') {
			steps {
				echo 'Docker Image Build'
				sh '''
					docker build -t ${IMAGE_NAME}:${IMAGE_TAG} .
				   '''
			}
		}
		
		stage('Docker Run (Local Deploy)') {
			steps {
				echo 'Deploy with Docker run'
				sh '''
					docker stop ${CONTAINER_NAME} || true
					docker rm ${CONTAINER_NAME} || true
					
					docker run -d \
						--name ${CONTAINER_NAME} \
						-p 9090:9090 \
						${IMAGE_NAME}:${IMAGE_TAG}
				   '''
			}
		}
	}
	
	post {
		success {
			echo 'CI/CD 실행 성공'
		}
		failure {
			echo 'CI/CD 실행 실패'
		}
	}
}
