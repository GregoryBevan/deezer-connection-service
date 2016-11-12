node {
	// Mark the code checkout 'stage'....
	stage 'Checkout'
 
	// Checkout code from repository
	checkout scm
  
	// Mark the code build 'stage'....
	stage 'Build'
	sh 'chmod +x gradlew | ./gradlew build --info'
 }