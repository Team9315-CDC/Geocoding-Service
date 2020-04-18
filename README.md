# Team 9315

Git repository for Junior Design Project - Geocoding Service for CDC

## Pre-requisites
1. Gradle version: 5.4.x or 6.2.x 
2. JVM: 12.0.1 or 13.0.2
  
## Download
`git clone https://github.com/Team9315-CDC/Geocoding-Service.git`

## Build and Running 

Tested with the following versions: 
1. Gradle version: 5.4.1 or 6.2.0 
2. JVM: 12.0.1 or 13.0.2
3. OS: Mac OS X 10.15.3 x86_64

### SpringBoot
To Run Spring Boot Application with Front End:
Note: no building is required.
1. From Project Root in Terminal/Command Prompt: `cd spring-boot`
2. `./gradlew bootRun` Note: Gradle getting stuck at 75% is normal 
3. Go to ``localhost:8080/index.html``

### Gradle 
#### Building with Gradle 
To Build Gradle Example (Terminal/Command Prompt only): 
Use
`gradle build` 
in the Geocoding-Service/ folder to build the source files located in `src/main/java`.  

#### Runnig with Gradle 
Use   
`gradle run`
This calls ``java main.Java.Server``

#### Cleaning
Use
``gradle clean``
which will remove the `build/` directory.




