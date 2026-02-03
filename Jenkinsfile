pipeline {
	agent any
	
	// 전역변수 => ${SERVER_IP}
	environment {
			APP_DIR = "~/app"
			JAR_NAME = "SpringTotalProject-0.0.1-SNAPSHOT.war"
	}
		
	stages {
		/*
			git push = commit
			    |
			web hooks / poll
			    |
			 jenkins (local)
			    |
			  build
			    |
			  docker build
			  docker push
			    |
			  minikube
			    | deployment.yaml update
			  브라우저 실행
		*/
		/*
		 연결 확인 = ngrok
		 stage('Check Git Info') {
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
		
		// gradle build => war파일을 다시 생성 
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
		
		// Docker Build 
		stage('Docker Build') {
			steps {
				sh '''
					docker build -t mindory0144/total-app:latest .
				   '''
			}
		}
		
		stage('Docker Login') {
		  	steps {
		   		 withCredentials([usernamePassword(
		        	credentialsId: 'dockerhub-creds',
		       		usernameVariable: 'DH_USER',
		        	passwordVariable: 'DH_PASS'
		    )]) {
		     	 sh '''
		       		 echo "$DH_PASS" | docker login -u "$DH_USER" --password-stdin
		      		'''
		    	}
		  	}
		}
		
		// Docker Push
		stage('Docker Push') {
		  	steps {
		    	sh '''
		      		docker push mindory0144/total-app:latest
		    	'''
		  	}
		}
		
		// 실행 명령 
		
		stage('Deploy to MiniKube') {
			steps {
				sh '''
					kubectl delete deployment total-app || true
					sudo -u sist /usr/local/bin/kubectl apply -f /var/lib/jenkins/k8s/deployment.yaml
					sudo -u sist /usr/local/bin/kubectl rollout restart deployment/totalapp-deployment
					sudo -u sist /usr/local/bin/kubectl rollout status deployment/totalapp-deployment
				   '''
			}
		}
		
	}
}