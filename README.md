# Team 9315

Git repository for Junior Design Project - Geocoding Service for CDC

## Pre-requisites
1. Gradle version: 5.4.x or 6.2.x 
2. JVM: 10.0.x or 12.0.x or 13.0.x
  
## Download
`git clone https://github.com/Team9315-CDC/Geocoding-Service.git`

## Build and Running 

Tested with the following versions: 
1. Gradle version: 5.4.x or 6.2.x 
2. JVM: 10.0.x or 12.0.x or 13.0.x
3. OS: Mac OSX 10.15.3 x86_64

### Spring Boot
Spring Boot will handle dependencies on Run using Gradle 
To Run Spring Boot Application with Front End:
Note: no building is required.
1. From Project Root in Terminal/Command Prompt: `cd spring-boot`
2. `./gradlew bootRun` 
3. Go to ``localhost:8080/index.html`` in browser. 

#### Troubleshooting Spring Boot
1. `./gradlew bootRun` may get stuck at 75%. This is normal behavior. Just be on the lookout for "App Started". 
2. If the above command fails, try utilizing `./mvnw spring-boot:run`. Note Maven Version: 4.0.0 is required. 
3. To Run Single Address Geocoding, CORS must be utilized, and Access-Control-Allow-Origin must be allowed. 

### Gradle 
#### Building with Gradle 
Gradle will handle dependencies on Build 
To Build Gradle Example (Terminal/Command Prompt only): 
Use
`gradle build` 
in the Geocoding-Service/ folder to build the source files located in `src/main/java`.  

#### Running with Gradle 
Use   
`gradle run`
This calls ``java main.Java.Server``

#### Cleaning
Use
``gradle clean``
which will remove the `build/` directory.


## Release Notes 
### New Features 
1. Spring Boot Application with HTML/JavaScript Front End prototype
2. Batch Geocoding of addresses 
3. Single Address Geocoding

### Notable Bugs and Defects
1. To Run Single Address Geocoding, CORS must be utilized, and Access-Control-Allow-Origin must be allowed. 




