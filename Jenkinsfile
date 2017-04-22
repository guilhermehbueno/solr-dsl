#!groovy

node{
    stage('Build') {
        echo "Running ${env.BUILD_ID} [branch:${env.BRANCH_NAME}] on ${env.JENKINS_URL}"
        checkout scm //Jenkins with multibranch support
        sh '/opt/maven/apache-maven-3.5.0/bin/./mvn clean install -DskipTests -U'
    }
}