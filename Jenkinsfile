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
			environment{
				NVD_API_KEY:'leonardoSilva/81184126-957b-41aa-bb12-82914ba54eee'
			}
			steps {
				dependencyCheck additionalArguments: ''' 
							-o './'
							-s './'
							-f 'ALL' 
							--prettyPrint''', odcInstallation: 'OWASP Dependency-Check Vulnerabilities'
				
				dependencyCheckPublisher pattern: 'dependency-check-report.xml'
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