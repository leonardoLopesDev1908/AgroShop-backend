pipeline {
	agent any
	
	stages{
		stage('Build DB'){
			steps{
				sh 'docker compose up -d'
				sh 'sleep 10'
			}
		}
		
		stage('Build'){
			steps{
				sh 'mvn clean package -DskipTests'
			}
		}
	}
	
	post {
		success{
			echo 'Pipeline ok'
		}
		
		failure{
			echo 'Erro na pipeline'
		}
	}
}