#!groovy

node{
    stage('Build') {
        echo "Running ${env.BUILD_ID} [branch:${env.BRANCH_NAME}] on ${env.JENKINS_URL}"
        checkout scm //Jenkins with multibranch support
        sh 'echo $PATH'
        sh 'mvn clean install -DskipTests -U'
    }
}