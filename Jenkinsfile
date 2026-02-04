pipeline {
	agent any
	
	environment {
		IMAGE_NAME = "mindory0144/total-app"
		IMAGE_TAG = "latest"
		BLUE_NAME = "total-app-blue"
		GREEN_NAME = "total-app-green"
		BLUE_PORT = "9091"
		GREEN_PORT = "9092"
		NGINX_NAME = "total-nginx"
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
		
		stage('Deploy (Blue/Green)') {
			steps {
				echo 'Deploy with Blue/Green'
				sh '''
					set -e
					
					# 1) 현재 어떤 컨테이너가 살아있는지 확인
					ACTIVE=""
					if docker ps --format '{{.Names}}' | grep -q "^${BLUE_NAME}$"; then
						ACTIVE="blue"
					elif docker ps --format '{{.Names}}' | grep -q "^${GREEN_NAME}$"; then
						ACTIVE="green"
					fi
					
					# 2) 다음에 띄울 색 결정
					if [ "$ACTIVE" = "blue" ]; then
						NEXT_NAME="${GREEN_NAME}"
						NEXT_PORT="${GREEN_PORT}"
						OLD_NAME="${BLUE_NAME}"
					else
						NEXT_NAME="${BLUE_NAME}"
						NEXT_PORT="${BLUE_PORT}"
						OLD_NAME="${GREEN_NAME}"
					fi
					
					echo "ACTIVE=$ACTIVE"
					echo "NEXT_NAME=$NEXT_NAME NEXT_PORT=$NEXT_PORT OLD_NAME=$OLD_NAME"
					
					# 3) 새 버전 컨테이너를 '다른 포트'로 먼저 띄움
					docker rm -f ${NEXT_NAME} || true
					docker run -d \
						--name ${NEXT_NAME} \
						-p ${NEXT_PORT}:9090 \
						${IMAGE_NAME}:${IMAGE_TAG}
					
					# 4) 새 컨테이너 헬스 체크(간단 버전)
					echo "Waiting for new container..."
					for i in $(seq 1 30); do
						if curl -fsS http://localhost:${NEXT_PORT}/ >/dev/null; then
							echo "New container is UP"
							break
						fi
						sleep 2
					done
					
					# 5) nginx가 없다면 무중단 불가 → 여기서 nginx upstream을 바꾸고 reload 해야 함
					if ! docker ps --format '{{.Names}}' | grep -q "^${NGINX_NAME}$"; then
						echo "ERROR: nginx container (${NGINX_NAME}) is not running. Zero-downtime requires nginx."
						exit 1
					fi
					
					# 6) nginx upstream 스위치 (아래 스크립트는 nginx.conf 구조에 따라 달라짐)
					#    여기서는 /etc/nginx/conf.d/upstream.conf 를 바꾼다고 가정
					docker exec ${NGINX_NAME} sh -lc "sed -i 's/server 127.0.0.1:[0-9]*/server 127.0.0.1:${NEXT_PORT}/' /etc/nginx/conf.d/upstream.conf && nginx -s reload"
					
					# 7) 이전 컨테이너 정리(트래픽 전환 후)
					docker rm -f ${OLD_NAME} || true
					
					echo "Switch complete."
				   '''
			}
		}
	}
	
	post {
		success { echo 'CI/CD 실행 성공' }
		failure { echo 'CI/CD 실행 실패' }
	}
}
