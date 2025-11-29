pipeline {
    agent any

    stages {

        stage('Subir banco') {
            steps {
                sh 'docker compose down || true'    
                sh 'docker compose up -d'            
                sh 'sleep 10'                       
            }
        }

        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Build') {
            steps {
                sh './mvnw clean package -DskipTests'
            }
        }

        stage('Test') {
            steps {
                sh './mvnw test'
            }
        }

    }
}
