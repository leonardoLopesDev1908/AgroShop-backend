pipeline {
    agent any

    stages{ 

        stage('Start DB') {
            steps {
                sh 'docker compose up -d db'
            }
        }
        
        stage('Checkout'){
            steps{
                checkout scm
            }
        }

        stage('Build') {
            steps{
                sh './mvnw clean package -DskipTests'
            }
        }

        stage('Test'){
            steps{
                sh './mvnw test'
            }
        }

    }
}
