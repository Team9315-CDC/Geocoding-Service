# Team 9315

Git repository for Junior Design Project - Geocoding Service for CDC

## Release Notes - Version 1.0.0
### New Features 
1. Spring Boot Application with HTML/JavaScript Front End prototype
2. Batch Geocoding of addresses 
3. Single Address Geocoding
4. Batch processing from an SQL database

### Notable Bugs and Defects
1. To Run Single Address Geocoding, CORS must be utilized, and Access-Control-Allow-Origin must be allowed. 

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

### SQL Configuration

#### Running the script
The `SQLQuery` can be set to connect to an SQL server automatically (credentials saved in the code) or connect
using the user's credentials (inputted at runtime). The geocoder API has a max limit of 10,000 entires per
request, so the query will only pull 10,000 addresses at a time. If the max number of addresses are pulled
from the database, the script will attempt another query. The `SQLQuery` only takes addresses that have not
previously been checked and do not have a NULL or blank value in the `street_addr1` field.

#### Fields in the Database
The `SQLQuery` class was configured to work with the NBS database structure. Addresses to geocode are taken
from the `Postal_locator` table and successful requests are written to the `Geocoding_Results` table. Since
the `Geocoding_Results` table is an old table, not all fields are used and some fields need to be updated.
Currently, the `longitude` and `latitude` fields are integers, which conflicts with the double values that the
US Census provides. We added an additional `true_longitude` and `true_latitude` to compensate for this. 
Additionally, for simplicity, we used the `postal_locator_uid` as the `geocoding_result_uid`. Ideally, these
should be different and the `geocoding_result_uid` should be set automatically upon adding a new row to the
table.

#### Geocoding Status Codes
Both the `Postal_locator` table and the `Geocoding_Results` have a field to store the status of the request
(`geocode_match_ind` and `result_type` respectively). We use the following codes to indicate whether or not an
address has been geocoded:
 - NULL : the address has not been geocoded
 - S : (success) the address was successfully geocoded and had an exact match
 - N : (non-exact match) the address was geocoded but did not have an exact match. The corrected address is
 the one stored in the `Geocoding_Resutls` table
 - F : (failure) the address did not have a good match from the geocoding API. This may because of too little
 information (the address was missing a field), the address was misspelled, or the address no longer exists.

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

Currently Gradle is configured to use the JDBC driver for an MS SQL server. This can be
changed easily by simply updating the maven repository in the dependency section of
`build.gradle`. (Note that if you change the JDBC driver to work with your SQL server,
you may need to update the flavor of the SQL statements in `SQLQuery`)

#### Running with Gradle 
Use   
`gradle run`
This calls ``java main.Java.Server``

#### Cleaning
Use
``gradle clean``
which will remove the `build/` directory.
