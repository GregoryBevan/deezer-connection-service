node {
	stage 'Checkout'
	checkout scm
  
	stage 'Compile'
	sh './gradlew clean build -x test'
	
	stage 'Unit test'
	sh (script: './gradlew test', returnStatus: true)
    step([$class: 'JUnitResultArchiver', testResults: '**/build/test-results/test/TEST-*.xml'])
 }