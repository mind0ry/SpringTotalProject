pipeline {
	agent any
	stages {
		stage('Git Check Test') {
			steps {
				git branch: 'main'
			}
		}
		
		stage('Check Git Info') {
			steps {
				sh '''
					echo "===Git Info==="
					git branch
					git log -1
				   '''
			}
		}
	}
}