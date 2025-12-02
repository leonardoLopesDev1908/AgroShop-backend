pipeline {
    agent any
    
    stages {
        stage('Start Services') {
            steps {
                sh 'docker-compose up -d'
                sh '''
                    while ! docker-compose exec mysql mysqladmin ping -h localhost --silent; do
                        sleep 5
                    done
                '''
            }
        }
        
        stage('Run Tests') {
            environment {
                SPRING_DATASOURCE_URL = 'jdbc:mysql://localhost:3307/testdb'
                SPRING_DATASOURCE_USERNAME = 'root'
                SPRING_DATASOURCE_PASSWORD = 'secret'
            }
            steps {
                sh './mvnw clean test'
            }
        }
        
        stage('Stop Services') {
            steps {
                sh 'docker-compose down'
            }
        }
    }
}