def call(def server, def port) {
    sh 'echo deploying to server ${server}'
    sh 'echo deploying to server port ${port}'
}