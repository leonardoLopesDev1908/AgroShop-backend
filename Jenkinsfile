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

		stage('OWASP Dependency-Check Vulnerabilities') {

			steps {
				dependencyCheck additionalArguments: ''' 
							-o './'
							-s './'
							-f 'ALL' 
							--prettyPrint''', odcInstallation: 'OWASP Dependency-Check Vulnerabilities'
				
				dependencyCheckPublisher pattern: 'dependency-check-report.xml'
			}
		}

		stage('Tests'){
			steps{
				sh 'mvn test'
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