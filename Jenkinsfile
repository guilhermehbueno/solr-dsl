#!groovy

node{
    stage('Build') {
        //notifyBuild()
        echo "Running ${env.BUILD_ID} [branch:${env.BRANCH_NAME}] on ${env.JENKINS_URL}"
        deleteDir() //delete the cloned dir before each build
        checkout scm //Jenkins with multibranch support
        sh '/opt/maven/bin/mvn clean install -DskipTests -U'
    }
}