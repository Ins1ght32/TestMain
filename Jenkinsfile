pipeline {
	agent any
	stages {
		stage('OWASP Dependency Check') {
			steps {
				dependencyCheck additionalArguments: '--format HTML --format XML --nvdApiKey 7ad48849-c21a-49f4-9ddb-85151d39d039', odcInstallation: 'OWASP Dependency-Check Vulnerabilities'
			}
			post {
				success {
					dependencyCheckPublisher pattern: 'dependency-check-report.xml'
				}
			}
		}
		
		stage('SonarQube') {
			steps {
				script {
					def scannerHome = tool 'SonarQube';
						withSonarQubeEnv('SonarQube') {
						sh "${scannerHome}/bin/sonar-scanner -Dsonar.projectKey=Test -Dsonar.sources=."
						}
					}
				}
		}
		
		stage('Integration') {
			steps {
				script {
                    // Check if the web app is up
                    sh '''
                        MAX_RETRIES=5
                        RETRY_COUNT=0
                        SUCCESS=0

                        while [ $RETRY_COUNT -lt $MAX_RETRIES ]; do
                            if curl -sSf http://172.20.0.4:80 >/dev/null; then
                                echo "Web app is up and running."
                                SUCCESS=1
								echo 'Now...'
								echo 'Visit http://localhost to see your PHP application in action.'
                                break
                            else
                                echo "Web app is not up yet. Retrying in 10 seconds..."
                                sleep 10
                                RETRY_COUNT=$((RETRY_COUNT + 1))
                            fi
                        done

                        if [ $SUCCESS -ne 1 ]; then
                            echo "Web app is not responding after $MAX_RETRIES attempts. Failing the build."
                            exit 1
                        fi
                    '''
                }
			}
		}
		stage('UI Testing') {
			agent {
				docker {
					image 'maven:3-alpine' 
					args '-v /root/.m2:/root/.m2' 
				}
			}
			steps {
				sh 'mvn -B -DskipTests clean package'
				sh 'mvn test'
			}
			post {
				always {
					junit 'target/surefire-reports/*.xml'
				}
			}
		}
		
		stage ('End Application') {
			steps {
				input message: 'Finished using the web site? (Click "Proceed" to continue)'
			}
		}
	}
}