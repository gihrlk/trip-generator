# Trip Generator
Trip Generator Coding Challenge for Littlepay

### Assumptions
1. Input JSON file is well formed and is not missing data. Therefore, input tap data is not validated.
2. There are only three bus stops. 
3. The cost to travel between the stops are stored in TripCostStore class in both directions. i.e. From Stop1 to Stop2 and also Stop2 to Stop1. These costs should ideally retrieve from a datasource or a configuration file.
4. Customer must always tap OFF before they could tap ON again. Input data will only have one tap ON without the tap OFF for given Primary Account Number.
5. Customer's corresponding tap ON and tap OFF entries will always have the same CompanyId and BusId. If tap OFF entries CompanyId and BusId different from the tap ON's CompanyId and BusId, tap OFF will be ignored.
6. CompanyId and BusId only used when finding the corresponding tap OFF entry.

### How to run the application
Application code is available at [GitHub repository](https://github.com/gihrlk/trip-generator)

You need Java 1.8+ to run the application.

Download or clone the project from GitHub repo and navigate to the project's root directory.

Project created with SpringBoot Maven wrapper. To use the wrapper you need JAVA_HOME system variable. If you already have Maven installed then you can use it.

To execute the test cases using the Maven wrapper, run
##### ./mnvw test

To build the project using the Maven wrapper, run
##### ./mnvw package

Application jar will be saved in target directory.

To run the application jar file you need to provide two arguments.<br>
Argument 1: Path to the input JSON file<br>
Argument 2: Path to save the output JSON file
##### java -jar target\trip-generator-project-0.0.1-SNAPSHOT.jar src\main\resources\tests\input\tap-data-1.json src\main\resources\tests\output\trip-data-1.json

### Test data
Application has been tested with sample input files shared in src/main/resources/tests/input/ directory.

Description of each test data set is below.

##### tap-data-1.json
Input file contains a single journey. This will generate a COMPLETED trip.
##### tap-data-2.json
Input file contains multiple journeys of the same customer. This will generate two COMPLETED trips.
##### tap-data-3.json
Input file contains multiple journeys of the same customer. This will generate two COMPLETED trips and one CANCELLED trip.
##### tap-data-4.json
Input file contains multiple journeys of the same customer. This will generate two COMPLETED trips, one CANCELLED trip and one INCOMPLETE trip.
##### tap-data-5.json
Input file contains multiple journeys of multiple customers. This will generate five COMPLETED trips, two CANCELLED trips and two INCOMPLETE trips.
