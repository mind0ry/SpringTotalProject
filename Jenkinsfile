pipeline {
	agent any
	
	// 전역 변수 => ${}
	environment {
			SERVER_IP = "3.39.232.170"
			SERVER_USER = "ubuntu"
			APP_DIR = "~/app"
			JAR_NAME = "SpringTotalProject-0.0.1-SNAPSHOT.war"
		}
		
		stages {
		/*stage('Check Git Info') {
			steps {
				sh '''
					echo "===Git Info==="
					git branch
					git log -1
				   '''
			}
		}*/
		
			// 감지 = main : push (commit)
			stage('Check Out') {
				steps {
					echo 'Git Checkout'
					checkout scm
				}
			}
			
			stage('Gradle Permission') {
				steps {
					sh '''
						chmod +x gradlew
					   '''
				}
			}
			
			// build 시작
			stage('Gradle Build') {
				steps {
					sh '''
						./gradlew clean build
					   '''
				}
			}
			
			// war파일 전송 = rsync / scp
			stage('Deploy = rsync') {
				steps {
					sshagent(credentials:['SERVER_SSH_KEY']) {
						sh """
							rsync -avz -e "ssh -o StrictHostKeyChecking=no build/libs/*.war ${SERVER_USER}@${SERVER_IP}:${APP_DIR}
						   """
					}
				}
			}
			
			stage('Run Application') {
				steps {
					sshagent(credentials:['SERVER_SSH_KEY']) {
						sh """
							ssh -o StrictHostKeyChecking=no ${SERVER_USER}@${SERVER_IP} << 'EOF'
								pkill -f 'java -jar'
								nohup java -jar ${APP_DIR}/${JAR_NAME} > log.txt 2>&1 &
EOF
						   """
					}
				}
			}
		}
	}
}