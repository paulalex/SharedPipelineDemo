def call(Map pipelineParams) {
    // evaluate the body block, and collect configuration into the object
//     def pipelineParams= [:]
//     body.resolveStrategy = Closure.DELEGATE_FIRST
//     body.delegate = pipelineParams
//     body()

    pipeline {
        agent any
        stages {
            stage('checkout git') {
                steps {
                    git branch: pipelineParams.branch, credentialsId: 'GitCredentials', url: pipelineParams.scmUrl
                }
            }

            stage('build') {
                steps {
                    sh 'echo building...'
                }
            }

            stage ('test') {
                steps {
                    parallel (
                        "unit tests": { sh 'echo unit test' },
                        "integration tests": { sh 'echo integration-test' }
                    )
                }
            }

            stage('deploy developmentServer'){
                steps {
                    deploy(pipelineParams.developmentServer, pipelineParams.serverPort)
                }
            }
        }
        post {
            failure {
                mail to: pipelineParams.email, subject: 'Pipeline failed', body: "${env.BUILD_URL}"
            }
        }
    }
}